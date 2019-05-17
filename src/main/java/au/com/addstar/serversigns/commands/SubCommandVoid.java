package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandVoid extends SubCommand {
    public SubCommandVoid(ServerSignsPlugin plugin) {
        super(plugin, "void", "void", "Invalidates any pending actions you may have", "void");
    }


    public void execute(boolean verbose) {
        java.util.UUID id = this.player == null ? SVSMetaManager.CONSOLE_UUID : this.player.getUniqueId();
        if (SVSMetaManager.hasMeta(id)) {
            SVSMetaManager.removeMeta(id);
            SVSMetaManager.removeSpecialMeta(id);
            msg("Success!");
        } else {
            sendUsage();
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandVoid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */