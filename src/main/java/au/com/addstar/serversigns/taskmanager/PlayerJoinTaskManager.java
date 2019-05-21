package au.com.addstar.serversigns.taskmanager;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.datastorage.PersistAction;
import au.com.addstar.serversigns.taskmanager.datastorage.PersistTask;
import au.com.addstar.serversigns.taskmanager.datastorage.IDataStorageHandler;
import au.com.addstar.serversigns.taskmanager.tasks.PlayerTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinTaskManager implements Listener {
    private final Map<UUID, List<PlayerTask>> playerJoinTasks = new HashMap<>();

    private final ServerSignsPlugin plugin;
    private final BlockingQueue<TaskManagerTask> taskQueue;
    private final IDataStorageHandler dataStorageHandler;

    public PlayerJoinTaskManager(ServerSignsPlugin plugin, BlockingQueue<TaskManagerTask> taskQueue, IDataStorageHandler dataStorageHandler) {
        this.plugin = plugin;
        this.taskQueue = taskQueue;
        this.dataStorageHandler = dataStorageHandler;
    }

    public void addPlayerJoinTasks(List<PlayerTask> tasksWaitingForPlayer) {
        if (tasksWaitingForPlayer.size() > 0) {
            synchronized (this.playerJoinTasks) {
                for (PlayerTask playerTask : tasksWaitingForPlayer) {
                    if (this.plugin.getServer().getPlayer(playerTask.getPlayerUniqueId()) != null) {
                        this.taskQueue.offer(playerTask);
                    } else {
                        List<PlayerTask> playerTasks = this.playerJoinTasks.computeIfAbsent(playerTask.getPlayerUniqueId(), k -> new java.util.ArrayList<>());
                        playerTasks.add(playerTask);

                        if (!playerTask.isPersisted()) {
                            this.dataStorageHandler.addTask(new PersistTask(PersistAction.SAVE, playerTask));
                        }
                    }
                }
            }
        }
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUniqueId = event.getPlayer().getUniqueId();
        synchronized (this.playerJoinTasks) {
            if (this.playerJoinTasks.containsKey(playerUniqueId)) {
                for (PlayerTask playerTask : this.playerJoinTasks.get(playerUniqueId)) {
                    this.taskQueue.offer(playerTask);
                }
                this.playerJoinTasks.remove(playerUniqueId);
            }
        }
    }

    Map<UUID, List<PlayerTask>> getPlayerJoinTasks() {
        return this.playerJoinTasks;
    }
}
