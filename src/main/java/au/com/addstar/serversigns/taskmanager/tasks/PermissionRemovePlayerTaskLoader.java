package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.datastorage.ISQLiteTaskLoader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class PermissionRemovePlayerTaskLoader implements ISQLiteTaskLoader<PermissionRemovePlayerTask> {
    public PermissionRemovePlayerTask getTaskFromCurrentRow(ResultSet resultSet, Map<Long, TaskManagerTask> loadedTasks) throws SQLException {
        PermissionRemovePlayerTaskType subType = PermissionRemovePlayerTaskType.valueOf(resultSet.getString(4));
        switch (subType) {
            case PERMISSION_REMOVE:
                return new PermissionRemovePlayerTask(resultSet.getLong(1), resultSet.getLong(2), resultSet.getString(5), UUID.fromString(resultSet.getString(6)), true);


            case PERMISSION_GRANT_REMOVE:
                long grantTaskId = Long.parseLong(resultSet.getString(5));
                TaskManagerTask grantTask = loadedTasks.get(Long.valueOf(grantTaskId));
                if ((grantTask != null) && ((grantTask instanceof PermissionGrantPlayerTask))) {
                    return new PermissionRemovePlayerTask(resultSet.getLong(1), resultSet.getLong(2), UUID.fromString(resultSet.getString(6)), (PermissionGrantPlayerTask) grantTask, true);
                }


                break;
        }


        return null;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\tasks\PermissionRemovePlayerTaskLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */