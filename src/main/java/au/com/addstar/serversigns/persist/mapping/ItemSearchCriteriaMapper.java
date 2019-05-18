package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.itemdata.ItemSearchCriteria;
import org.bukkit.configuration.MemorySection;

public class ItemSearchCriteriaMapper implements IPersistenceMapper<ItemSearchCriteria> {
    private MemorySection memorySection;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public ItemSearchCriteria getValue(String path) {
        if (this.memorySection.getConfigurationSection(path) == null)
            return new ItemSearchCriteria(false, false, false, false);
        return new ItemSearchCriteria(this.memorySection.getBoolean(path + ".enchants"), this.memorySection.getBoolean(path + ".name"), this.memorySection.getBoolean(path + ".lores"), this.memorySection.getBoolean(path + ".durability"));
    }


    public void setValue(String path, ItemSearchCriteria val) {
        this.memorySection.set(path + ".enchants", val.getEnchantsCriteria());
        this.memorySection.set(path + ".name", val.getIgnoreName());
        this.memorySection.set(path + ".lores", val.getIgnoreLore());
        this.memorySection.set(path + ".durability", val.getIgnoreDurability());
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\ItemSearchCriteriaMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */