package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.*;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandList extends SubCommand {
    public SubCommandList(ServerSignsPlugin plugin) {
        super(plugin, "list", "list [persist]", "List all information available for an existing ServerSign", "list", "ls", "info");
    }


    public void execute(boolean verbose) {
        if ((argSet(0)) && (!arg(0).equalsIgnoreCase("true")) && (!arg(0).equalsIgnoreCase("false"))) {
            if (verbose) sendUsage();
            return;
        }

        if ((argSet(0)) && (arg(0).equalsIgnoreCase("false")) &&
                (SVSMetaManager.hasMeta(this.player))) {
            SVSMeta meta = SVSMetaManager.getMeta(this.player);
            if (meta.getValue().asBoolean()) {
                SVSMetaManager.removeMeta(this.player);
                if (verbose) msg(Message.LIST_PERSIST_OFF);
                return;
            }
        }


        applyMeta(SVSMetaKey.LIST, new SVSMetaValue(argBool(0, false)));
        if ((argSet(0)) && (argBool(0, false)) && (verbose)) msg(Message.LIST_PERSIST_ON);
        if (verbose) msg(Message.RIGHT_CLICK_VIEW_LIST);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */