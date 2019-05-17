package au.com.addstar.serversigns.persist.mapping;

import java.util.HashMap;

import org.bukkit.configuration.MemorySection;

public class StringLongHashMapper implements IPersistenceMapper<HashMap<String, Long>> {
    private MemorySection memorySection;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public HashMap<String, Long> getValue(String path) {
        HashMap<String, Long> map = new HashMap();
        if (this.memorySection.getConfigurationSection(path) == null) return map;
        for (String key : this.memorySection.getConfigurationSection(path).getKeys(false)) {
            map.put(key, Long.valueOf(this.memorySection.getLong(path + "." + key)));
        }

        return map;
    }

    public void setValue(String path, HashMap<String, Long> val) {
        for (Map.Entry<?, ?> entry : val.entrySet()) {
            this.memorySection.set(path + "." + entry.getKey().toString(), entry.getValue());
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\StringLongHashMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */