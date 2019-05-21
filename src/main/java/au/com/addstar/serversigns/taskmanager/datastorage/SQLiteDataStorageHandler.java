package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.QueueConsumer;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SQLiteDataStorageHandler implements IDataStorageHandler {
    private final QueueConsumer<PersistTask> queueConsumer;
    private final String databaseUrl;
    private final BlockingQueue<PersistTask> tasksQueue;

    public SQLiteDataStorageHandler(Path dataFolder) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.databaseUrl = String.format("jdbc:sqlite:%s", dataFolder.resolve("tasks.sqlite").toString());
        this.tasksQueue = new LinkedBlockingQueue<>();
        PersistTaskExecutor persistTaskExecutor = new PersistTaskExecutor(this);
        this.queueConsumer = new QueueConsumer<>(this.tasksQueue, persistTaskExecutor);
    }

    public void init() throws Exception {
        IDataStorageAccessor storage = new SQLiteDataStorageAccessor(this.databaseUrl);
        Throwable localThrowable2 = null;
        try {
            storage.prepareDataStructure();
        } catch (Throwable localThrowable1) {
            localThrowable2 = localThrowable1;
            throw localThrowable1;
        } finally {
            if (storage != null) if (localThrowable2 != null) try {
                storage.close();
            } catch (Throwable x2) {
                localThrowable2.addSuppressed(x2);
            }
            else storage.close();
        }
        Thread persistThread = new Thread(this.queueConsumer, "ServerSigns-TaskPersistence");
        persistThread.start();
    }

    public IDataStorageAccessor newDataStorageAccessor() throws Exception {
        return new SQLiteDataStorageAccessor(this.databaseUrl);
    }

    public void addTask(PersistTask task) {
        this.tasksQueue.offer(task);
    }

    public void close() {
        this.queueConsumer.stop();
        this.tasksQueue.add(new PersistTask(PersistAction.STOP, null));
    }
}


