package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.ITaskExecutor;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.util.List;

public class PersistTaskExecutor implements ITaskExecutor<PersistTask> {
    private final IDataStorageHandler dataStorageHandler;

    public PersistTaskExecutor(IDataStorageHandler dataStorageHandler) {
        this.dataStorageHandler = dataStorageHandler;
    }

    public void runTasks(List<? extends PersistTask> tasks) {
        try {
            IDataStorageAccessor storage = this.dataStorageHandler.newDataStorageAccessor();
            Throwable localThrowable2 = null;
            try {
                for (PersistTask persistTask : tasks) {
                    PersistAction persistAction = persistTask.getPersistAction();
                    TaskManagerTask taskManagerTask = persistTask.getTask();

                    if (persistAction == PersistAction.SAVE) {
                        storage.saveTask(taskManagerTask);
                    } else if (persistAction == PersistAction.DELETE) {
                        storage.deleteTask(taskManagerTask);
                    }
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runTask(PersistTask task) {
        throw new UnsupportedOperationException();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\datastorage\PersistTaskExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */