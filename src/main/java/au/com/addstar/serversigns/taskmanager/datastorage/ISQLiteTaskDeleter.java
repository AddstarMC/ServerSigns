package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ISQLiteTaskDeleter {
    void deleteTask(Connection paramConnection, TaskManagerTask paramTaskManagerTask, List<ISQLiteTaskDeleter> paramList)
            throws SQLException;

    void close()
            throws SQLException;
}
