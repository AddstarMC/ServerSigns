package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandResetCooldown extends SubCommand {
    public SubCommandResetCooldown(ServerSignsPlugin plugin) {
        super(plugin, "cooldown_reset", "resetcd", "Reset the cooldown data for a ServerSign", "cdr", "cooldownreset", "cdreset", "resetcooldown", "rcd", "resetcd");
    }


    public void execute(boolean verbose) {
        applyMeta(SVSMetaKey.COOLDOWN_RESET, new SVSMetaValue(Boolean.valueOf(true)));
        if (verbose) msg(Message.RIGHT_CLICK_RESET_COOLDOWN);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandResetCooldown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */