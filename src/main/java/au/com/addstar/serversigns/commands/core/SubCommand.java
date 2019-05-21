package au.com.addstar.serversigns.commands.core;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.meta.SVSMetaValue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand extends AbstractCommand {
    protected ServerSignsPlugin plugin;
    protected String commandParams;
    protected String commandDescription;
    protected String permission;
    protected List<String> aliases;

    public SubCommand(ServerSignsPlugin plugin, String permission, String commandParams, String commandDescription, String... aliases) {
        super(plugin);
        this.plugin = plugin;
        this.commandParams = commandParams;
        this.commandDescription = commandDescription;
        this.permission = permission;
        this.aliases = Arrays.asList(aliases);
    }

    public void execute(CommandSender sender, List<String> args, String label, boolean verbose) {
        if ((sender instanceof Player)) {
            this.player = ((Player) sender);
        }
        this.sender = sender;
        this.args = args;
        this.label = label;
        plugin.debug("SubCommand Excuting: " +this.getClass().getSimpleName() +
                " :" +label + " "+ args.toString());
        execute(verbose);
    }


    protected abstract void execute(boolean paramBoolean);

    public String getParameters() {
        return this.commandParams;
    }

    public String getDescription() {
        return this.commandDescription;
    }

    protected void sendUsage() {
        msg("/" + getLastLabel() + " " + getParameters() + " - " + getDescription());
    }


    public String getPermission() {
        return this.permission;
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission("serversigns.command." + this.permission);
    }


    public List<String> getAliases() {
        return this.aliases;
    }


    protected void applyMeta(SVSMetaKey key, SVSMetaValue... values) {
        SVSMetaManager.setMeta(this.player == null ? SVSMetaManager.CONSOLE_UUID : this.player.getUniqueId(), new SVSMeta(key, values));
    }

    protected SVSMetaValue[] getMetaValues(SVSMetaKey key) {
        UUID id = this.player == null ? SVSMetaManager.CONSOLE_UUID : this.player.getUniqueId();
        if (SVSMetaManager.hasMeta(id)) {
            SVSMeta meta = SVSMetaManager.getMeta(id);

            if (meta.getKey().equals(key)) {
                return meta.getValues();
            }
        }

        return null;
    }
}


