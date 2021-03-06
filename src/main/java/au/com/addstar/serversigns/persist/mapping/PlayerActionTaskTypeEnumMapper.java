package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.taskmanager.tasks.PlayerActionTaskType;

public class PlayerActionTaskTypeEnumMapper implements IPersistenceMapper<PlayerActionTaskType> {
    private org.bukkit.configuration.MemorySection memorySection;

    public void setMemorySection(org.bukkit.configuration.MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public PlayerActionTaskType getValue(String path) {
        try {
            return Enum.valueOf(PlayerActionTaskType.class, this.memorySection.getString(path));
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        return null;
    }


    public void setValue(String path, PlayerActionTaskType value) {
        this.memorySection.set(path, value.toString());
    }
}


