package au.com.addstar.serversigns.itemdata;

import org.bukkit.inventory.ItemStack;

public class ItemAmount extends ItemData implements IItemData {
    private static ItemAmount i = new ItemAmount();

    public ItemAmount() {
        super(ItemPart.AMOUNT, new String[]{"am.", "amount.", "count."});
    }

    public static ItemAmount get() {
        return i;
    }

    public ItemStack applyValue(ItemStack item, String value) throws DataException {
        if (item == null) {
            return null;
        }
        int amount = 0;
        try {
            amount = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new DataException(amount + " is not a valid ItemStack amount (integer)!");
        }

        if (amount < 1) {
            throw new DataException(amount + " is not a valid ItemStack amount (must be >= 1)");
        }
        item.setAmount(amount);
        return item;
    }

    public org.bukkit.inventory.meta.ItemMeta applyMetaValue(org.bukkit.inventory.meta.ItemMeta item, String value) throws DataException {
        throw new UnsupportedOperationException();
    }
}


