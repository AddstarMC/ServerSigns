package au.com.addstar.serversigns.itemdata;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemLore extends ItemData implements IItemData {
    private static ItemLore i = new ItemLore();

    public ItemLore() {
        super(ItemPart.LORES, new String[]{"lo.", "lores.", "lore."});
        setApplyToMeta(true);
    }

    public static ItemLore get() {
        return i;
    }

    public ItemStack applyValue(ItemStack item, String value) throws DataException {
        if (item == null) {
            return null;
        }
        ItemMeta meta = applyMetaValue(item.getItemMeta(), value);
        item.setItemMeta(meta);

        return item;
    }

    public ItemMeta applyMetaValue(ItemMeta meta, String value) throws DataException {
        if (!value.isEmpty()) {
            List<String> lores = (meta.hasLore()) ? meta.getLore() : new ArrayList<>();
            lores.add(ChatColor.translateAlternateColorCodes('&', value.replaceAll("_", " ")));
            meta.setLore(lores);

            return meta;
        }

        throw new DataException("Empty strings cannot be used for ItemStack lores!");
    }
}


