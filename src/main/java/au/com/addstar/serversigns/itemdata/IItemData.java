package au.com.addstar.serversigns.itemdata;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface IItemData {
    ItemStack applyValue(ItemStack paramItemStack, String paramString)
            throws DataException;

    ItemMeta applyMetaValue(ItemMeta paramItemMeta, String paramString)
            throws DataException;
}


