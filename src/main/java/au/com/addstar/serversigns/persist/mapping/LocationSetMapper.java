package au.com.addstar.serversigns.persist.mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;

public class LocationSetMapper implements ISmartPersistenceMapper<Set<Location>> {
    private MemorySection memorySection;
    private String host = "unknown";

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public Set<Location> getValue(String path) {
        List<String> strings = this.memorySection.getStringList(path);
        Set<Location> locs = new HashSet();

        for (String str : strings) {
            Location loc = stringToLocation(str);
            if (loc != null) {
                locs.add(loc);
            }
        }
        return locs;
    }

    public void setValue(String path, Set<Location> val) {
        ArrayList<String> list = new ArrayList();
        for (Location loc : val) {
            list.add(loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        }

        this.memorySection.set(path, list);
    }

    public void setHostId(String id) {
        this.host = id;
    }

    private Location stringToLocation(String input) {
        try {
            String[] split = input.split(",");
            if (split.length < 4) {
                return null;
            }
            int z = Integer.parseInt(split[(split.length - 1)]);
            int y = Integer.parseInt(split[(split.length - 2)]);
            int x = Integer.parseInt(split[(split.length - 3)]);

            String worldName = "";
            for (int k = 0; k <= split.length - 4; k++) {
                worldName = worldName + split[k] + ",";
            }
            if (worldName.isEmpty()) return null;
            worldName = worldName.substring(0, worldName.length() - 1);

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                return null;
            }
            return new Location(world, x, y, z);
        } catch (NumberFormatException ex) {
        }
        return null;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\LocationSetMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */