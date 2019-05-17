package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.utils.ItemUtils;
import au.com.addstar.serversigns.itemdata.ItemStringParser;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;

public class ItemStackListMapper implements ISmartPersistenceMapper<List<ItemStack>> {
    private MemorySection memorySection;
    private String host = "unknown";

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public List<ItemStack> getValue(String path) {
        ArrayList<ItemStack> stacks = new ArrayList();
        List<String> rawStrings = this.memorySection.getStringList(path);

        for (String raw : rawStrings) {
            ItemStringParser parser = new ItemStringParser(raw);
            ItemStack stack = parser.parse();
            if (stack != null) {
                if (parser.encounteredErrors()) {
                    ServerSignsPlugin.log("Encountered errors while building ItemStack from '" + this.host + "'");
                }
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public void setValue(String path, List<ItemStack> val) {
        ArrayList<String> list = new ArrayList();
        for (ItemStack stack : val) {
            list.add(ItemUtils.getStringFromItemStack(stack));
        }

        this.memorySection.set(path, list);
    }

    public void setHostId(String id) {
        this.host = id;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\ItemStackListMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */