package au.com.addstar.serversigns.commands.core;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.SubCommandDev;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command extends AbstractCommand {
    protected final String name;
    protected ArrayList<SubCommand> subCommands = new ArrayList();

    protected String label;
    protected boolean isConsole;

    protected Command(String name, ServerSignsPlugin plugin) {
        super(plugin);
        this.name = name;
    }

    public void setPlugin(ServerSignsPlugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return this.name;
    }

    public void addSubCommand(SubCommand command) {
        this.subCommands.add(command);
    }

    public void removeSubCommand(SubCommand command) {
        this.subCommands.remove(command);
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

    protected void sendHelpMessages() {
        for (SubCommand sub : this.subCommands) {
            if (((this.isConsole) || (sub.hasPermission(this.player))) &&
                    (!(sub instanceof SubCommandDev))) {
                msg("/" + this.label + " " + sub.getParameters());
            }
        }
    }

    public void run(Server server, CommandSender sender, String commandLabel, org.bukkit.command.Command cmd, String[] args) throws Exception {
        this.sender = sender;
        this.label = commandLabel;
        this.isConsole = (!(sender instanceof Player));
        this.player = (this.isConsole ? null : (Player) sender);
        this.args = Arrays.asList(args);

        perform(commandLabel, cmd);
    }

    protected abstract void perform(String paramString, org.bukkit.command.Command paramCommand)
            throws Exception;
}
