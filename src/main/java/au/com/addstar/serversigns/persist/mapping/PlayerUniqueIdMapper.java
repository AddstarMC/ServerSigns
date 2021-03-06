package au.com.addstar.serversigns.persist.mapping;

import java.util.UUID;

import org.bukkit.configuration.MemorySection;

public class PlayerUniqueIdMapper implements IPersistenceMapper<UUID> {
    private MemorySection memorySection;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public UUID getValue(String path) {
        String string = this.memorySection.getString(path);
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }


    public void setValue(String path, UUID value) {
        this.memorySection.set(path, value.toString());
    }
}


