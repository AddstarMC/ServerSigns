package au.com.addstar.serversigns.itemdata;

import org.bukkit.inventory.ItemStack;

public class ItemDamage extends ItemData implements IItemData {
    private static ItemDamage i = new ItemDamage();

    public ItemDamage() {
        super(ItemPart.DAMAGE, new String[]{"du.", "durability.", "damage.", "dura."});
    }

    public static ItemDamage get() {
        return i;
    }

    public ItemStack applyValue(ItemStack item, String value) throws DataException {
        if (item == null) {
            return null;
        }
        short damage = 0;
        try {
            damage = (short) Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new DataException(damage + " is not a valid ItemStack durability (integer)!");
        }

        if (damage < 0) {
            throw new DataException(damage + " is not a valid ItemStack durability (must be >= 0)");
        }
        item.setDurability(damage);
        return item;
    }

    public org.bukkit.inventory.meta.ItemMeta applyMetaValue(org.bukkit.inventory.meta.ItemMeta item, String value) throws DataException {
        throw new UnsupportedOperationException();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\ItemDamage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */