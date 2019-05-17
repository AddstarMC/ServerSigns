package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.*;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandGrantPermission extends SubCommand {
    public SubCommandGrantPermission(ServerSignsPlugin plugin) {
        super(plugin, "grant", "grant {delete | add} [permission]", "Add or remove a granted permission to/from an existing ServerSign", "permissiongrant", "permissionsgrant", "pgrant", "grantpermission", "grantpermissions", "grant");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }


        if (arg(0).equalsIgnoreCase("delete")) {
            SVSMeta meta = new SVSMeta(SVSMetaKey.GRANT, new SVSMetaValue(Boolean.valueOf(false)));
            if (verbose) msg(Message.RIGHT_CLICK_DEL_PERMISSION);
        } else if (arg(0).equalsIgnoreCase("add")) {
            if (!argSet(1)) {
                if (verbose) sendUsage();
                return;
            }

            SVSMeta meta = new SVSMeta(SVSMetaKey.GRANT, new SVSMetaValue(Boolean.valueOf(true)), new SVSMetaValue(arg(1)));
            if (verbose) msg(Message.RIGHT_CLICK_BIND_PERMISSION);
        } else {
            if (verbose) sendUsage();
            return;
        }
        SVSMeta meta;
        SVSMetaManager.setMeta(this.player, meta);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandGrantPermission.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */