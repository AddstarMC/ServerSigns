package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.core.Command;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.utils.StringUtils;
import au.com.addstar.serversigns.commands.core.CommandException;
import au.com.addstar.serversigns.commands.core.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class CommandServerSignsRemote extends Command {
    public CommandServerSignsRemote(ServerSignsPlugin instance) {
        super("serversignsremote", instance);

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

    protected void perform(String commandLabel, org.bukkit.command.Command cmd) throws Exception {
        this.label += " <location>";
        if (!argSet(1)) {
            sendHelpMessages();
            msg("&7<location> must be in the format 'world,x,y,z'");
            msg("&7&oParameters: &2<required> &c{exact} &9[optional]");
            msg("&7Detailed reference: http://serversigns.de/cmds");
            return;
        }


        String rawLoc = arg(0);
        Location remoteLocation;
        try {
            if (StringUtils.count(rawLoc, ',') >= 3) {
                String[] split = rawLoc.split(",");
                String worldName = "";
                double[] coords = new double[3];
                int i = 2;

                for (int k = split.length - 1; k >= 0; k--) {
                    if (i >= 0) {
                        coords[(i--)] = Double.parseDouble(split[k]);
                    } else {
                        worldName = StringUtils.join(split, ",", 0, k + 1);
                        break;
                    }
                }

                org.bukkit.World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    throw new CommandException("");
                }
                remoteLocation = new Location(world, coords[0], coords[1], coords[2]);
            } else {
                throw new CommandException("");
            }
        } catch (NumberFormatException | CommandException ex) {
            msg("Invalid remote location provided. Format is world,x,y,z");
            return;
        }

        SubCommand match = matchSubCommand(arg(1));
        if (match != null) {
            if ((!this.isConsole) && (!match.hasPermission(this.player))) {
                sendHelpMessages();
                return;
            }

            match.execute(this.sender, this.args.subList(2, this.args.size()), this.label, false);

            UUID id = this.player == null ? SVSMetaManager.CONSOLE_UUID : this.player.getUniqueId();
            if (SVSMetaManager.hasMeta(id)) {
                this.plugin.adminListener.handleAdminInteract(remoteLocation, this.sender, id);
            }
            return;
        }

        sendHelpMessages();
        msg("&7<location> must be in the format 'world,x,y,z'");
        msg("&7&oParameters: &2<required> &c{exact} &9[optional]");
        msg("&7Detailed reference: http://serversigns.de/cmds");
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\CommandServerSignsRemote.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */