package au.com.addstar.serversigns.taskmanager;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueConsumer<E> implements Runnable {
    private final BlockingQueue<E> queue;
    private final ITaskExecutor<E> taskExecutor;
    private volatile boolean running = true;

    public QueueConsumer(BlockingQueue<E> queue, ITaskExecutor<E> taskExecutor) {
        this.queue = queue;
        this.taskExecutor = taskExecutor;
    }

    public void stop() {
        this.running = false;
    }

    public void run() {
        while (this.running) {
            try {
                List<E> tasksToRun = new java.util.ArrayList();
                tasksToRun.add(this.queue.take());
                this.queue.drainTo(tasksToRun);
                this.taskExecutor.runTasks(tasksToRun);
            } catch (InterruptedException ignored) {
            }
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\QueueConsumer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */