package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class PlayerTaskDeleter implements ISQLiteTaskDeleter {
    private static final String SQL_DELETE_PLAYER_TASK = "DELETE FROM PlayerTask WHERE TaskId IS ?";
    private final TaskDeleter taskDeleter;
    private PreparedStatement statement;

    public PlayerTaskDeleter(TaskDeleter taskDeleter) {
        this.taskDeleter = taskDeleter;
    }

    public void deleteTask(Connection connection, TaskManagerTask task, List<ISQLiteTaskDeleter> closeChain) throws java.sql.SQLException {
        this.taskDeleter.deleteTask(connection, task, closeChain);

        if (this.statement == null) {
            this.statement = connection.prepareStatement("DELETE FROM PlayerTask WHERE TaskId IS ?");
            closeChain.add(this);
        }

        this.statement.setLong(1, task.getTaskID());
        this.statement.execute();
    }

    public void close() throws java.sql.SQLException {
        try {
            this.statement.close();
        } finally {
            this.statement = null;
        }
    }
}


