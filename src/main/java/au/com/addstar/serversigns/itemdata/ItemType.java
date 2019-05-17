package au.com.addstar.serversigns.itemdata;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemType extends ItemData implements IItemData {
    private static ItemType i = new ItemType();

    public ItemType() {
        super(ItemPart.TYPE, new String[]{"id.", "type.", "identity.", "item."});
    }

    public static ItemType get() {
        return i;
    }

    public ItemStack applyValue(ItemStack item, String value)
            throws DataException {
        org.bukkit.Material mat = org.bukkit.Material.getMaterial(value);
        if (mat == null) {
            try {
                mat = org.bukkit.Material.getMaterial(Integer.parseInt(value));
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        if (mat == null) {
            throw new DataException(value + " is not a valid ItemStack type!");
        }

        if (item != null) {
            item.setType(mat);
            return item;
        }

        return new ItemStack(mat);
    }

    public ItemMeta applyMetaValue(ItemMeta item, String value) throws DataException {
        throw new UnsupportedOperationException();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\ItemType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */