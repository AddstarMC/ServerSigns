package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ISQLiteTaskSaver {
    void saveTask(Connection paramConnection, TaskManagerTask paramTaskManagerTask, List<ISQLiteTaskSaver> paramList)
            throws SQLException;

    void close()
            throws SQLException;
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\datastorage\ISQLiteTaskSaver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */