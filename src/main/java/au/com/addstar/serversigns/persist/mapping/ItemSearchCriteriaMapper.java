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
            return new ItemSearchCriteria(false, false, false);
        return new ItemSearchCriteria(this.memorySection.getBoolean(path + ".enchants"), this.memorySection.getBoolean(path + ".name"), this.memorySection.getBoolean(path + ".lores"));
    }


    public void setValue(String path, ItemSearchCriteria val) {
        this.memorySection.set(path + ".enchants", val.getEnchantsCriteria());
        this.memorySection.set(path + ".name", val.getIgnoreName());
        this.memorySection.set(path + ".lores", val.getIgnoreLore());
    }
}


