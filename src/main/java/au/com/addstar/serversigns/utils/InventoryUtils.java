package au.com.addstar.serversigns.utils;

import au.com.addstar.serversigns.itemdata.ItemSearchCriteria;

import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    public static Collection<ItemStack> scan(Inventory inventory, ItemSearchCriteria criteria, boolean removeOnFind, ItemStack... items) {
        return scan(inventory, criteria.getEnchantsCriteria(), criteria.getIgnoreLore(), criteria.getIgnoreName(), removeOnFind, items);
    }


    public static Collection<ItemStack> scan(Inventory inventory, boolean ignoreEnchants, boolean ignoreLores, boolean ignoreName, boolean removeOnFind, ItemStack... items) {
        Validate.notNull(items, "Items cannot be null");
        Validate.notNull(inventory, "Inventory cannot be null");

        Inventory clone = cloneInventory(inventory);
        HashMap<Integer, ItemStack> leftover = new HashMap<>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i].clone();
            int toDelete = item.getAmount();

            while (toDelete > 0) {
                int first = first(clone, item, true, ignoreEnchants, ignoreLores, ignoreName);

                if (first < 0) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                }

                ItemStack itemStack = clone.getItem(first);
                int amount = itemStack.getAmount();

                if (amount <= toDelete) {
                    toDelete -= amount;
                    clone.clear(first);
                    if (removeOnFind) inventory.clear(first);
                } else {
                    itemStack.setAmount(amount - toDelete);
                    clone.setItem(first, itemStack);
                    if (removeOnFind) inventory.setItem(first, itemStack);
                    toDelete = 0;
                }
            }
        }

        return leftover.values();
    }

    public static int first(Inventory inventory, ItemStack item, boolean ignoreAmount, boolean ignoreEnchants, boolean ignoreLores, boolean ignoreName) {
        Validate.notNull(inventory);
        Validate.notNull(item);

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if ((stack != null) &&
                    (ItemUtils.compare(item, stack, false,  ignoreAmount, ignoreName, ignoreLores, ignoreEnchants)))
                return i;
        }
        return -1;
    }

    public static ItemStack[] cloneItemStacks(ItemStack[] itemStacks) {
        ItemStack[] ret = new ItemStack[itemStacks.length];
        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack stack = itemStacks[i];
            if (stack != null)
                ret[i] = new ItemStack(itemStacks[i]);
        }
        return ret;
    }

    public static Inventory cloneInventory(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        int size = inventory.getSize();
        InventoryType type = inventory.getType();

        int rem = size % 9;
        if (rem > 0) {
            size += 9 - rem;
        }
        InventoryHolder holder = inventory.getHolder();
        Inventory ret;
        if (size != type.getDefaultSize()) {
            ret = Bukkit.createInventory(holder, size);
        } else
            ret = Bukkit.createInventory(holder, type);
        ItemStack[] contents = cloneItemStacks(inventory.getContents());
        ret.setContents(contents);

        return ret;
    }
}

