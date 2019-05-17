package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.datastorage.ISQLiteTaskLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class PlayerActionTaskLoader implements ISQLiteTaskLoader<PlayerActionTask> {
    public PlayerActionTask getTaskFromCurrentRow(ResultSet resultSet, Map<Long, TaskManagerTask> loadedTasks) throws SQLException {
        return new PlayerActionTask(resultSet.getLong(1), resultSet.getLong(2), PlayerActionTaskType.valueOf(resultSet.getString(4)), resultSet.getString(5), UUID.fromString(resultSet.getString(6)), true);
    }
}