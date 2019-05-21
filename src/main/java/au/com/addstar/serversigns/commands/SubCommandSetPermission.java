package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

import java.util.ArrayList;
import java.util.List;

public class SubCommandSetPermission extends SubCommand {
    public SubCommandSetPermission(ServerSignsPlugin plugin) {
        super(plugin, "set_permission", "setperms <perm|-> [perm]... [{m:}message]", "For each permission, the user must have 'serversigns.use.<permission>' to use this sign", "sp", "setpermission", "setpermissions", "setperms");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        if (arg(0).equals("-")) {
            SVSMetaManager.setMeta(this.player, new SVSMeta(SVSMetaKey.PERMISSION, new SVSMetaValue(null)));
            if (verbose) msg(Message.RIGHT_CLICK_DEL_PERMISSION);
            return;
        }

        StringBuilder message = new StringBuilder();
        List<String> perms =  new ArrayList<>();
        boolean b = false;
        for (String arg : this.args) {
            if ((arg.toLowerCase().contains("serversigns.use")) &&
                    (verbose)) {
                msg("NOTE: permissions will automatically be prefixed with 'serversigns.use.' - you do not need to add it yourself!");
            }
            if (arg.startsWith("m:")) {
                message.append(arg.substring(2));
                b = true;
            } else if (b) {
                message.append(" ").append(arg);
            } else {
                perms.add(arg);
            }
        }

        if ((perms.isEmpty()) && (message.length() > 0)) {
            if (verbose) sendUsage();
            return;
        }

        SVSMeta meta = new SVSMeta(SVSMetaKey.PERMISSION, new SVSMetaValue(perms));
        if (message.length() > 0) {
            meta.addValue(new SVSMetaValue(message.toString()));
        }

        SVSMetaManager.setMeta(this.player, meta);
        if (verbose) msg(Message.RIGHT_CLICK_BIND_PERMISSION);
    }
}


