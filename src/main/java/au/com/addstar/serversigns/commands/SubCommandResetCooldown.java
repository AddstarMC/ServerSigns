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
        applyMeta(SVSMetaKey.COOLDOWN_RESET, new SVSMetaValue(Boolean.TRUE));
        if (verbose) msg(Message.RIGHT_CLICK_RESET_COOLDOWN);
    }
}


