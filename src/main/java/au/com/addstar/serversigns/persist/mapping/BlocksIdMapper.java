package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.utils.MaterialConvertor;
import au.com.addstar.serversigns.utils.NumberUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;

public class BlocksIdMapper implements IPersistenceMapper<EnumSet<Material>> {
    private MemorySection memorySection;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public EnumSet<Material> getValue(String path) {
        List<String> blocksList = this.memorySection.getStringList(path);
        EnumSet<Material> blocks = EnumSet.noneOf(Material.class);
        for (String blockId : blocksList) {
            int val = NumberUtils.parseInt(blockId, -1);
            Material material = MaterialConvertor.getMaterialById(val);
            if (material != null) {
                blocks.add(material);
            }
        }
        return blocks;
    }

    public void setValue(String path, EnumSet<Material> val) {
        ArrayList<String> list = new ArrayList();
        for (Object material : val) {
            list.add(material.toString());
        }

        this.memorySection.set(path, list);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\BlocksIdMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */