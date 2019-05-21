package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.commands.core.SubCommand;

import java.util.UUID;

public class SubCommandVoid extends SubCommand {
    public SubCommandVoid(ServerSignsPlugin plugin) {
        super(plugin, "void", "void", "Invalidates any pending actions you may have", "void");
    }


    public void execute(boolean verbose) {
        UUID id = this.player == null ? SVSMetaManager.CONSOLE_UUID : this.player.getUniqueId();
        if (SVSMetaManager.hasMeta(id)) {
            SVSMetaManager.removeMeta(id);
            SVSMetaManager.removeSpecialMeta(id);
            msg("Success!");
        } else {
            sendUsage();
        }
    }
}


