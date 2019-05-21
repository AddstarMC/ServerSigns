package au.com.addstar.serversigns.commands;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.meta.SVSMetaKey;
import au.com.addstar.serversigns.meta.SVSMetaValue;
import au.com.addstar.serversigns.utils.ItemUtils;
import au.com.addstar.serversigns.commands.core.SubCommand;
import au.com.addstar.serversigns.translations.Message;
import org.bukkit.inventory.ItemStack;

public class SubCommandHolding extends SubCommand {
    public SubCommandHolding(ServerSignsPlugin plugin) {
        super(plugin, "holding", "holding {hand|0|<item data...>}", "Add held item conditions for an existing ServerSign", "holding", "hold", "held");
    }


    public void execute(boolean verbose) {
        if (!argSet(0)) {
            if (verbose) {
                sendUsage();
            }
            return;
        }
        ItemStack item;
        if (arg(0).equalsIgnoreCase("hand")) {
            if (this.player.getInventory().getItemInMainHand().getType().equals(org.bukkit.Material.AIR)) {
                if (verbose) msg(Message.INVALID_HAND_ITEM);
                return;
            }

            item = this.player.getInventory().getItemInMainHand();
        } else {
            if ((arg(0).equalsIgnoreCase("0")) || (arg(0).equalsIgnoreCase("off"))) {
                applyMeta(SVSMetaKey.HOLDING, new SVSMetaValue(null));
                if (verbose) msg(Message.HOLDING_REMOVE);
                return;
            }
            item = ItemUtils.getItemStackFromString(loopArgs(0));
            if (item == null) {
                if (verbose)
                    msg("Invalid item string - Use this format, in any order: id.<type> [am.<amount>] [du.<durability>] [na.<display name>] [lo.<lore>]... [en.<enchantment>.<level>]...");
                return;
            }
        }

        applyMeta(SVSMetaKey.HOLDING, new SVSMetaValue(item));
        if (verbose) msg(Message.HOLDING_BIND);
    }
}


