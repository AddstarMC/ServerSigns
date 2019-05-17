package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

import java.util.Iterator;

public class SubCommandResetAllCooldowns extends SubCommand {
    public SubCommandResetAllCooldowns(ServerSignsPlugin plugin) {
        super(plugin, "reset_all_cooldowns", "resetallcd", "Reset all ServerSigns cooldowns across all signs", "resetallcd", "cdra", "cooldownresetall", "cdresetall", "rcda", "resetcdall", "resetallcooldown");
    }


    public void execute(boolean verbose) {
        Iterator<ServerSign> iterator = this.plugin.serverSignsManager.getSigns().iterator();
        while (iterator.hasNext()) {
            ServerSign sign = iterator.next();
            sign.setLastGlobalUse(0L);
            sign.getLastUse().clear();
            this.plugin.serverSignsManager.save(sign);
        }

        if (verbose) msg(Message.ALL_COOLDOWNS_RESET);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandResetAllCooldowns.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */