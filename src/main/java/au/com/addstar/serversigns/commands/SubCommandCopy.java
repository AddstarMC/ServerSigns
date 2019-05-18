package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.*;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandCopy extends SubCommand {
    public SubCommandCopy(ServerSignsPlugin plugin) {
        super(plugin, "copy", "copy [persist]", "Copy and paste a ServerSign", "copy", "cp");
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
                if (verbose) msg(Message.PERSISTENCE_OFF);
                return;
            }
        }


        applyMeta(SVSMetaKey.COPY, new SVSMetaValue(argSet(0) && argBool(0).booleanValue()));
        if ((argSet(0)) && (argBool(0)) && (verbose)) msg(Message.PERSISTENCE_ON);
        if (verbose) msg(Message.RIGHT_CLICK_COPY);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandCopy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */