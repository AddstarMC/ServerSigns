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
        applyMeta(SVSMetaKey.REMOVE, new SVSMetaValue(argInt(0, -1)));
        if (verbose) msg(Message.RIGHT_CLICK_REMOVE_COMMAND);
    }
}


