package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.datastorage.ISQLiteTaskLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class PermissionGrantPlayerTaskLoader implements ISQLiteTaskLoader<PermissionGrantPlayerTask> {
    public PermissionGrantPlayerTask getTaskFromCurrentRow(ResultSet resultSet, Map<Long, TaskManagerTask> loadedTasks) throws SQLException {
        return new PermissionGrantPlayerTask(resultSet.getLong(1), resultSet.getLong(2), resultSet.getString(5), UUID.fromString(resultSet.getString(6)), true);
    }
}
