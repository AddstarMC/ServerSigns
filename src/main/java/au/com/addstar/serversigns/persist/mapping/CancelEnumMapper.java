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
        } catch (IllegalArgumentException | NullPointerException ex) {
        }
        return null;
    }


    public void setValue(String path, CancelMode value) {
        this.memorySection.set(path, value.toString());
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\CancelEnumMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */