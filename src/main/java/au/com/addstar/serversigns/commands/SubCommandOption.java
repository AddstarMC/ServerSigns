package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandOption extends SubCommand {
    public SubCommandOption(ServerSignsPlugin plugin) {
        super(plugin, "option", "option <id> {question|add|remove}", "Bind player-input option questions & answers to ServerSigns", "option", "opt");
    }

    public void execute(boolean verbose) {
        if (!argSet(1)) {
            if (verbose) sendUsage();
            return;
        }

        String displayName = arg(0);
        String param1 = arg(1);

        SVSMetaValue[] values;
        int optionId;
        if ((param1.equalsIgnoreCase("question")) || (param1.equalsIgnoreCase("q"))) {
            if (!argSet(2)) {
                msg("/" + getLastLabel() + " option " + arg(0) + " " + arg(1) + " <question to ask player>");
                return;
            }
            optionId = 0;
            values = new SVSMetaValue[]{null, null, new SVSMetaValue(loopArgs(2))};
        } else {
            if ((param1.equalsIgnoreCase("add")) || (param1.equalsIgnoreCase("a"))) {
                if (!argSet(3)) {
                    msg("/" + getLastLabel() + " option " + arg(0) + " " + arg(1) + " <answer label> <description>");
                    return;
                }
                optionId = 1;
                values = new SVSMetaValue[]{null, null, new SVSMetaValue(arg(2)), new SVSMetaValue(loopArgs(3))};
            } else {
                if ((param1.equalsIgnoreCase("remove")) || (param1.equalsIgnoreCase("r"))) {
                    if (!argSet(2)) {
                        msg("/" + getLastLabel() + " option " + arg(0) + " " + arg(1) + " <answer label>");
                        return;
                    }
                    optionId = 2;
                    values = new SVSMetaValue[]{null, null, new SVSMetaValue(arg(2))};
                } else {
                    if (verbose) sendUsage();
                    return;
                }
            }
        }
        values[0] = new SVSMetaValue(displayName);
        values[1] = new SVSMetaValue(optionId);

        applyMeta(SVSMetaKey.OPTION, values);
        msg(Message.RIGHT_CLICK_APPLY);
    }
}


