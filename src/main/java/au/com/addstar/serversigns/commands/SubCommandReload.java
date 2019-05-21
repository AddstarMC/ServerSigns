package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.config.ConfigLoadingException;
import au.com.addstar.serversigns.translations.NoDefaultException;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandReload extends SubCommand {
    public SubCommandReload(ServerSignsPlugin plugin) {
        super(plugin, "reload", "reload", "Reload config.yml, translation files, and all ServerSigns from disk", "reload", "rl");
    }


    public void execute(boolean verbose) {
        try {
            this.plugin.loadConfig(this.plugin.getDataFolder().toPath());
        } catch (NoDefaultException | ConfigLoadingException ex) {
            if (verbose) msg(Message.RELOAD_CONFIG_FAIL);
            org.bukkit.Bukkit.getPluginManager().disablePlugin(this.plugin);
            return;
        }

        if (verbose) {
            if (!this.plugin.serverSignsManager.populateSignsMap(this.plugin.serverSignsManager.prepareServerSignsSet())) {
                msg(Message.RELOAD_SIGNS_FAIL);
            }
            msg(Message.RELOAD_SUCCESS);
        }
    }
}


