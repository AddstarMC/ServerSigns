package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandCreate extends SubCommand {
    public SubCommandCreate(ServerSignsPlugin plugin) {
        super(plugin, "create", "create", "Create a new ServerSign", "create", "cr", "new");
    }


    public void execute(boolean verbose) {
        applyMeta(SVSMetaKey.CREATE, new SVSMetaValue(Integer.valueOf(1)));
        if (verbose) msg(Message.RIGHT_CLICK_CREATE);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandCreate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */