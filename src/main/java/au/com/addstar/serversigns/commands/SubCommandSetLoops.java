package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandSetLoops extends SubCommand {
    public SubCommandSetLoops(ServerSignsPlugin plugin) {
        super(plugin, "set_loops", "setloops <loop count> [loop delay (secs)]", "Convert an existing ServerSign to a looped ServerSign (60s by default)", "setloops", "setl", "setloop");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        if (argInt(1, 60) < 1) {
            if (verbose) msg(Message.DELAY_GREATER_THAN_ZERO);
            if (verbose) sendUsage();
            return;
        }

        applyMeta(SVSMetaKey.LOOP, new SVSMetaValue(argInt(0)), new SVSMetaValue(argInt(1, 60)));
        if (verbose) msg(Message.RIGHT_CLICK_BIND_LOOPS);
    }
}


