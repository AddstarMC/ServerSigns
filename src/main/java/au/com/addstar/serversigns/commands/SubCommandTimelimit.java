package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.utils.TimeUtils;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandTimelimit extends SubCommand {
    public SubCommandTimelimit(ServerSignsPlugin plugin) {
        super(plugin, "timelimit", "timelimit {<minimum>|@|-} [maximum]", "Set the minimum & maximum date/time between which a ServerSign can be used", "timelimit", "timel", "tl");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        long min = -1L;
        long max = -1L;

        switch (arg(0)) {
            case "@":
                if (!argSet(1)) {
                    sendUsage();
                }
                break;
            case "-":
                min = 0L;
                max = 0L;
                break;
            case "0":
                min = 0L;
                break;
            default:
                min = TimeUtils.convertDSDDFToEpochMillis(arg(0), this.plugin.config.getTimeZone());
                if (min == 0L) {
                    msg(Message.TIMELIMIT_INVALID);
                    return;
                }
                break;
        }

        if (argSet(1)) {
            if (arg(1).equals("0")) {
                max = 0L;
            } else {
                max = TimeUtils.convertDSDDFToEpochMillis(arg(1), this.plugin.config.getTimeZone());
                if (max == 0L) {
                    msg(Message.TIMELIMIT_INVALID);
                    return;
                }
            }
        }

        msg(Message.RIGHT_CLICK_APPLY);
        applyMeta(SVSMetaKey.TIME_LIMIT, new SVSMetaValue(min), new SVSMetaValue(max));
    }
}


