package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.util.Collection;

public interface IDataStorageAccessor
        extends AutoCloseable {
    void prepareDataStructure()
            throws Exception;

    Collection<TaskManagerTask> loadTasks(ServerSignsPlugin paramServerSignsPlugin)
            throws Exception;

    void saveTask(TaskManagerTask paramTaskManagerTask)
            throws Exception;

    void deleteTask(TaskManagerTask paramTaskManagerTask)
            throws Exception;
}
