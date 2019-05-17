package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandDev extends SubCommand {
    public SubCommandDev(ServerSignsPlugin plugin) {
        super(plugin, "", "dev <params>", "This command is intended for advanced users only; no documentation is provided.", "dev");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        if (argStr(0).equalsIgnoreCase("total")) {
            this.sender.sendMessage("Total number of ServerSigns loaded: " + this.plugin.serverSignsManager.getSigns().size());
            return;
        }

        if (argStr(0).equalsIgnoreCase("persist_version")) {
            this.sender.sendMessage("Current persistence version: 7");
            return;
        }

        if (argStr(0).equalsIgnoreCase("hooks")) {
            this.sender.sendMessage(String.format("Vault: %b, NCP: %b, Ess: %b", Boolean.valueOf(this.plugin.hookManager.vault.isHooked()), Boolean.valueOf(this.plugin.hookManager.noCheatPlus.isHooked()), Boolean.valueOf(this.plugin.hookManager.essentials.isHooked())));
            return;
        }

        if (verbose) sendUsage();
    }

    public boolean hasPermission(org.bukkit.entity.Player player) {
        return player.isOp();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandDev.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */