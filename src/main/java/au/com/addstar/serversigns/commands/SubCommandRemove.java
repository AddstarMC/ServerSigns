package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandRemove extends SubCommand {
    protected String usage;

    public SubCommandRemove(ServerSignsPlugin plugin) {
        super(plugin, "remove", "remove [line number]", "Remove a ServerSign, or just a specific command", "remove", "rem", "del", "delete");
    }


    public void execute(boolean verbose) {
        applyMeta(SVSMetaKey.REMOVE, new SVSMetaValue(argInt(0, Integer.valueOf(-1))));
        if (verbose) msg(Message.RIGHT_CLICK_REMOVE_COMMAND);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandRemove.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */