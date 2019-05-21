package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ExecutableSVSR {
    protected ArrayList<SubCommand> subCommands = new ArrayList<>();
    private ServerSignsPlugin plugin;

    public ExecutableSVSR(ServerSignsPlugin instance) {
        this.plugin = instance;

        addSubCommand(new SubCommandAdd(this.plugin));
        addSubCommand(new SubCommandCancel(this.plugin));
        addSubCommand(new SubCommandCancelPermission(this.plugin));
        addSubCommand(new SubCommandConfirmation(this.plugin));
        addSubCommand(new SubCommandCreate(this.plugin));

        addSubCommand(new SubCommandEdit(this.plugin));
        addSubCommand(new SubCommandHeldItemCriteria(this.plugin));
        addSubCommand(new SubCommandHolding(this.plugin));
        addSubCommand(new SubCommandInsert(this.plugin));
        addSubCommand(new SubCommandList(this.plugin));

        addSubCommand(new SubCommandGrantPermission(this.plugin));
        addSubCommand(new SubCommandOption(this.plugin));
        addSubCommand(new SubCommandPriceItem(this.plugin));
        addSubCommand(new SubCommandPriceItemCriteria(this.plugin));
        addSubCommand(new SubCommandRemove(this.plugin));

        addSubCommand(new SubCommandResetCooldowns(this.plugin));
        addSubCommand(new SubCommandResetCooldown(this.plugin));
        addSubCommand(new SubCommandSetCooldown(this.plugin));
        addSubCommand(new SubCommandSetGlobalCooldown(this.plugin));
        addSubCommand(new SubCommandSetLoops(this.plugin));

        addSubCommand(new SubCommandSetPermission(this.plugin));
        addSubCommand(new SubCommandSetPrice(this.plugin));
        addSubCommand(new SubCommandSetUses(this.plugin));
        addSubCommand(new SubCommandSilent(this.plugin));
        addSubCommand(new SubCommandTimelimit(this.plugin));

        addSubCommand(new SubCommandXP(this.plugin));
    }

    public void addSubCommand(SubCommand command) {
        this.subCommands.add(command);
    }

    public SubCommand matchSubCommand(String label) {
        label = label.toLowerCase();
        for (SubCommand sub : this.subCommands) {
            if (sub.getAliases().contains(label)) {
                return sub;
            }
        }
        return null;
    }

    public void execute(Location signLocation, Player player, String command) throws Exception {
        List<String> parts = Arrays.asList(command.split(" "));

        SubCommand match = matchSubCommand(parts.get(0));
        if (match != null) {
            plugin.debug("Command Match :" +match.toString());
            match.execute(player, parts.subList(1, parts.size()), "", false);
            plugin.debug("Execute Complete for Match :" +match.toString());
            UUID id = (player == null) ? SVSMetaManager.CONSOLE_UUID : player.getUniqueId();
            if (SVSMetaManager.hasMeta(id)) {
                this.plugin.adminListener.handleAdminInteract(signLocation, player, id);
            }
        } else {
            this.plugin.send(player, Message.UNABLE_TO_EXECUTE_CMD, "<string>", command);
        }
    }
}


