package au.com.addstar.serversigns.itemdata;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface IItemData {
    ItemStack applyValue(ItemStack paramItemStack, String paramString)
            throws DataException;

    ItemMeta applyMetaValue(ItemMeta paramItemMeta, String paramString)
            throws DataException;
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\IItemData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */