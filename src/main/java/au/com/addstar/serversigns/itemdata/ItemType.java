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
