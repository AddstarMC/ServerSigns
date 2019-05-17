package au.com.addstar.serversigns.taskmanager;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.datastorage.PersistTask;
import au.com.addstar.serversigns.taskmanager.datastorage.IDataStorageHandler;
import au.com.addstar.serversigns.taskmanager.datastorage.PersistAction;
import au.com.addstar.serversigns.taskmanager.tasks.PlayerTask;
import au.com.addstar.serversigns.taskmanager.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TaskManagerTaskExecutor implements ITaskExecutor<TaskManagerTask> {
    private final ServerSignsPlugin plugin;
    private final IDataStorageHandler dataStorageHandler;
    private final PlayerJoinTaskManager playerJoinTaskManager;

    public TaskManagerTaskExecutor(ServerSignsPlugin plugin, IDataStorageHandler dataStorageHandler, PlayerJoinTaskManager playerJoinTaskManager) {
        this.plugin = plugin;
        this.dataStorageHandler = dataStorageHandler;
        this.playerJoinTaskManager = playerJoinTaskManager;
    }

    public void runTasks(List<? extends TaskManagerTask> tasks) {
        List<PlayerTask> tasksWaitingForPlayer = new ArrayList();
        for (TaskManagerTask task : tasks) {
            runTaskImpl(task, tasksWaitingForPlayer);
        }
        cleanUp(tasksWaitingForPlayer);
    }

    public void runTask(TaskManagerTask task) {
        List<PlayerTask> tasksWaitingForPlayer = new ArrayList();
        runTaskImpl(task, tasksWaitingForPlayer);
        cleanUp(tasksWaitingForPlayer);
    }

    private void runTaskImpl(TaskManagerTask task, List<PlayerTask> tasksWaitingForPlayer) {
        TaskStatus taskStatus;
        try {
            taskStatus = task.runTask(this.plugin);
        } catch (RuntimeException e) {
            taskStatus = TaskStatus.ERROR;
            this.plugin.getLogger().log(Level.WARNING, "Error while executing task " + task, e);
        }

        if (((task instanceof PlayerTask)) && (taskStatus == TaskStatus.PLAYER_NOT_ONLINE)) {
            tasksWaitingForPlayer.add((PlayerTask) task);
        } else if (task.isPersisted()) {
            this.dataStorageHandler.addTask(new PersistTask(PersistAction.DELETE, task));
        }
    }

    private void cleanUp(List<PlayerTask> tasksWaitingForPlayer) {
        if (tasksWaitingForPlayer.size() > 0) {
            this.playerJoinTaskManager.addPlayerJoinTasks(tasksWaitingForPlayer);
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\TaskManagerTaskExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */