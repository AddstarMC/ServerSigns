package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandSetUses extends SubCommand {
    public SubCommandSetUses(ServerSignsPlugin plugin) {
        super(plugin, "set_uses", "setuses {0|<number of uses>}", "Limit this ServerSign to X number of uses", "setuses", "setuse", "uses");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        int uses = argInt(0, -1);
        if (uses < 0) {
            if (verbose) msg(Message.USES_GREATER_ZERO);
            return;
        }

        applyMeta(SVSMetaKey.USES, new SVSMetaValue(uses));

        if (verbose) {
            if (uses == 0) msg(Message.RIGHT_CLICK_REMOVE_USES);
            else {
                msg(Message.RIGHT_CLICK_SET_USES);
            }
        }
    }
}


