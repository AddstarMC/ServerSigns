package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.signs.CancelMode;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandCancel extends SubCommand {
    public SubCommandCancel(ServerSignsPlugin plugin) {
        super(plugin, "set_cancel", "cancel <cancel mode>", "Set if/when this sign should cancel the interact event. Available modes: always|never|success_only|fail_only", "cancel", "setcancel", "stopevent");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        String str = argStr(0);
        CancelMode mode;
        try {
            mode = CancelMode.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException ex) {
            if (verbose) sendUsage();
            return;
        }

        applyMeta(SVSMetaKey.CANCEL, new SVSMetaValue(mode.name()));
        if (verbose) msg(Message.RIGHT_CLICK_APPLY);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandCancel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */