package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.core.Command;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.commands.core.CommandException;
import au.com.addstar.serversigns.commands.core.SubCommand;

import java.util.UUID;

public class CommandServerSigns extends Command {
    public CommandServerSigns(ServerSignsPlugin instance) {
        super("serversigns", instance);

        addSubCommand(new SubCommandAdd(this.plugin));
        addSubCommand(new SubCommandCancel(this.plugin));
        addSubCommand(new SubCommandCancelPermission(this.plugin));
        addSubCommand(new SubCommandConfirmation(this.plugin));
        addSubCommand(new SubCommandCopy(this.plugin));

        addSubCommand(new SubCommandCreate(this.plugin));
        addSubCommand(new SubCommandDev(this.plugin));
        addSubCommand(new SubCommandEdit(this.plugin));
        addSubCommand(new SubCommandHeldItemCriteria(this.plugin));
        addSubCommand(new SubCommandHolding(this.plugin));

        addSubCommand(new SubCommandImport(this.plugin));
        addSubCommand(new SubCommandInsert(this.plugin));
        addSubCommand(new SubCommandList(this.plugin));
        addSubCommand(new SubCommandLong(this.plugin));
        addSubCommand(new SubCommandGrantPermission(this.plugin));

        addSubCommand(new SubCommandOption(this.plugin));
        addSubCommand(new SubCommandPriceItem(this.plugin));
        addSubCommand(new SubCommandPriceItemCriteria(this.plugin));
        addSubCommand(new SubCommandReload(this.plugin));
        addSubCommand(new SubCommandReloadConfig(this.plugin));

        addSubCommand(new SubCommandReloadSigns(this.plugin));
        addSubCommand(new SubCommandRemove(this.plugin));
        addSubCommand(new SubCommandResetAllCooldowns(this.plugin));
        addSubCommand(new SubCommandResetCooldowns(this.plugin));
        addSubCommand(new SubCommandResetCooldown(this.plugin));

        addSubCommand(new SubCommandSelect(this.plugin));
        addSubCommand(new SubCommandSetCooldown(this.plugin));
        addSubCommand(new SubCommandSetGlobalCooldown(this.plugin));
        addSubCommand(new SubCommandSetLoops(this.plugin));
        addSubCommand(new SubCommandSetPermission(this.plugin));

        addSubCommand(new SubCommandSetPrice(this.plugin));
        addSubCommand(new SubCommandSetUses(this.plugin));
        addSubCommand(new SubCommandSilent(this.plugin));
        addSubCommand(new SubCommandTimelimit(this.plugin));
        addSubCommand(new SubCommandVoid(this.plugin));

        addSubCommand(new SubCommandXP(this.plugin));
    }

    protected void perform(String commandLabel, org.bukkit.command.Command cmd) throws Exception {
        if (!argSet(0)) {
            sendHelpMessages();
            msg("&7&oParameters: &2<required> &c{exact} &9[optional]");
            msg("&7Detailed reference: http://serversigns.de/cmds");
            return;
        }
        String arg0 = arg(0);

        if (arg0.equalsIgnoreCase("yes")) {
            if (this.isConsole) throw new CommandException("This command cannot be performed from console");
            if (SVSMetaManager.hasMeta(this.player)) {
                SVSMeta meta = SVSMetaManager.getMeta(this.player);
                if (meta.getKey().equals(SVSMetaKey.YES)) {
                    ServerSign sign = meta.getValue().asServerSign();
                    this.plugin.serverSignExecutor.executeSignFull(this.player, sign, null);
                    SVSMetaManager.removeMeta(this.player);
                }
            }
            return;
        }

        SubCommand match = matchSubCommand(arg0);
        if (match != null) {
            if (this.isConsole) {
                ServerSignsPlugin.log("WARNING: /svs commands are not designed for Console use - errors may occur!");
            } else if (!match.hasPermission(this.player)) {
                sendHelpMessages();
                return;
            }

            match.execute(this.sender, this.args.subList(1, this.args.size()), commandLabel, true);

            UUID id = (this.player == null) ? SVSMetaManager.CONSOLE_UUID : this.player.getUniqueId();
            if (((SVSMetaManager.hasSpecialMeta(id, SVSMetaKey.SELECT)) && (SVSMetaManager.hasMeta(id)))) {
                this.plugin.adminListener.handleAdminInteract(SVSMetaManager.getSpecialMeta(id).getValue().asLocation(), this.sender, id);
            }
            return;
        }

        sendHelpMessages();
        msg("&7&oParameters: &2<required> &c{exact} &9[optional]");
        msg("&7Detailed reference: http://serversigns.de/cmds");
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\CommandServerSigns.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */