package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.signs.CancelMode;

public class CancelEnumMapper implements IPersistenceMapper<CancelMode> {
    private org.bukkit.configuration.MemorySection memorySection;

    public void setMemorySection(org.bukkit.configuration.MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public CancelMode getValue(String path) {
        try {
            return Enum.valueOf(CancelMode.class, this.memorySection.getString(path));
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        return null;
    }


    public void setValue(String path, CancelMode value) {
        this.memorySection.set(path, value.toString());
    }
}


