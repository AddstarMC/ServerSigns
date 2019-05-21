package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.taskmanager.tasks.ServerActionTaskType;

public class ServerActionTaskTypeEnumMapper implements IPersistenceMapper<ServerActionTaskType> {
    private org.bukkit.configuration.MemorySection memorySection;

    public void setMemorySection(org.bukkit.configuration.MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public ServerActionTaskType getValue(String path) {
        try {
            return Enum.valueOf(ServerActionTaskType.class, this.memorySection.getString(path));
        } catch (IllegalArgumentException | NullPointerException ignored) {
        }
        return null;
    }


    public void setValue(String path, ServerActionTaskType value) {
        this.memorySection.set(path, value.toString());
    }
}


