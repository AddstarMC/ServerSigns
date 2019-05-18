package au.com.addstar.serversigns.signs;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.parsing.CommandType;
import au.com.addstar.serversigns.parsing.command.ReturnServerSignCommand;
import au.com.addstar.serversigns.taskmanager.tasks.PermissionRemovePlayerTask;
import au.com.addstar.serversigns.utils.InventoryUtils;
import au.com.addstar.serversigns.utils.StringUtils;
import au.com.addstar.serversigns.parsing.command.ConditionalServerSignCommand;
import au.com.addstar.serversigns.parsing.command.ServerSignCommand;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.tasks.PermissionGrantPlayerTask;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.utils.ItemUtils;
import au.com.addstar.serversigns.utils.TimeUtils;
import au.com.addstar.serversigns.utils.TimeUtils.TimeUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ServerSignExecutor {
    private ServerSignsPlugin plugin;

    public ServerSignExecutor(ServerSignsPlugin instance) {
        this.plugin = instance;
    }

    public void executeSignFull(Player player, ServerSign sign, PlayerInteractEvent event) {
        try {
            if ((event != null) && (sign.getCancelMode().equals(CancelMode.ALWAYS))) {
                event.setCancelled(true);
            }


            if (!isReady(player, sign)) {
                if ((event != null) && (sign.getCancelMode().equals(CancelMode.FAIL_ONLY))) {
                    event.setCancelled(true);
                }
                return;
            }


            if (sign.getCurrentLoop() > 0) {
                if (sign.shouldDisplayInternalMessages()) {
                    this.plugin.send(player, Message.LOOP_MUST_FINISH);
                }
                return;
            }
            boolean looped = sign.getLoops() > -1;


            if (!looped) {
                List<TaskManagerTask> tasks = new ArrayList<>();


                List<PermissionGrantPlayerTask> grantTasks = null;
                if ((sign.getGrantPermissions() != null) && (!sign.getGrantPermissions().isEmpty())) {
                    if (((!this.plugin.hookManager.vault.isHooked()) || (!this.plugin.hookManager.vault.getHook().hasPermissions())) && ((this.plugin.config.getPermissionAddCommand().isEmpty()) || (this.plugin.config.getPermissionRemoveCommand().isEmpty()))) {
                        if (sign.shouldDisplayInternalMessages()) {
                            this.plugin.send(player, Message.FEATURES_NOT_AVAILABLE);
                        }
                        ServerSignsPlugin.log("ServerSign at " + sign.getLocationString() + " has been activated, but cannot execute as it is attempting to grant permissions but no Vault hook or config-defined commands exist");
                        return;
                    }
                    grantTasks = grantPermissions(player.getUniqueId(), 0L, sign.getGrantPermissions(), tasks);
                }


                createCommandTasks(sign, tasks, player, event == null ? null : event.getAction());
                if (this.plugin.inputOptionsManager.hasCompletedAnswers(player)) {
                    this.plugin.inputOptionsManager.getCompletedAnswers(player, true);
                }


                if ((sign.getGrantPermissions() != null) && (!sign.getGrantPermissions().isEmpty())) {
                    removePermissions(player.getUniqueId(), 0L, grantTasks, tasks);
                }


                if ((sign.getCommands().size() > 0) && (tasks.isEmpty())) {
                    return;
                }
                for (TaskManagerTask task : tasks) {
                    this.plugin.taskManager.addTask(task);
                }
            }


            sign.setLastGlobalUse(System.currentTimeMillis());
            if (sign.getCooldown() > 0L) {
                sign.addLastUse(player.getUniqueId());
            }


            removePriceItems(player, sign);
            removeXP(player, sign);
            removeMoney(player, sign);


            if ((event != null) && (sign.getCancelMode().equals(CancelMode.SUCCESS_ONLY))) {
                event.setCancelled(true);
            }


            sign.incrementUseTally();
            if ((sign.getUseLimit() > 0) &&
                    (sign.getUseTally() >= sign.getUseLimit())) {
                ServerSignsPlugin.log("ServerSign at '" + sign.getLocationString() + "' has reached its uses limit and has expired");
                this.plugin.serverSignsManager.expire(sign);
                return;
            }


            if (looped) {
                executeSignLooped(sign, player);
                return;
            }


            this.plugin.serverSignsManager.save(sign);
        } catch (Exception ex) {
            ServerSignsPlugin.log("Exception generated during execution of a ServerSign!", java.util.logging.Level.SEVERE, ex);
            ex.printStackTrace();
        }
    }

    public void executeSignLooped(ServerSign sign, Player executor) {
        try {
            if ((sign == null) || (this.plugin.serverSignsManager.getServerSignByLocation(sign.getLocation()) == null)) {
                return;
            }

            int loops = sign.getLoops();
            int currentLoop = sign.getCurrentLoop() > 0 ? sign.getCurrentLoop() : 1;
            int loopDelay = sign.getLoopDelayInSecs();


            sign.setCurrentLoop(currentLoop);


            for (TaskManagerTask task : createCommandTasks(sign, null, executor, null)) {
                this.plugin.taskManager.addTask(task);
            }

            if (loops == 0) {
                executeLoopRunnable(sign, executor, loopDelay);
            } else if ((loops > -1) && (currentLoop < loops)) {
                sign.setCurrentLoop(++currentLoop);
                executeLoopRunnable(sign, executor, loopDelay);
            } else {
                sign.setCurrentLoop(0);
            }


            this.plugin.serverSignsManager.save(sign);
        } catch (Exception ex) {
            ServerSignsPlugin.log("Exception generated during execution of a looped ServerSign!", java.util.logging.Level.SEVERE, ex);
        }
    }

    private void executeLoopRunnable(final ServerSign sign, final Player executor, long loopDelay) {
        new org.bukkit.scheduler.BukkitRunnable() {
            public void run() {
                ServerSignExecutor.this.executeSignLooped(sign, executor.isOnline() ? executor : executor == null ? null : null);
            }
        }.runTaskLater(this.plugin, loopDelay * 20L);
    }


    private List<TaskManagerTask> createCommandTasks(ServerSign sign, List<TaskManagerTask> existingList, Player executor, Action eventAction) {
        List<TaskManagerTask> tasks = existingList == null ? new ArrayList() : existingList;

        ProcessingData processingData = new ProcessingData();
        for (ServerSignCommand command : sign.getCommands()) {
            if ((eventAction == null) || (
                    ((command.getInteractValue() == 0) || (command.getInteractValue() != 1) || (eventAction.equals(Action.LEFT_CLICK_BLOCK))) && ((command.getInteractValue() != 2) || (eventAction.equals(Action.RIGHT_CLICK_BLOCK))))) {


                processingData = processConditionalCommand(sign, executor, command, processingData);
                if (processingData.lastResult != 1) {
                    if (processingData.lastResult == 2) {
                        break;
                    }

                    tasks.addAll(command.getTasks(executor, this.plugin, getInjectedCommandReplacements(sign, true)));
                }
            }
        }
        return tasks;
    }

    private ProcessingData processConditionalCommand(ServerSign sign, Player executor, ServerSignCommand command, ProcessingData data) {
        if (data.skipUntilLevel > 0) {
            if (((command instanceof ConditionalServerSignCommand)) && (((ConditionalServerSignCommand) command).isEndifStatement())) {
                data.endifLevel += 1;
                if (data.skipUntilLevel == data.endifLevel) {
                    data.skipUntilLevel = -1;
                }
                data.lastResult = 1;
                return data;
            }
            data.lastResult = 1;
            return data;
        }


        if ((command instanceof ReturnServerSignCommand)) {
            data.lastResult = 2;
            return data;
        }


        if ((command instanceof ConditionalServerSignCommand)) {
            ConditionalServerSignCommand condCommand = (ConditionalServerSignCommand) command;
            if (condCommand.isIfStatement()) {
                data.ifLevel += 1;
                if (!condCommand.meetsAllConditions(executor, sign, this.plugin)) {
                    data.skipUntilLevel = data.ifLevel;
                    data.lastResult = 1;
                    return data;
                }
            } else if (condCommand.isEndifStatement()) {
                data.endifLevel += 1;
            }
        }

        data.lastResult = 0;
        return data;
    }

    private Map<String, String> getInjectedCommandReplacements(ServerSign sign, boolean looped) {
        Map<String, String> replacementMap = new java.util.HashMap();

        replacementMap.put("<usesTally>", sign.getUseTally() + "");
        replacementMap.put("<signLoc>", sign.getWorld() + "," + sign.getX() + "," + sign.getY() + "," + sign.getZ());
        if (sign.getUseLimit() > 0) {
            replacementMap.put("<usesLeft>", sign.getUseLimit() - sign.getUseTally() + "");
            replacementMap.put("<usesLimit>", sign.getUseLimit() + "");
        }
        if (looped) {
            replacementMap.put("<loopCount>", sign.getCurrentLoop() + "");
            replacementMap.put("<loopsLeft>", sign.getLoops() - sign.getCurrentLoop() + "");
        }

        return replacementMap;
    }

    private boolean isReady(Player player, ServerSign sign) {
        if ((SVSMetaManager.hasInclusiveMeta(player, SVSMetaKey.YES)) && (!SVSMetaManager.getMeta(player).getValue().asServerSign().equals(sign))) {
            SVSMetaManager.removeMeta(player);
        }

        if ((sign.getTimeLimitMinimum() > 0L) && (System.currentTimeMillis() < sign.getTimeLimitMinimum())) {
            this.plugin.send(player, Message.TIMELIMIT_WAITING, "<string>", TimeUtils.getTimeSpan(sign.getTimeLimitMinimum() - System.currentTimeMillis(), TimeUnit.SECONDS, TimeUnit.YEARS, false, false));
            return false;
        }
        if ((sign.getTimeLimitMaximum() > 0L) && (System.currentTimeMillis() >= sign.getTimeLimitMaximum())) {
            this.plugin.send(player, Message.TIMELIMIT_EXPIRED, "<string>", TimeUtils.getTimeSpan(System.currentTimeMillis() - sign.getTimeLimitMaximum(), TimeUnit.SECONDS, TimeUnit.YEARS, false, false));
            return false;
        }

        if ((!sign.getCancelPermission().isEmpty()) &&
                (player.hasPermission(sign.getCancelPermission()))) {
            if (sign.shouldDisplayInternalMessages()) {
                if (sign.getCancelPermissionMessage().isEmpty()) {
                    this.plugin.send(player, Message.CANCELLED_DUE_TO_PERMISSION);
                } else {
                    this.plugin.send(player, StringUtils.colour(sign.getCancelPermissionMessage()));
                }
            }
            return false;
        }


        if (!hasPermission(player, sign)) {
            if (sign.shouldDisplayInternalMessages()) {
                if (sign.getPermissionMessage().isEmpty()) {
                    this.plugin.send(player, Message.NOT_ENOUGH_PERMISSIONS);
                } else {
                    this.plugin.send(player, StringUtils.colour(sign.getPermissionMessage()));
                }
            }
            return false;
        }

        if (!canUse(player, sign)) {
            if (sign.shouldDisplayInternalMessages()) {
                this.plugin.send(player, Message.NOT_READY, "<cooldown>", getCooldownLeft(player, sign));
            }
            return false;
        }

        return (!needConfirmation(player, sign)) && (canAffordXP(player, sign)) && (canAffordCost(player, sign)) && (hasAnsweredQuestions(player, sign)) && ((!hasHeldRequirements(sign)) || (meetsHeldItemRequirements(player, sign))) && ((!hasPriceItem(sign)) || (canAffordPriceItem(player, sign)));
    }

    private boolean hasAnsweredQuestions(Player executor, ServerSign sign) {
        List<String> displayedOptionIds = new ArrayList();

        ProcessingData processingData = new ProcessingData();
        for (ServerSignCommand command : sign.getCommands()) {
            processingData = processConditionalCommand(sign, executor, command, processingData);
            if (processingData.lastResult != 1) {
                if (processingData.lastResult == 2) {
                    break;
                }

                if (command.getType() == CommandType.DISPLAY_OPTIONS) {
                    String data = command.getUnformattedCommand();
                    PlayerInputOptions options = sign.getInputOption(data);
                    if ((options != null) && (options.getAnswersLength() > 0)) {
                        displayedOptionIds.add(data);
                    } else {
                        ServerSignsPlugin.log("An invalid '/svs option' question has been encountered at " + sign.getLocationString());
                    }
                }
            }
        }
        if (displayedOptionIds.size() > 0) {
            if (!this.plugin.inputOptionsManager.hasCompletedAnswers(executor)) {
                if (!this.plugin.inputOptionsManager.isSuspended(executor)) {
                    this.plugin.inputOptionsManager.suspend(executor, displayedOptionIds, sign);
                }
                return false;
            }
            if (this.plugin.inputOptionsManager.getCompletedAnswers(executor, false).size() < displayedOptionIds.size()) {
                Map<String, String> currentlyAnswered = this.plugin.inputOptionsManager.getCompletedAnswers(executor, false);
                for (Map.Entry<String, String> entry : currentlyAnswered.entrySet()) {
                    displayedOptionIds.remove(entry.getKey());
                }

                this.plugin.inputOptionsManager.suspend(executor, displayedOptionIds, sign);
                return false;
            }
        }


        return true;
    }

    private boolean hasPermission(Player player, ServerSign sign) {
        if ((player.hasPermission("serversigns.admin")) || (player.hasPermission("serversigns.use.*"))) {
            return true;
        }

        if (sign.getPermissions().isEmpty()) {
            return player.hasPermission("serversigns.use");
        }

        for (String perm : sign.getPermissions()) {
            if (!player.hasPermission("serversigns.use." + perm)) {
                return false;
            }
        }

        return true;
    }

    private boolean canUse(Player player, ServerSign sign) {
        return ((sign.getGlobalCooldown() == 0L) || (System.currentTimeMillis() - sign.getLastGlobalUse() >= sign.getGlobalCooldown() * 1000L)) && (
                (sign.getCooldown() == 0L) || (System.currentTimeMillis() - sign.getLastUse(player.getUniqueId()) >= sign.getCooldown() * 1000L));

    }

    private String getCooldownLeft(Player player, ServerSign sign) {
        long globalLeft = sign.getGlobalCooldown() * 1000L - (System.currentTimeMillis() - sign.getLastGlobalUse());
        long normalLeft = sign.getCooldown() * 1000L - (System.currentTimeMillis() - sign.getLastUse(player.getUniqueId()));
        long applicable = globalLeft > normalLeft ? globalLeft : normalLeft;
        String toRet = TimeUtils.getTimeSpan(applicable, TimeUtils.TimeUnit.SECONDS, TimeUtils.TimeUnit.YEARS, true, false);
        return toRet.isEmpty() ? "<1s" : toRet;
    }

    private boolean needConfirmation(Player player, ServerSign sign) {
        if (sign.isConfirmation()) {
            if (SVSMetaManager.hasInclusiveMeta(player, SVSMetaKey.YES)) {
                SVSMetaManager.removeMeta(player);
                return false;
            }

            if (sign.shouldDisplayInternalMessages()) {
                if (!sign.getHeldItems().isEmpty()) {
                    this.plugin.send(player, Message.NEED_CONFIRMATION_HELD_ITEMS);
                    for (ItemStack item : sign.getHeldItems()) {
                        this.plugin.send(player, ItemUtils.getDescription(item, this.plugin.config.getMessageColour()));
                    }
                }

                if (!sign.getPriceItems().isEmpty()) {
                    this.plugin.send(player, Message.NEED_CONFIRMATION_PRICE_ITEMS);
                    for (ItemStack pi : sign.getPriceItems()) {
                        this.plugin.send(player, ItemUtils.getDescription(pi, this.plugin.config.getMessageColour()));
                    }
                }

                if (sign.getXP() > 0) {
                    this.plugin.send(player, Message.NEED_CONFIRMATION_XP, "<integer>", sign.getXP() + "");
                }

                if (!sign.getConfirmationMessage().isEmpty()) {
                    this.plugin.send(player, sign.getConfirmationMessage());
                }

                if ((sign.getPrice() > 0.0D) && (this.plugin.hookManager.vault.isHooked()) && (this.plugin.hookManager.vault.getHook().hasEconomy())) {
                    this.plugin.send(player, Message.NEED_CONFIRMATION_COST, "<price>", sign.getPrice() + "", "<currency>", this.plugin.config.getCurrencyString());
                } else {
                    this.plugin.send(player, Message.NEED_CONFIRMATION);
                }
            }

            SVSMetaManager.setMeta(player, new SVSMeta(SVSMetaKey.YES, new SVSMetaValue(sign)));
            return true;
        }

        return false;
    }

    private boolean canAffordXP(Player player, ServerSign sign) {
        if (sign.getXP() > 0) {
            if (player.getLevel() >= sign.getXP()) {
                return true;
            }

            if (sign.shouldDisplayInternalMessages()) {
                this.plugin.send(player, Message.NOT_ENOUGH_XP);
                this.plugin.send(player, Message.LEVELS_NEEDED, "<integer>", sign.getXP() - player.getLevel() + "");
            }
            return false;
        }
        return true;
    }

    private boolean canAffordCost(Player player, ServerSign sign) {
        if ((this.plugin.hookManager.vault.isHooked()) && (this.plugin.hookManager.vault.getHook().hasEconomy())) {
            double price = sign.getPrice();
            if (price > 0.0D) {
                if (this.plugin.hookManager.vault.getHook().getEconomy().has(player, price)) {
                    return true;
                }

                if (sign.shouldDisplayInternalMessages()) {
                    this.plugin.send(player, Message.NOT_ENOUGH_MONEY);
                }
                return false;
            }
        }

        return true;
    }

    private boolean hasPriceItem(ServerSign sign) {
        return !sign.getPriceItems().isEmpty();
    }

    private boolean canAffordPriceItem(Player player, ServerSign sign) {
        ItemStack[] items = sign.getPriceItems().toArray(new ItemStack[1]);
        Collection<ItemStack> leftover = InventoryUtils.scan(player.getInventory(), sign.getPIC(), false, items);

        if (!leftover.isEmpty()) {
            if (sign.shouldDisplayInternalMessages()) {
                this.plugin.send(player, Message.NOT_ENOUGH_ITEMS);
                for (ItemStack required : leftover) {
                    this.plugin.send(player, ItemUtils.getDescription(required, this.plugin.config.getMessageColour()));
                }
            }
            return false;
        }

        return true;
    }

    private boolean hasHeldRequirements(ServerSign sign) {
        return !sign.getHeldItems().isEmpty();
    }

    private boolean meetsHeldItemRequirements(Player player, ServerSign sign) {
        ItemStack held = player.getItemInHand();
        if ((held != null) && (!held.getType().equals(org.bukkit.Material.AIR))) {
            for (ItemStack stack : sign.getHeldItems()) {
                if (ItemUtils.compare(stack, held, sign.getHIC())) {
                    return true;
                }
            }
        }
        if (sign.shouldDisplayInternalMessages()) {
            this.plugin.send(player, Message.MUST_BE_HOLDING);
            for (ItemStack required : sign.getHeldItems()) {
                this.plugin.send(player, ItemUtils.getDescription(required, this.plugin.config.getMessageColour()));
            }
        }
        return false;
    }

    private void removeXP(Player player, ServerSign sign) {
        if (sign.getXP() > 0) {
            player.setLevel(player.getLevel() - sign.getXP());
            if (sign.shouldDisplayInternalMessages()) {
                this.plugin.send(player, Message.XP_REMOVED, "<levels>", sign.getXP() + "");
            }
        }
    }

    private boolean removeMoney(Player player, ServerSign sign) {
        if (sign.getPrice() > 0.0D) {
            if ((!this.plugin.hookManager.vault.isHooked()) || (!this.plugin.hookManager.vault.getHook().hasEconomy())) {
                ServerSignsPlugin.log("Unable to remove money from " + player.getName() + " because no Economy plugins exist!");
                return false;
            }

            this.plugin.hookManager.vault.getHook().getEconomy().withdrawPlayer(player, sign.getPrice());
            if ((this.plugin.config.getShowFundsRemovedMessage()) && (sign.shouldDisplayInternalMessages())) {
                String message = this.plugin.msgHandler.get(Message.FUNDS_WITHDRAWN);
                if (message.contains("<number>")) {
                    message = message.replaceAll("<number>", String.valueOf(sign.getPrice()));
                }

                if ((message.contains("<currency>")) && (!this.plugin.config.getCurrencyString().isEmpty())) {
                    String repl = this.plugin.config.getCurrencyString();
                    message = message.replaceAll("<currency>", java.util.regex.Matcher.quoteReplacement(repl));
                }

                this.plugin.send(player, message);
            }
            if (this.plugin.config.getSendPaymentsToBank()) {
                String bank = this.plugin.config.getDepositBankName();
                if (bank.isEmpty()) {
                    return true;
                }
                this.plugin.hookManager.vault.getHook().getEconomy().bankDeposit(bank, sign.getPrice());
            }
            return true;
        }

        return false;
    }

    private void removePriceItems(Player player, ServerSign sign) {
        if (sign.getPriceItems().isEmpty()) {
            return;
        }
        ItemStack[] items = sign.getPriceItems().toArray(new ItemStack[1]);
        if (!InventoryUtils.scan(player.getInventory(), sign.getPIC(), true, items).isEmpty()) {
            org.bukkit.Bukkit.getLogger().warning("A player has managed to execute a ServerSign without paying all the price items! (Location: " + sign.getLocation().toString() + ")");
        }

        player.updateInventory();
    }

    private List<PermissionGrantPlayerTask> grantPermissions(UUID player, long timestamp, List<String> permissions, List<TaskManagerTask> tasks) {
        assert (!permissions.isEmpty());
        List<PermissionGrantPlayerTask> list = new ArrayList();

        for (String perm : permissions) {
            PermissionGrantPlayerTask grantTask = new PermissionGrantPlayerTask(timestamp, perm, player, true);
            tasks.add(grantTask);
            list.add(grantTask);
        }

        return list;
    }

    private void removePermissions(UUID player, long timestamp, List<PermissionGrantPlayerTask> grantTasks, List<TaskManagerTask> tasks) {
        for (PermissionGrantPlayerTask grantTask : grantTasks) {
            tasks.add(new PermissionRemovePlayerTask(timestamp, player, grantTask, true));
        }
    }

    private class ProcessingData {
        public int ifLevel = 0;
        public int endifLevel = 0;
        public int skipUntilLevel = -1;


        public int lastResult = -1;

        private ProcessingData() {
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\signs\ServerSignExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */