package au.com.addstar.serversigns.listeners;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.ExecutableSVSR;
import au.com.addstar.serversigns.itemdata.ItemSearchCriteria;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.parsing.command.ServerSignCommand;
import au.com.addstar.serversigns.signs.CancelMode;
import au.com.addstar.serversigns.signs.PlayerInputOptions;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.utils.ItemUtils;
import au.com.addstar.serversigns.utils.StringUtils;
import au.com.addstar.serversigns.utils.TimeUtils;
import au.com.addstar.serversigns.translations.Message;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class AdminListener implements Listener {
    private final ServerSignsPlugin plugin;

    public AdminListener(ServerSignsPlugin instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeveloperJoin(PlayerJoinEvent event) {
        if ((event.getPlayer().getName().equalsIgnoreCase("ExLoki")) || (event.getPlayer().getName().equalsIgnoreCase("CalibeR93")) || (event.getPlayer().getName().equalsIgnoreCase("Steffencz"))) {
            if (this.plugin.config.getBroadcastDevelopers()) {
                org.bukkit.Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[ServerSigns] " + ChatColor.YELLOW + event.getPlayer().getName() + " is an author of ServerSigns!");
            }
            event.getPlayer().sendMessage("Running version: " + this.plugin.getDescription().getVersion());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void adminChatCheck(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (SVSMetaManager.hasMeta(player)) {
            SVSMeta meta = SVSMetaManager.getMeta(player);
            if (meta.getKey().equals(SVSMetaKey.LONG)) {
                event.setCancelled(true);
                meta.addValue(new SVSMetaValue(message.trim()));


                this.plugin.send(player, Message.LONG_COMMAND_AGAIN);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void adminInteractCheck(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if ((SVSMetaManager.hasExclusiveMeta(player, SVSMetaKey.YES)) && (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            Block block = event.getClickedBlock();
            if (block == null) {
                return;
            }
            Location location = block.getLocation();
            handleAdminInteract(location, player, player.getUniqueId());
            if (!SVSMetaManager.hasInclusiveMeta(player, SVSMetaKey.COPY))
                event.setCancelled(true);
        }
    }

    public void handleAdminInteract(Location clicked, CommandSender recipient, UUID adminUUID) {
        ServerSign sign = this.plugin.serverSignsManager.getServerSignByLocation(clicked);
        if ((sign == null) && (!this.plugin.config.getAnyBlock()) && (!this.plugin.config.getBlocks().contains(clicked.getBlock().getType()))) {
            return;
        }
        SVSMeta meta = SVSMetaManager.getMeta(adminUUID);

        boolean saveRemoveExit = false;
        switch (meta.getKey()) {
            case ADD:
                if (sign != null) sign.addCommand(meta.getValue().asServerSignCommand());
                else {
                    sign = new ServerSign(clicked, meta.getValue().asServerSignCommand());
                }
                this.plugin.send(recipient, Message.COMMAND_SET);
                saveRemoveExit = true;
                break;

            case CANCEL:
                if (sign == null) return;
                String raw = meta.getValue().asString();
                CancelMode mode = CancelMode.valueOf(raw);
                sign.setCancelMode(mode);

                this.plugin.send(recipient, Message.SET_CANCEL_MODE, "<mode>", raw);
                saveRemoveExit = true;
                break;

            case CANCEL_PERMISSION:
                if (sign == null) {
                    return;
                }
                sign.setCancelPermission(meta.getValue().asString());
                if (meta.hasValue(1)) {
                    sign.setCancelPermissionMessage(meta.getValue(1).asString());
                }
                this.plugin.send(recipient, Message.CANCEL_PERMISSION_SET);
                saveRemoveExit = true;
                break;

            case CONFIRMATION:
                if (sign == null) return;
                sign.setConfirmation(meta.getValue().asBoolean());
                if (meta.hasValue(1)) {
                    sign.setConfirmationMessage(meta.getValue(1).asString());
                }
                this.plugin.send(recipient, Message.CONFIRMATION_SET, "<boolean>", meta.getValue().asBoolean() + "");
                saveRemoveExit = true;
                break;

            case COOLDOWN_RESET:
                if (sign == null) return;
                sign.resetCooldowns();

                this.plugin.send(recipient, Message.RESET_COOLDOWN);
                saveRemoveExit = true;
                break;

            case COPY:
                ServerSign copiedSign = meta.hasValue(1) ? meta.getValue(1).asServerSign() : null;
                if ((copiedSign == null) && (sign == null)) {
                    return;
                }
                if (sign == null) {
                    sign = this.plugin.serverSignsManager.copy(copiedSign);
                    if (sign == null) {
                        this.plugin.send(recipient, "An error occurred, please refer to console for details.");
                        SVSMetaManager.removeMeta(adminUUID);
                        return;
                    }
                    sign.setLocation(clicked);

                    this.plugin.serverSignsManager.save(sign);
                    this.plugin.send(recipient, Message.COPY_SUCCESS);
                    if (meta.getValue().asBoolean()) this.plugin.send(recipient, Message.RIGHT_CLICK_PASTE);
                    else
                        SVSMetaManager.removeMeta(adminUUID);
                } else if (copiedSign == null) {
                    if (meta.hasValue(1)) meta.removeValue(1);
                    meta.addValue(new SVSMetaValue(sign));
                    this.plugin.send(recipient, Message.RIGHT_CLICK_PASTE);
                }

                return;

            case CREATE:
                sign = new ServerSign();
                sign.setLocation(clicked);

                this.plugin.send(recipient, Message.CREATE_SUCCESS);
                saveRemoveExit = true;
                break;

            case EDIT:
                if (sign == null) {
                    return;
                }
                int index = meta.getValue().asInt();
                ServerSignCommand cmd = meta.getValue(1).asServerSignCommand();

                if ((index > sign.getCommands().size()) || (index < 1)) {
                    this.plugin.send(recipient, Message.INVALID_INDEX);
                    return;
                }

                this.plugin.send(recipient, Message.COMMAND_EDITED);
                sign.editCommand(index - 1, cmd);
                saveRemoveExit = true;
                break;

            case GRANT:
                if (sign == null) {
                    return;
                }
                if (meta.getValue().asBoolean()) {
                    sign.addGrantPermissions(meta.getValue(1).asString());
                    this.plugin.send(recipient, Message.PERMISSION_SET);
                } else {
                    sign.removeGrantPermissions();
                    this.plugin.send(recipient, Message.PERMISSION_REMOVED);
                }

                saveRemoveExit = true;
                break;

            case HOLDING:
                if (sign == null) {
                    return;
                }
                if (meta.getValue().asObject() == null) {
                    sign.clearHeldItems();
                    this.plugin.send(recipient, Message.HOLDING_REMOVED);
                } else {
                    sign.addHeldItem(meta.getValue().asItemStack());
                    this.plugin.send(recipient, Message.HOLDING_SUCCESS);
                }

                saveRemoveExit = true;
                break;

            case HELD_ITEM_CRITERIA:
                if (sign == null) {
                    return;
                }
                sign.setHIC(new ItemSearchCriteria(meta.getValue().asBoolean(), meta.getValue(2).asBoolean(), meta.getValue(1).asBoolean(), meta.getValue(3).asBoolean()));


                this.plugin.send(recipient, Message.HELD_ITEM_CRITERIA_SET);
                saveRemoveExit = true;
                break;

            case IMPORT:
                if (sign == null) {
                    return;
                }
                Path path = java.nio.file.Paths.get(meta.getValue().asString());
                try {
                    List<String> commands = com.google.common.io.Files.readLines(path.toFile(), java.nio.charset.Charset.defaultCharset());
                    svsr = new ExecutableSVSR(this.plugin);
                    for (String command : commands)
                        svsr.execute(clicked, (Player) recipient, command);
                } catch (Exception ex) {
                    ExecutableSVSR svsr;
                    this.plugin.send(recipient, "An error occurred, please refer to console for details.");
                    ServerSignsPlugin.log("An error occurred while importing file '" + path.toString() + "'", Level.WARNING, ex);
                    return;
                }

                this.plugin.send(recipient, Message.IMPORT_SUCCESS, "<string>", path.toString());
                saveRemoveExit = true;
                break;

            case INSERT:
                if (sign == null) {
                    return;
                }
                int insertIndex = meta.getValue().asInt();
                ServerSignCommand insertCmd = meta.getValue(1).asServerSignCommand();

                if (insertIndex > sign.getCommands().size()) {
                    this.plugin.send(recipient, Message.INVALID_INDEX);
                    return;
                }

                this.plugin.send(recipient, Message.COMMAND_SET);
                sign.getCommands().add(insertIndex - 1, insertCmd);
                saveRemoveExit = true;
                break;

            case LIST:
                this.plugin.send(recipient, String.format("&6Coordinates: &e%s&7, &e%d&7, &e%d&7, &e%d", clicked.getWorld().getName(), Integer.valueOf(clicked.getBlockX()), Integer.valueOf(clicked.getBlockY()), Integer.valueOf(clicked.getBlockZ())));

                if (sign != null) {
                    if (!sign.getPermissions().isEmpty()) {
                        this.plugin.send(recipient, "&6Permissions: &e" + StringUtils.join(sign.getPermissions(), ", "));
                    }
                    if (!sign.getCancelPermission().isEmpty()) {
                        this.plugin.send(recipient, "&6Cancel Permission: &e" + sign.getCancelPermission());
                        if (!sign.getCancelPermissionMessage().isEmpty()) {
                            this.plugin.send(recipient, "&6Cancel Perm Message: &e" + sign.getCancelPermissionMessage());
                        }
                    }
                    if (!sign.getPermissionMessage().isEmpty()) {
                        this.plugin.send(recipient, "&6No Perm Message: &e" + sign.getPermissionMessage());
                    }
                    if (sign.getPrice() != 0.0D) {
                        this.plugin.send(recipient, "&6Price: &e" + sign.getPrice());
                    }
                    if (sign.getXP() != 0) {
                        this.plugin.send(recipient, "&6Xp Cost: &e" + sign.getXP());
                    }
                    if (sign.isConfirmation()) {
                        this.plugin.send(recipient, "&6Confirmation: &etrue" + (sign.getConfirmationMessage().isEmpty() ? "" : new StringBuilder().append(", &6Message: &e").append(sign.getConfirmationMessage()).toString()));
                    }
                    if (sign.getCooldown() != 0L) {
                        this.plugin.send(recipient, "&6Cooldown: &e" + TimeUtils.getTimeSpan(sign.getCooldown() * 1000L, TimeUtils.TimeUnit.SECONDS, TimeUtils.TimeUnit.YEARS, true, false));
                    }
                    if (sign.getGlobalCooldown() != 0L) {
                        this.plugin.send(recipient, "&6Global Cooldown: &e" + TimeUtils.getTimeSpan(sign.getGlobalCooldown() * 1000L, TimeUtils.TimeUnit.SECONDS, TimeUtils.TimeUnit.YEARS, true, false));
                    }
                    if (sign.getLoops() >= 0) {
                        this.plugin.send(recipient, "&6Loop count: &e" + sign.getLoops());
                        this.plugin.send(recipient, "&6Loop delay: &e" + sign.getLoopDelayInSecs() + "s");
                    }

                    if (sign.getUseLimit() > 0) {
                        this.plugin.send(recipient, "&6Use limit: &e" + sign.getUseLimit());
                    }
                    this.plugin.send(recipient, "&6Use tally: &e" + sign.getUseTally());

                    if (!sign.getPriceItems().isEmpty()) {
                        this.plugin.send(recipient, "&6Price Items: ");
                        for (ItemStack stack : sign.getPriceItems()) {
                            this.plugin.send(recipient, ItemUtils.getDescription(stack, this.plugin.config.getMessageColour()));
                        }

                        this.plugin.send(recipient, "&6Price Item Criteria: &a&oTrue &c&oFalse");
                        this.plugin.send(recipient, sign.getPIC().getColouredString(ChatColor.GREEN, ChatColor.RED));
                    }

                    if (!sign.getHeldItems().isEmpty()) {
                        this.plugin.send(recipient, "&6Held Items: ");
                        for (ItemStack stack : sign.getHeldItems()) {
                            this.plugin.send(recipient, ItemUtils.getDescription(stack, this.plugin.config.getMessageColour()));
                        }

                        this.plugin.send(recipient, "&6Held Item Criteria: ");
                        this.plugin.send(recipient, sign.getHIC().getColouredString(ChatColor.GREEN, ChatColor.RED));
                    }

                    if (!sign.getGrantPermissions().isEmpty()) {
                        this.plugin.send(recipient, "&6Grant Permissions: ");
                        for (String str : sign.getGrantPermissions()) {
                            this.plugin.send(recipient, "- " + str);
                        }
                    }

                    if (!sign.shouldDisplayInternalMessages()) {
                        this.plugin.send(recipient, "&6Silent: &etrue");
                    }


                    if (sign.getTimeLimitMinimum() > 0L) {
                        this.plugin.send(recipient, "&6Time Limit (min): &e" + TimeUtils.getFormattedTime(sign.getTimeLimitMinimum(), "d MMM yyyy hh:mm:ss a"));
                    }

                    if (sign.getTimeLimitMaximum() > 0L) {
                        this.plugin.send(recipient, "&6Time Limit (max): &e" + TimeUtils.getFormattedTime(sign.getTimeLimitMaximum(), "d MMM yyyy hh:mm:ss a"));
                    }

                    if (!sign.getInputOptions().isEmpty()) {
                        this.plugin.send(recipient, "&6'Option Menus' (Q+As): ");
                        for (PlayerInputOptions options : sign.getInputOptions()) {
                            this.plugin.send(recipient, "&bID: " + options.getName());
                            this.plugin.send(recipient, "  &9" + options.getQuestion());
                            for (int k = 0; k < options.getAnswersLength(); k++) {
                                this.plugin.send(recipient, "  &3" + options.getAnswerLabel(k) + " - " + options.getAnswerDescription(k));
                            }
                        }
                    }

                    this.plugin.send(recipient, "&6Cancel interact event mode: &e" + sign.getCancelMode().name());
                    int k;
                    if (!sign.getCommands().isEmpty()) {
                        this.plugin.send(recipient, "&6Commands: ");
                        this.plugin.send(recipient, "&oLine #: &c&oType &a&oDelay &9&oPerms &d&o[ap] &7&oClick &f&oCommand");

                        k = 1;
                        for (ServerSignCommand line : sign.getCommands()) {
                            StringBuilder builder = new StringBuilder();
                            builder.append(k++).append(": &c").append(line.getType().toString().toLowerCase()).append(" ");
                            if (line.getDelay() > 0L) {
                                builder.append("&a").append(TimeUtils.getTimeSpan(line.getDelay() * 1000L, TimeUtils.TimeUnit.SECONDS, TimeUtils.TimeUnit.YEARS, true, false)).append(" ");
                            }
                            if (!line.getGrantPermissions().isEmpty()) {
                                builder.append("&9");
                                for (String perm : line.getGrantPermissions()) {
                                    builder.append(perm).append(" ");
                                }
                            }
                            if (line.isAlwaysPersisted()) {
                                builder.append("&dtrue ");
                            }
                            builder.append("&7").append(line.getInteractValue() == 1 ? "left " : line.getInteractValue() == 0 ? "both " : "right ");
                            builder.append("&f").append(line.getUnformattedCommand());
                            this.plugin.send(recipient, builder.toString().trim());
                        }
                    }

                    if (!SVSMetaManager.getMeta(adminUUID).getValue().asBoolean()) {
                        SVSMetaManager.removeMeta(adminUUID);
                    }
                }

                break;
            case LONG:
                this.plugin.send(recipient, Message.LONG_COMMAND_AGAIN);
                break;

            case LOOP:
                if (sign == null) {
                    return;
                }
                sign.setLoops(meta.getValue().asInt());
                if (meta.getValue().asInt() > -1) sign.setLoopDelay(meta.getValue(1).asInt());
                this.plugin.send(recipient, Message.SET_LOOPS);
                saveRemoveExit = true;
                break;

            case OPTION:
                if (sign == null) {
                    return;
                }
                String optionName = meta.getValue().asString();
                int optionId = meta.getValue(1).asInt();

                if (optionId == 0) {
                    sign.setInputOptionQuestion(optionName, meta.getValue(2).asString());
                } else {
                    if (!sign.containsInputOption(optionName)) {
                        this.plugin.send(recipient, Message.OPTION_CREATE_W_QUESTION);
                        SVSMetaManager.removeMeta(adminUUID);
                        return;
                    }

                    if (optionId == 1) {
                        if (sign.getInputOption(optionName).isValidAnswerLabel(meta.getValue(2).asString())) {
                            this.plugin.send(recipient, Message.OPTION_LABEL_UNIQUE);
                            SVSMetaManager.removeMeta(adminUUID);
                            return;
                        }
                        sign.addInputOptionAnswer(optionName, meta.getValue(2).asString(), meta.getValue(3).asString());
                    } else if (optionId == 2) {
                        sign.removeInputOptionAnswer(optionName, meta.getValue(2).asString());
                    }
                }
                saveRemoveExit = true;
                this.plugin.send(recipient, Message.OPTION_SET);
                break;

            case PERMISSION:
                if (sign == null) {
                    return;
                }
                if (meta.getValue().asObject() == null) {
                    sign.setPermissions(new ArrayList());
                    this.plugin.send(recipient, Message.PERMISSIONS_REMOVED);
                } else {
                    if (!meta.getValue().asStringList().isEmpty()) {
                        sign.setPermissions(meta.getValue().asStringList());
                    }
                    if (meta.hasValue(1)) {
                        sign.setPermissionMessage(meta.getValue(1).asString());
                    }
                    this.plugin.send(recipient, Message.PERMISSION_SET);
                }

                saveRemoveExit = true;
                break;

            case PRICE:
                if (sign == null) {
                    return;
                }
                sign.setPrice(meta.getValue().asDouble());
                this.plugin.send(recipient, Message.PRICE_SET);
                saveRemoveExit = true;
                break;

            case PRICE_ITEM:
                if (sign == null) {
                    return;
                }
                if (meta.getValue().asObject() == null) {
                    sign.clearPriceItems();
                    this.plugin.send(recipient, Message.PRICE_ITEM_REMOVED);
                } else {
                    sign.addPriceItem(meta.getValue().asItemStack());
                    this.plugin.send(recipient, Message.PRICE_ITEM_SUCCESS);
                }

                saveRemoveExit = true;
                break;

            case PRICE_ITEM_CRITERIA:
                if (sign == null) {
                    return;
                }
                sign.setPIC(new ItemSearchCriteria(meta.getValue().asBoolean(), meta.getValue(2).asBoolean(), meta.getValue(1).asBoolean(), meta.getValue(3).asBoolean()));


                this.plugin.send(recipient, Message.PRICE_ITEM_CRITERIA_SET);
                saveRemoveExit = true;
                break;

            case REMOVE:
                if (sign == null) {
                    return;
                }
                int lineNumber = meta.getValue().asInt();
                if (lineNumber < 0) {
                    this.plugin.serverSignsManager.remove(sign);

                    this.plugin.send(recipient, Message.COMMANDS_REMOVED);
                } else {
                    if ((lineNumber > sign.getCommands().size()) || (lineNumber < 1)) {
                        this.plugin.send(recipient, Message.LINE_NOT_FOUND);
                        return;
                    }
                    sign.removeCommand(lineNumber - 1);
                    this.plugin.serverSignsManager.save(sign);
                    this.plugin.send(recipient, Message.COMMAND_REMOVED);
                }

                SVSMetaManager.removeMeta(adminUUID);
                break;

            case SELECT:
                if (sign == null) {
                    return;
                }
                SVSMetaManager.removeMeta(adminUUID);
                SVSMetaManager.setSpecialMeta(adminUUID, new SVSMeta(SVSMetaKey.SELECT, new SVSMetaValue(clicked)));
                this.plugin.send(recipient, Message.SIGN_SELECTED);
                break;

            case SET_COOLDOWN:
                if (sign == null) {
                    return;
                }
                sign.setCooldown(meta.getValue().asLong());
                this.plugin.send(recipient, Message.COOLDOWN_SET);
                saveRemoveExit = true;
                break;

            case SET_GLOBAL_COOLDOWN:
                if (sign == null) {
                    return;
                }
                sign.setGlobalCooldown(meta.getValue().asLong());
                this.plugin.send(recipient, Message.COOLDOWN_SET);
                saveRemoveExit = true;
                break;

            case SILENT:
                if (sign == null) {
                    return;
                }
                sign.setDisplayInternalMessages(meta.getValue().asBoolean());
                this.plugin.send(recipient, Message.SILENT_SUCCESS);
                saveRemoveExit = true;
                break;

            case TIME_LIMIT:
                if (sign == null) {
                    return;
                }

                if (meta.getValue().asLong() >= 0L) {
                    sign.setTimeLimitMinimum(meta.getValue().asLong());
                }
                if (meta.getValue(1).asLong() >= 0L) {
                    sign.setTimeLimitMaximum(meta.getValue(1).asLong());
                }

                this.plugin.send(recipient, Message.TIMELIMIT_SUCCESS);
                saveRemoveExit = true;
                break;

            case USES:
                if (sign == null) {
                    return;
                }
                sign.setUseLimit(meta.getValue().asInt());
                this.plugin.send(recipient, Message.USES_SUCCESS);
                saveRemoveExit = true;
                break;

            case XP:
                if (sign == null) {
                    return;
                }
                int value = meta.getValue().asInt();
                if (value == 0) {
                    this.plugin.send(recipient, Message.XP_COST_REMOVED);
                } else {
                    this.plugin.send(recipient, Message.XP_SET);
                }

                sign.setXP(Integer.valueOf(value));
                saveRemoveExit = true;
                break;
        }


        if (saveRemoveExit) {
            this.plugin.serverSignsManager.save(sign);
            SVSMetaManager.removeMeta(adminUUID);
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\listeners\AdminListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */