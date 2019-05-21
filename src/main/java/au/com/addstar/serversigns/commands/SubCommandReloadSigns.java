package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandReloadSigns extends SubCommand {
    public SubCommandReloadSigns(ServerSignsPlugin plugin) {
        super(plugin, "reload_signs", "reloadSigns", "Reload all ServerSigns from disk", "reloadsigns", "reloadsign", "reloads", "rls", "rlsigns");
    }


    public void execute(boolean verbose) {
        if (verbose) {
            if (this.plugin.serverSignsManager.populateSignsMap(this.plugin.serverSignsManager.prepareServerSignsSet())) {
                msg(Message.RELOAD_SUCCESS);
            } else {
                msg(Message.RELOAD_SIGNS_FAIL);
            }
        }
    }
}


