package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.*;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandLong extends SubCommand {
    public SubCommandLong(ServerSignsPlugin plugin) {
        super(plugin, "long", "long", "Toggle 'long' mode, which allows long commands to be applied to ServerSigns", "long", "longcommand", "longcmd");
    }


    public void execute(boolean verbose) {
        if (argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        SVSMetaValue[] values = getMetaValues(SVSMetaKey.LONG);
        if (values != null) {
            if (values.length <= 1) {
                SVSMetaManager.removeMeta(this.player);
                if (verbose) msg(Message.LONG_CANCELLED);
                return;
            }


            String command = "";
            for (SVSMetaValue val : values) {
                command = command + " " + val.asString();
            }
            command = command.trim();
            if (CommandUtils.isCommandUnsafe(command, this.plugin, this.sender)) {
                return;
            }

            CommandUtils.applyServerSignCommandMeta(command, this.plugin, this.sender, verbose, SVSMetaKey.ADD);
            return;
        }

        applyMeta(SVSMetaKey.LONG, new SVSMetaValue(""));
        if (verbose) msg(Message.LONG_TYPE_TO_CHAT);
    }
}


