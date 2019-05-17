package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface ISQLiteTaskLoader<T extends TaskManagerTask> {
    T getTaskFromCurrentRow(ResultSet paramResultSet, Map<Long, TaskManagerTask> paramMap)
            throws SQLException;
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\datastorage\ISQLiteTaskLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */