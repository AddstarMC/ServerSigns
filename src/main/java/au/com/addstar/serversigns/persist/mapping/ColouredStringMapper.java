package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.utils.StringUtils;
import org.bukkit.configuration.MemorySection;

public class ColouredStringMapper implements IPersistenceMapper<String> {
    private MemorySection memorySection;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public String getValue(String path) {
        return StringUtils.colour(this.memorySection.getString(path));
    }

    public void setValue(String path, String value) {
        this.memorySection.set(path, StringUtils.decolour(value));
    }
}
