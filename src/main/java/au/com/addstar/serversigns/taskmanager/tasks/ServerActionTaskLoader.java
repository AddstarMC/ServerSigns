package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.datastorage.ISQLiteTaskLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ServerActionTaskLoader implements ISQLiteTaskLoader<ServerActionTask> {
    public ServerActionTask getTaskFromCurrentRow(ResultSet resultSet, Map<Long, TaskManagerTask> loadedTasks) throws SQLException {
        return new ServerActionTask(resultSet.getLong(1), resultSet.getLong(2), ServerActionTaskType.valueOf(resultSet.getString(4)), resultSet.getString(5), true);
    }
}