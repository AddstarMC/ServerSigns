package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.tasks.PlayerTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PlayerTaskSaver implements ISQLiteTaskSaver {
    private static final String SQL_INSERT_PLAYER_TASK = "INSERT INTO `PlayerTask`(`TaskId`, `PlayerUniqueId`) VALUES (?, ?)";
    private final TaskSaver taskSaver;
    private PreparedStatement statement;

    public PlayerTaskSaver(TaskSaver taskSaver) {
        this.taskSaver = taskSaver;
    }

    public void saveTask(Connection connection, TaskManagerTask task, List<ISQLiteTaskSaver> closeChain) throws SQLException {
        PlayerTask playerTask = (PlayerTask) task;
        this.taskSaver.saveTask(connection, task, closeChain);

        if (this.statement == null) {
            this.statement = connection.prepareStatement(SQL_INSERT_PLAYER_TASK);
            closeChain.add(this);
        }

        this.statement.setLong(1, playerTask.getTaskID());
        this.statement.setString(2, playerTask.getPlayerUniqueId().toString());
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


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\datastorage\PlayerTaskSaver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */