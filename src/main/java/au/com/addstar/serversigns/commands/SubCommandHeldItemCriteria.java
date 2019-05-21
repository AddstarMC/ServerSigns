package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;

public class SubCommandHeldItemCriteria extends SubCommand {
    public SubCommandHeldItemCriteria(ServerSignsPlugin plugin) {
        super(plugin, "held_item_criteria", "hic <enchants> <lores> <name> <durability>", "Set whether the listed item attributes should be ignored in held item checks on an existing ServerSign (true/false)", "hic", "helditemcriteria", "holditemcriteria", "holdcriteria");
    }


    public void execute(boolean verbose) {
        if (!argSet(3)) {
            if (verbose) sendUsage();
            return;
        }


        if ((!arg(0).equalsIgnoreCase("true")) && (!arg(1).equalsIgnoreCase("false"))) {
            if (verbose) msg(Message.ITEM_CRITERIA_BOOLEAN);
            return;
        }

        applyMeta(SVSMetaKey.HELD_ITEM_CRITERIA, new SVSMetaValue(argBool(0, true)), new SVSMetaValue(argBool(1, true)), new SVSMetaValue(argBool(2, true)), new SVSMetaValue(argBool(3, true)));
        if (verbose) msg(Message.HELD_ITEM_CRITERIA_BIND);
    }
}


