package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.*;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandCancelPermission extends SubCommand {
    public SubCommandCancelPermission(ServerSignsPlugin plugin) {
        super(plugin, "cancel_permission", "cancelpermission <permission> [message]", "Users with this permission will not be able to execute the ServerSign", "cancelpermission", "canpermission", "canperm", "cancelperm", "cperm", "cpermission");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        SVSMeta meta = new SVSMeta(SVSMetaKey.CANCEL_PERMISSION, new SVSMetaValue(arg(0)));
        if (argSet(1)) {
            meta.addValue(new SVSMetaValue(loopArgs(1)));
        }

        SVSMetaManager.setMeta(this.player, meta);
        if (verbose) msg(Message.RIGHT_CLICK_BIND_CANCEL_PERMISSION);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandCancelPermission.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */