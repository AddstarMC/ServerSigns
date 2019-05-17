package au.com.addstar.serversigns.taskmanager;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.legacy.QueueToTaskConverter;
import au.com.addstar.serversigns.taskmanager.datastorage.IDataStorageAccessor;
import au.com.addstar.serversigns.taskmanager.datastorage.IDataStorageHandler;
import au.com.addstar.serversigns.taskmanager.datastorage.PersistAction;
import au.com.addstar.serversigns.taskmanager.datastorage.PersistTask;
import au.com.addstar.serversigns.taskmanager.datastorage.SQLiteDataStorageHandler;
import au.com.addstar.serversigns.taskmanager.tasks.PlayerTask;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class TaskManager {
    private final ServerSignsPlugin plugin;
    private final AtomicLong currentId;
    private final BlockingQueue<TaskManagerTask> queue;
    private final IDataStorageHandler dataStorageHandler;
    private final PlayerJoinTaskManager playerJoinTaskManager;
    private final ITaskExecutor<TaskManagerTask> taskExecutor;
    private final QueueConsumer<TaskManagerTask> queueConsumer;
    private final Thread taskManagerThread;

    TaskManager(ServerSignsPlugin plugin, AtomicLong currentId, BlockingQueue<TaskManagerTask> queue, IDataStorageHandler dataStorageHandler, PlayerJoinTaskManager playerJoinTaskManager, ITaskExecutor<TaskManagerTask> taskExecutor, QueueConsumer<TaskManagerTask> queueConsumer, Thread taskManagerThread) {
        this.plugin = plugin;
        this.currentId = currentId;
        this.queue = queue;
        this.dataStorageHandler = dataStorageHandler;
        this.playerJoinTaskManager = playerJoinTaskManager;
        this.taskExecutor = taskExecutor;
        this.queueConsumer = queueConsumer;
        this.taskManagerThread = taskManagerThread;
    }

    public TaskManager(ServerSignsPlugin plugin, Path dataFolder) throws Exception {
        this.plugin = plugin;
        this.currentId = new AtomicLong();
        this.queue = new java.util.concurrent.DelayQueue();
        this.dataStorageHandler = new SQLiteDataStorageHandler(dataFolder);
        this.playerJoinTaskManager = new PlayerJoinTaskManager(plugin, this.queue, this.dataStorageHandler);
        this.taskExecutor = new TaskManagerTaskExecutor(plugin, this.dataStorageHandler, this.playerJoinTaskManager);
        this.queueConsumer = new QueueConsumer(this.queue, new BukkitTaskManagerTaskExecutor(plugin, this.taskExecutor));
        this.taskManagerThread = new Thread(this.queueConsumer, "ServerSigns-TaskManager");
    }

    public void init() throws Exception {
        this.plugin.getServer().getPluginManager().registerEvents(this.playerJoinTaskManager, this.plugin);
        this.dataStorageHandler.init();
        IDataStorageAccessor dataStorageAccessor = this.dataStorageHandler.newDataStorageAccessor();
        Throwable localThrowable2 = null;
        try {
            Collection<TaskManagerTask> tasks = dataStorageAccessor.loadTasks(this.plugin);
            if (tasks.size() > 0) {
                long highestId = 0L;
                for (TaskManagerTask taskManagerTask : tasks) {
                    this.queue.offer(taskManagerTask);
                    highestId = Math.max(taskManagerTask.getTaskID(), highestId);
                }
                this.currentId.set(highestId + 1L);
            }
        } catch (Throwable localThrowable1) {
            localThrowable2 = localThrowable1;
            throw localThrowable1;


        } finally {


            if (dataStorageAccessor != null) if (localThrowable2 != null) try {
                dataStorageAccessor.close();
            } catch (Throwable x2) {
                localThrowable2.addSuppressed(x2);
            }
            else dataStorageAccessor.close();
        }
        QueueToTaskConverter.QueueToTaskResult result = QueueToTaskConverter.convertFile(this.plugin.getDataFolder().toPath());
        if (result != null) {
            if (result.getHighestId() > this.currentId.get()) {
                this.currentId.set(result.getHighestId());
            }
            for (TaskManagerTask task : result.getTasks()) {
                addTask(task);
            }
        }
    }

    public void start() {
        this.taskManagerThread.start();
    }

    public void addTask(TaskManagerTask task) {
        task.setTaskID(this.currentId.getAndIncrement());
        if (task.getRunAt() == 0L) {
            this.taskExecutor.runTask(task);
        } else {
            if ((task.getDelay(java.util.concurrent.TimeUnit.SECONDS) > this.plugin.getServerSignsConfig().getTaskPersistThreshold()) || (task.isAlwaysPersisted())) {
                this.dataStorageHandler.addTask(new PersistTask(PersistAction.SAVE, task));
                task.setPersisted(true);
            }
            this.queue.offer(task);
        }
    }

    public int removePlayerTasks(UUID player, Pattern regexPattern) {
        int removed = 0;
        for (Iterator<TaskManagerTask> it = this.queue.iterator(); it.hasNext(); ) {
            TaskManagerTask task = it.next();
            if ((task instanceof PlayerTask)) {
                PlayerTask ptask = (PlayerTask) task;
                if ((ptask.getPlayerUniqueId().equals(player)) && (
                        (regexPattern == null) || (regexPattern.matcher(ptask.getData()).matches()))) {
                    it.remove();
                    removed++;
                }
            }
        }

        return removed;
    }

    public void stop() {
        this.queueConsumer.stop();
        this.taskManagerThread.interrupt();

        for (TaskManagerTask taskManagerTask : this.queue) {
            if (!taskManagerTask.isPersisted()) {
                this.dataStorageHandler.addTask(new PersistTask(PersistAction.SAVE, taskManagerTask));
            }
        }

        this.dataStorageHandler.close();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\TaskManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */