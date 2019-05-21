package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.translations.Message;
import au.com.addstar.serversigns.commands.core.SubCommand;

public class SubCommandPriceItemCriteria extends SubCommand {
    public SubCommandPriceItemCriteria(ServerSignsPlugin plugin) {
        super(plugin, "price_item_criteria", "pic <enchants> <lores> <name> <durability>", "Set whether the listed item attributes should be ignored in price item checks on an existing ServerSign (true/false)", "pic", "priceitemcriteria", "priceitemcrit", "picrit", "picriteria");
    }


    public void execute(boolean verbose) {
        if (!argSet(3)) {
            if (verbose) sendUsage();
            return;
        }


        if ((!arg(0).equalsIgnoreCase("true")) && (!arg(0).equalsIgnoreCase("false"))) {
            if (verbose) msg(Message.ITEM_CRITERIA_BOOLEAN);
            return;
        }

        applyMeta(SVSMetaKey.PRICE_ITEM_CRITERIA, new SVSMetaValue(argBool(0, true)), new SVSMetaValue(argBool(1, true)), new SVSMetaValue(argBool(2, true)), new SVSMetaValue(argBool(3, true)));
        if (verbose) msg(Message.PRICE_ITEM_CRITERIA_BIND);
    }
}


