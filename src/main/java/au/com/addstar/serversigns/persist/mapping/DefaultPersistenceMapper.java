package au.com.addstar.serversigns.persist.mapping;

import org.bukkit.configuration.MemorySection;

public class DefaultPersistenceMapper implements IPersistenceMapper<Object> {
    private MemorySection memorySection;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public Object getValue(String path) {
        return this.memorySection.get(path);
    }

    public void setValue(String path, Object value) {
        this.memorySection.set(path, value);
    }
}


