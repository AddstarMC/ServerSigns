package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.*;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.meta.SVSMeta;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandConfirmation extends SubCommand {
    public SubCommandConfirmation(ServerSignsPlugin plugin) {
        super(plugin, "confirmation", "confirmation {true | false} [message]", "Set whether this sign should ask for confirmation before allowing use", "conf", "confirm", "confirmation", "cf");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        boolean value = argBool(0).booleanValue();
        SVSMeta meta = new SVSMeta(SVSMetaKey.CONFIRMATION, new SVSMetaValue(Boolean.valueOf(value)));
        if ((value) && (argSet(1))) {
            meta.addValue(new SVSMetaValue(loopArgs(1)));
        }

        SVSMetaManager.setMeta(this.player, meta);
        if (verbose) msg(Message.RIGHT_CLICK_APPLY);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandConfirmation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */