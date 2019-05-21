package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TaskSaver implements ISQLiteTaskSaver {
    private static final String SQL_INSERT_TASK = "INSERT INTO Task (TaskId, RunAt, Type, SubType, Data)\nVALUES (?, ?, ?, ?, ?)";
    private PreparedStatement statement;

    public void saveTask(Connection connection, TaskManagerTask task, List<ISQLiteTaskSaver> closeChain) throws SQLException {
        if (this.statement == null) {
            this.statement = connection.prepareStatement(SQL_INSERT_TASK);
            closeChain.add(this);
        }

        this.statement.setLong(1, task.getTaskID());
        this.statement.setLong(2, task.getRunAt());
        this.statement.setString(3, task.getTaskType().name());
        this.statement.setString(4, task.getSubType().name());
        this.statement.setString(5, task.getData());
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


