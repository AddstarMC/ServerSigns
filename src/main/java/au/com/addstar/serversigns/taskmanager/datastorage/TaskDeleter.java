package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TaskDeleter implements ISQLiteTaskDeleter {
    private static final String SQL_DELETE_TASK = "DELETE FROM Task WHERE TaskId IS ?";
    private PreparedStatement statement;

    public void deleteTask(Connection connection, TaskManagerTask task, List<ISQLiteTaskDeleter> closeChain) throws SQLException {
        if (this.statement == null) {
            this.statement = connection.prepareStatement("DELETE FROM Task WHERE TaskId IS ?");
            closeChain.add(this);
        }

        this.statement.setLong(1, task.getTaskID());
        this.statement.execute();
    }

    public void close() throws SQLException {
        try {
            this.statement.close();
        } finally {
            this.statement = null;
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\datastorage\TaskDeleter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */