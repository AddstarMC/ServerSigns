package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaManager;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.parsing.CommandParseException;
import au.com.addstar.serversigns.parsing.ServerSignCommandFactory;
import au.com.addstar.serversigns.parsing.command.ServerSignCommand;
import au.com.addstar.serversigns.translations.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtils {
    public static boolean isCommandUnsafe(String command, ServerSignsPlugin plugin, CommandSender executor) {
        String rawCmd = command.trim();
        if ((rawCmd.startsWith("*")) || (rawCmd.startsWith("/")))
            rawCmd = rawCmd.substring(1);
        if (rawCmd.contains(" ")) {
            rawCmd = rawCmd.split(" ")[0];
        }
        if (plugin.config.getBlockedCommands().contains(rawCmd.toLowerCase())) {
            plugin.send(executor, Message.BLOCKED_COMMAND);
            return true;
        }

        return false;
    }

    public static void applyServerSignCommandMeta(String command, ServerSignsPlugin plugin, CommandSender executor, boolean verbose, SVSMetaKey key, SVSMetaValue... precursingValues) {
        try {
            ServerSignCommand cmd = ServerSignCommandFactory.getCommandFromString(command, plugin);
            if (cmd == null) {
                throw new CommandParseException("Unidentified error");
            }
            SVSMetaValue[] values = java.util.Arrays.copyOf(precursingValues, precursingValues.length + 1);
            values[(values.length - 1)] = new SVSMetaValue(cmd);

            SVSMetaManager.setMeta(
                    (executor instanceof Player) ? ((Player) executor).getUniqueId() : SVSMetaManager.CONSOLE_UUID, new SVSMeta(key, values));


            if (verbose) plugin.send(executor, Message.RIGHT_CLICK_BIND_CMD);
        } catch (CommandParseException ex) {
            if (verbose) plugin.send(executor, plugin.msgHandler.get(Message.INVALID_COMMAND) + " " + ex.getMessage());
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\CommandUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */