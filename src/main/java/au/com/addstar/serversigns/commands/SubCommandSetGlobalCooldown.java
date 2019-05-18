package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.utils.TimeUtils;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandSetGlobalCooldown extends SubCommand {
    public SubCommandSetGlobalCooldown(ServerSignsPlugin plugin) {
        super(plugin, "set_global_cooldown", "setglobalcooldown <cooldown>{s|m|h|d|w|mo}", "Set the global cooldown time for an existing ServerSign", "sgcd", "setglobalcooldown");
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

        applyMeta(SVSMetaKey.SET_GLOBAL_COOLDOWN, new SVSMetaValue(cooldown / 1000L));
        if (verbose) msg(Message.RIGHT_CLICK_SET_COOLDOWN);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandSetGlobalCooldown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */