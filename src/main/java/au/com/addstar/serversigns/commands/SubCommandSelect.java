package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandSelect extends SubCommand {
    public SubCommandSelect(ServerSignsPlugin plugin) {
        super(plugin, "select", "select", "Select a ServerSign for future commands to be automatically applied to", "select", "sel");
    }


    public void execute(boolean verbose) {
        if (SVSMetaManager.hasSpecialMeta(this.player.getUniqueId(), SVSMetaKey.SELECT)) {
            SVSMetaManager.removeSpecialMeta(this.player.getUniqueId());
            if (verbose) msg(Message.SIGN_DESELECTED);
            return;
        }

        applyMeta(SVSMetaKey.SELECT, new SVSMetaValue(1));
        if (verbose) msg(Message.RIGHT_CLICK_SELECT);
    }
}


