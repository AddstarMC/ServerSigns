package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandInsert extends SubCommand {
    public SubCommandInsert(ServerSignsPlugin plugin) {
        super(plugin, "insert", "insert <line number> <command>", "Insert a new command at the given index on an existing ServerSign", "insert", "ins");
    }


    public void execute(boolean verbose) {
        if (!argSet(1)) {
            if (verbose) sendUsage();
            return;
        }

        int line = argInt(0, -1);
        if (line < 1) {
            if (verbose) sendUsage();
            return;
        }

        String command = loopArgs(1);
        if (!CommandUtils.isCommandSafe(command, this.plugin, this.sender)) {
            return;
        }

        CommandUtils.applyServerSignCommandMeta(command, this.plugin, this.sender, verbose, SVSMetaKey.INSERT, new SVSMetaValue(line));
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandInsert.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */