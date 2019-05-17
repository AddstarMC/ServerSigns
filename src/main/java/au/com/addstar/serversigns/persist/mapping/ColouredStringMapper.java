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


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\ColouredStringMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */