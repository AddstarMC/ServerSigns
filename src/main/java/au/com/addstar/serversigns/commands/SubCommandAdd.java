package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandAdd extends SubCommand {
    public SubCommandAdd(ServerSignsPlugin plugin) {
        super(plugin, "add", "add [type] [params] <command>", "Bind a command to a new or existing ServerSign", "add", "a");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        String command = loopArgs(0);
        if (CommandUtils.isCommandUnsafe(command, this.plugin, this.sender)) {
            return;
        }

        CommandUtils.applyServerSignCommandMeta(command, this.plugin, this.sender, verbose, SVSMetaKey.ADD);
    }
}


