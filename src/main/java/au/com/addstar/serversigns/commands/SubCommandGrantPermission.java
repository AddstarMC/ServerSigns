package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.*;
import au.com.addstar.serversigns.commands.core.SubCommand;
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

        SVSMeta meta;
        if (arg(0).equalsIgnoreCase("delete")) {
            meta = new SVSMeta(SVSMetaKey.GRANT, new SVSMetaValue(Boolean.FALSE));
            if (verbose) msg(Message.RIGHT_CLICK_DEL_PERMISSION);
        } else if (arg(0).equalsIgnoreCase("add")) {
            if (!argSet(1)) {
                if (verbose) sendUsage();
                return;
            }
            meta = new SVSMeta(SVSMetaKey.GRANT, new SVSMetaValue(Boolean.TRUE), new SVSMetaValue(arg(1)));
            if (verbose) msg(Message.RIGHT_CLICK_BIND_PERMISSION);
        } else {
            if (verbose) sendUsage();
            return;
        }
        SVSMetaManager.setMeta(this.player, meta);
    }
}


