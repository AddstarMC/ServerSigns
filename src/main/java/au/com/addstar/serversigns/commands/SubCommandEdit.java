package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;

public class SubCommandEdit extends SubCommand {
    public SubCommandEdit(ServerSignsPlugin plugin) {
        super(plugin, "edit", "edit <line number> <new command>", "Edit a command on an existing ServerSign", "edit", "ed");
    }


    public void execute(boolean verbose) {
        if (!argSet(1)) {
            if (verbose) sendUsage();
            return;
        }

        int line = argInt(0, -1);
        if (line < 1) {
            sendUsage();
            return;
        }

        String command = loopArgs(1);
        if (CommandUtils.isCommandUnsafe(command, this.plugin, this.sender)) {
            return;
        }

        CommandUtils.applyServerSignCommandMeta(command, this.plugin, this.sender, verbose, SVSMetaKey.EDIT, new SVSMetaValue(line));
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandEdit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */