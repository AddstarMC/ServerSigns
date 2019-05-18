package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandSilent extends SubCommand {
    public SubCommandSilent(ServerSignsPlugin plugin) {
        super(plugin, "silent", "silent {true | false}", "Set whether a ServerSign should display internal messages to the user", "silent", "sil", "quiet", "mute");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) sendUsage();
            return;
        }

        applyMeta(SVSMetaKey.SILENT, new SVSMetaValue(!argBool(0).booleanValue()));
        if (verbose) msg(Message.RIGHT_CLICK_APPLY);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\SubCommandSilent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */