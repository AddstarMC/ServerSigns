package au.com.addstar.serversigns.itemdata;

import au.com.addstar.serversigns.ServerSignsPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStringParser {
    private static final String LOG_PREFIX = "[ServerSigns] ";
    private static final String DELIMITER = " ";
    private static final String EQUALIZER = ".";
    private boolean parseCalled = false;
    private String input;
    private ItemStack result;
    private boolean errors;

    public ItemStringParser(String input) {
        this.input = input;
    }

    public void setNewInput(String input) {
        this.input = input;
        this.parseCalled = false;
        this.result = null;
    }

    public String getInput() {
        return this.input;
    }

    public ItemStack getLastResult() {
        if (!this.parseCalled) {
            throw new IllegalAccessError("getResult() cannot be called before parse()!");
        }
        return this.result;
    }

    public ItemStack parse() {
        return parse(false);
    }

    public ItemStack parse(boolean deepLog) {
        this.parseCalled = true;
        this.errors = false;
        HashMap<ItemPart, List<String>> dataMap = createPartMap(false);
        if (dataMap.isEmpty()) {
            ServerSignsPlugin.log("Disregarding invalid item string: '" + this.input + "'");
            this.errors = true;
            return null;
        }
        ItemStack stack;
        try {
            if ((!dataMap.containsKey(ItemPart.TYPE)) || (dataMap.get(ItemPart.TYPE).size() < 1))
                throw new DataException("No type (id) can be found!");
            if (dataMap.get(ItemPart.TYPE).size() > 1) {
                throw new DataException("Too many types (ids) were found!");
            }
            stack = ItemPart.TYPE.getAttachedData().applyValue(null, (String) ((List) dataMap.get(ItemPart.TYPE)).iterator().next());
            if (stack == null) throw new DataException("No type (id) can be found!");
        } catch (DataException ex) {
            ServerSignsPlugin.log("Disregarding invalid item string: '" + this.input + "' - " + ex.getMessage());
            this.errors = true;
            return null;
        }
        ItemMeta meta = stack.getItemMeta();
        for (ItemPart rel : dataMap.keySet()) {
            ItemData data = rel.getAttachedData();
            for (String value : dataMap.get(rel)) {
                try {
                    if (data.canApplyToMeta())
                        meta = data.applyMetaValue(meta, value);
                    else
                        stack = data.applyValue(stack, value);
                } catch (DataException ex) {
                    ServerSignsPlugin.log(LOG_PREFIX + "Disregarding invalid ItemPart with value '" + value + "' - " + ex.getMessage());
                    this.errors = true;
                }
            }
        }
        stack.setItemMeta(meta);
        this.result = stack;
        return stack;
    }

    public boolean encounteredErrors() {
        return this.errors;
    }

    protected HashMap<ItemPart, List<String>> createPartMap(boolean deepLog) {
        HashMap<ItemPart, List<String>> map = new HashMap<>();
        if (this.input.isEmpty()) {
            return map;
        }
        if (!this.input.contains(DELIMITER)) {
            if ((!this.input.contains(".")) || (this.input.indexOf(EQUALIZER) == this.input.length())) {
                return map;
            }
            String key = this.input.substring(0, this.input.indexOf(EQUALIZER));
            ItemPart part = ItemPart.getPartFromPrefix(key);
            if (part == null) {
                return map;
            }
            String value = this.input.substring(this.input.indexOf(EQUALIZER) + 1);
            List<String> values = new ArrayList();
            values.add(value);

            map.put(part, values);
            return map;
        }
        String[] set = this.input.split(DELIMITER);

        for (String str : set)
            if ((!str.contains(EQUALIZER)) || (this.input.indexOf(EQUALIZER) == this.input.length())) {
                ServerSignsPlugin.log(LOG_PREFIX + "Disregarding invalid ItemPart '" + str + "' as no value can be found");
                this.errors = true;
            } else {
                String key = str.substring(0, str.indexOf(EQUALIZER));
                ItemPart part = ItemPart.getPartFromPrefix(key);
                if (part == null) {
                    if (deepLog) {
                        ServerSignsPlugin.log(LOG_PREFIX + "Disregarding invalid ItemPart definition: '" + key + "'");
                    }
                } else {
                    String value = str.substring(str.indexOf(EQUALIZER) + 1);

                    if (map.containsKey(part)) {
                        List<String> values = map.get(part);
                        values.add(value);

                        map.put(part, values);
                    } else {
                        List<String> values = new ArrayList<>();
                        values.add(value);

                        map.put(part, values);
                    }
                }
            }
        return map;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\itemdata\ItemStringParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */