package au.com.addstar.serversigns.taskmanager;

import au.com.addstar.serversigns.ServerSignsPlugin;

import java.util.List;

public class BukkitTaskManagerTaskExecutor implements ITaskExecutor<TaskManagerTask> {
    private final ServerSignsPlugin plugin;
    private final ITaskExecutor<TaskManagerTask> taskExecutor;

    public BukkitTaskManagerTaskExecutor(ServerSignsPlugin plugin, ITaskExecutor<TaskManagerTask> taskExecutor) {
        this.plugin = plugin;
        this.taskExecutor = taskExecutor;
    }

    public void runTasks(List<? extends TaskManagerTask> tasks) {
        BukkitTaskExecutor bukkitTaskExecutor = new BukkitTaskExecutor(tasks);
        bukkitTaskExecutor.runTask(this.plugin);

        synchronized (this) {
            while (bukkitTaskExecutor.isRunning()) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void runTask(TaskManagerTask task) {
        throw new UnsupportedOperationException();
    }

    private class BukkitTaskExecutor extends org.bukkit.scheduler.BukkitRunnable {
        private final List<? extends TaskManagerTask> tasks;
        private boolean running = true;

        public BukkitTaskExecutor(List<? extends TaskManagerTask> tasks) {
            this.tasks = tasks;
        }

        public boolean isRunning() {
            return this.running;
        }

        public void run() {
            BukkitTaskManagerTaskExecutor.this.taskExecutor.runTasks(this.tasks);

            synchronized (BukkitTaskManagerTaskExecutor.this) {
                this.running = false;
                BukkitTaskManagerTaskExecutor.this.notify();
            }
        }
    }
}


