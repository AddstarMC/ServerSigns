package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandXP extends SubCommand {
    public SubCommandXP(ServerSignsPlugin plugin) {
        super(plugin, "xp", "xp <levels>", "Set an experience levels cost on an existing ServerSign", "xp", "xpprice", "exp");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        int levels = argInt(0, -1);
        if (levels < 0) {
            if (verbose) msg(Message.XP_COST_INVALID);
            return;
        }

        applyMeta(SVSMetaKey.XP, new SVSMetaValue(levels));
        if (verbose) msg(Message.XP_COST_BIND);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandXP.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */