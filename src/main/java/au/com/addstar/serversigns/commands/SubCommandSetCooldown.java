package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.utils.TimeUtils;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandSetCooldown extends SubCommand {
    public SubCommandSetCooldown(ServerSignsPlugin plugin) {
        super(plugin, "set_cooldown", "setcooldown <cooldown>{s|m|h|d|w|mo}", "Set the per-player cooldown time for an existing ServerSign", "scd", "setcooldown");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        long cooldown = TimeUtils.getLengthFromString(arg(0));
        if ((cooldown == 0L) && (!arg(0).equals("0"))) cooldown = -1L;
        if (cooldown < 0L) {
            if (verbose) msg(Message.NO_NUMBER);
            return;
        }

        applyMeta(SVSMetaKey.SET_COOLDOWN, new SVSMetaValue(cooldown / 1000L));
        if (verbose) msg(Message.RIGHT_CLICK_SET_COOLDOWN);
    }
}


