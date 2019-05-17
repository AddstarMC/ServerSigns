package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.util.UUID;

import org.bukkit.entity.Player;

public abstract class PlayerTask<T extends Enum<T>> extends TaskManagerTask<T> {
    private final UUID playerUniqueId;

    public PlayerTask(long runAt, T actionType, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(runAt, actionType, action, alwaysPersisted);
        this.playerUniqueId = playerUniqueId;
    }

    protected PlayerTask(long taskID, long runAt, T type, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(taskID, runAt, type, action, alwaysPersisted);
        this.playerUniqueId = playerUniqueId;
    }

    public UUID getPlayerUniqueId() {
        return this.playerUniqueId;
    }

    public TaskStatus runTask(ServerSignsPlugin plugin) {
        Player player = org.bukkit.Bukkit.getPlayer(getPlayerUniqueId());
        if (player == null) {
            return TaskStatus.PLAYER_NOT_ONLINE;
        }

        return runPlayerTask(plugin, player);
    }

    protected abstract TaskStatus runPlayerTask(ServerSignsPlugin paramServerSignsPlugin, Player paramPlayer);

    public String toString() {
        return "PlayerTask{data='" + getData() + '\'' + "} " + super.toString();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\tasks\PlayerTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */