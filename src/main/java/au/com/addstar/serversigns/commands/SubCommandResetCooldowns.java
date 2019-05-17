package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.OfflinePlayer;

public class SubCommandResetCooldowns extends SubCommand {
    public SubCommandResetCooldowns(ServerSignsPlugin plugin) {
        super(plugin, "resetcooldowns", "resetcooldowns <player>", "Reset all ServerSigns cooldowns for <player>", "resetcooldowns", "resetcds", "rcds");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        OfflinePlayer offline = org.bukkit.Bukkit.getOfflinePlayer(argStr(0));
        if (offline == null) {
            if (verbose) msg(Message.PLAYER_NOT_FOUND, "<string>", argStr(0));
            return;
        }

        Iterator<ServerSign> it = this.plugin.serverSignsManager.getSigns().iterator();
        HashSet<ServerSign> toSave = new HashSet();
        while (it.hasNext()) {
            ServerSign sign = it.next();

            if (sign.getLastUse(offline.getUniqueId()) > 0L) {
                sign.removeLastUse(offline.getUniqueId());
                toSave.add(sign);
            }
        }

        if (!toSave.isEmpty()) {
            for (ServerSign sign : toSave) {
                this.plugin.serverSignsManager.save(sign);
            }
        }

        if (verbose) msg(Message.COOLDOWNS_RESET, "<string>", argStr(0));
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandResetCooldowns.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */