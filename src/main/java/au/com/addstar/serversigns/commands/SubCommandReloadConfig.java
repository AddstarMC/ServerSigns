package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.config.ConfigLoadingException;
import au.com.addstar.serversigns.translations.NoDefaultException;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandReloadConfig extends SubCommand {
    public SubCommandReloadConfig(ServerSignsPlugin plugin) {
        super(plugin, "reload_config", "reloadConfig", "Reload the config.yml & translations files from disk", "reloadconfig", "reloadconf", "reloadc", "rlc", "rlconf");
    }


    public void execute(boolean verbose) {
        try {
            this.plugin.loadConfig(this.plugin.getDataFolder().toPath());
            if (verbose) msg(Message.RELOAD_CONFIG_SUCCESS);
        } catch (NoDefaultException | ConfigLoadingException ex) {
            if (verbose) msg(Message.RELOAD_CONFIG_FAIL);
            org.bukkit.Bukkit.getPluginManager().disablePlugin(this.plugin);
        }
    }
}


