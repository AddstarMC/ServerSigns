package au.com.addstar.serversigns.utils;

import au.com.addstar.serversigns.itemdata.ItemSearchCriteria;
import au.com.addstar.serversigns.itemdata.ItemStringParser;
import au.com.addstar.serversigns.legacy.ItemStringConverter;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemUtils {
    public static ItemStack getItemStackFromString(String itemString) {
        ItemStringParser parser = new ItemStringParser(ItemStringConverter.convertPreV4String(itemString));
        return parser.parse();
    }

    @Deprecated
    public static String getStringFromItemStack(ItemStack it) {
        if (it == null) return "";
        StringBuilder sb = new StringBuilder();

        sb.append("id.").append(it.getType().toString()).append(" ");
        if (it.getDurability() > 0) sb.append("du.").append(it.getDurability()).append(" ");
        if (it.getAmount() != 1) {
            sb.append("am.").append(it.getAmount()).append(" ");
        }
        if (it.getItemMeta() == null) return sb.toString().trim();
        ItemMeta im = it.getItemMeta();

        if (im.hasDisplayName()) {
            sb.append("na.").append(StringUtils.decolour(im.getDisplayName().replaceAll(" ", "_"))).append(" ");
        }
        if (im.hasLore()) {
            for (String str : im.getLore()) {
                sb.append("lo.").append(StringUtils.decolour(str.replaceAll(" ", "_"))).append(" ");
            }
        }
        if (im.hasEnchants()) {
            for (Enchantment enchant : im.getEnchants().keySet()) {
                sb.append("en.").append(ParseUtils.getStringFromEnchantment(enchant)).append(".").append(im.getEnchants().get(enchant)).append(" ");
            }
        }
        if ((im instanceof LeatherArmorMeta)) {
            LeatherArmorMeta lm = (LeatherArmorMeta) im;
            Color color = lm.getColor();
            sb.append("co.").append(color.getRed()).append(",").append(color.getGreen()).append(",").append(color.getBlue());
        }

        return sb.toString().trim();
    }

    public static boolean compare(ItemStack item1, ItemStack item2, ItemSearchCriteria criteria) {
        return compare(item1, item2, false, criteria.getIgnoreDurability(), false, criteria.getIgnoreName(), criteria.getIgnoreLore(), criteria.getEnchantsCriteria());
    }

    public static boolean compare(ItemStack item1, ItemStack item2, boolean ignoreMaterial, boolean ignoreDurability, boolean ignoreAmount, boolean ignoreName, boolean ignoreLores, boolean ignoreEnchants) {
        if ((!ignoreMaterial) &&
                (item1.getType() != item2.getType())) {
            return false;
        }
        if ((!ignoreDurability) &&
                (item1.getDurability() != item2.getDurability())) {
            return false;
        }
        if ((!ignoreAmount) &&
                (item1.getAmount() != item2.getAmount())) return false;
        ItemMeta meta2;
        if ((item1.getItemMeta() != null) && (item2.getItemMeta() != null)) {
            ItemMeta meta1 = item1.getItemMeta();
            meta2 = item2.getItemMeta();

            if (!ignoreName) {
                if (meta1.hasDisplayName() != meta2.hasDisplayName()) return false;
                if ((meta1.hasDisplayName()) &&
                        (!meta1.getDisplayName().equals(meta2.getDisplayName()))) {
                    return false;
                }
            }
            if (!ignoreLores) {
                if (meta1.hasLore() != meta2.hasLore()) return false;
                if (meta1.hasLore()) {
                    for (int i = 0; i < meta1.getLore().size(); i++) {
                        if (!meta1.getLore().get(i).equals(meta2.getLore().get(i))) return false;
                    }
                }
            }
            if (!ignoreEnchants) {
                if (meta1.hasEnchants() != meta2.hasEnchants()) return false;
                for (Map.Entry<Enchantment, Integer> entry : meta1.getEnchants().entrySet()) {
                    if (!meta2.getEnchants().containsKey(entry.getKey()))
                        return false;
                    if (meta2.getEnchantLevel(entry.getKey()) != entry.getValue().intValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static ArrayList<String> getDescription(ItemStack item, String messageColour) {
        ArrayList<String> list = new ArrayList(8);
        list.add(messageColour + "" + item.getAmount() + "x " + getFriendlyName(item.getType()) + (item.getDurability() > 0 ? ":" + item.getDurability() : ""));

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) list.add(messageColour + "  Name: " + meta.getDisplayName());
            if (meta.hasLore())
                list.add(messageColour + "  Lores: " + StringUtils.join(meta.getLore(), new StringBuilder().append(messageColour).append(", ").toString()));
            if (meta.hasEnchants()) {
                String enchants = "";
                for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                    enchants = enchants + ParseUtils.getStringFromEnchantment(entry.getKey()) + " " + entry.getValue() + ", ";
                }
                list.add(messageColour + "  Enchants: " + enchants.trim().substring(0, enchants.trim().length() - 1).trim());
            }
        }

        return list;
    }

    private static String getFriendlyName(Material material) {
        return StringUtils.firstToUpper(material.name().toLowerCase().replaceAll("_", " "));
    }
}
