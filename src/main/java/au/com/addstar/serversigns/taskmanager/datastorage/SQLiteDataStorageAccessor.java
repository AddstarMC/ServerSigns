package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.tasks.PermissionGrantPlayerTaskLoader;
import au.com.addstar.serversigns.taskmanager.tasks.PermissionRemovePlayerTaskLoader;
import au.com.addstar.serversigns.taskmanager.tasks.PlayerActionTaskLoader;
import au.com.addstar.serversigns.taskmanager.tasks.ServerActionTaskLoader;
import au.com.addstar.serversigns.taskmanager.tasks.TaskType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;


public class SQLiteDataStorageAccessor
        implements IDataStorageAccessor {
    private static final String SQL_CREATE_TABLE_TASK = "CREATE TABLE IF NOT EXISTS Task(TaskId INTEGER PRIMARY KEY NOT NULL,RunAt INTEGER NOT NULL,Type TEXT NOT NULL,SubType TEXT NOT NULL,Data TEXT NOT NULL)";
    private static final String SQL_CREATE_TABLE_PLAYER_TASK = "CREATE TABLE IF NOT EXISTS PlayerTask(TaskId INTEGER NOT NULL,PlayerUniqueId TEXT NOT NULL,FOREIGN KEY (TaskId) REFERENCES Task(TaskId))";
    private static final String SQL_SELECT_ALL = "SELECT Task.TaskId, Task.RunAt, Task.Type, Task.SubType, Task.Data, PlayerTask.PlayerUniqueId FROM Task LEFT JOIN PlayerTask ON Task.TaskId = PlayerTask.TaskId";
    private static final Map<TaskType, ISQLiteTaskLoader> TASK_LOADERS = new HashMap<>();
    private static final Map<TaskType, ISQLiteTaskSaver> TASK_SAVERS;
    private static final Map<TaskType, ISQLiteTaskDeleter> TASK_DELETER;

    static {

        TASK_LOADERS.put(TaskType.SERVER, new ServerActionTaskLoader());
        TASK_LOADERS.put(TaskType.PLAYER, new PlayerActionTaskLoader());
        TASK_LOADERS.put(TaskType.PERMISSION_GRANT, new PermissionGrantPlayerTaskLoader());
        TASK_LOADERS.put(TaskType.PERMISSION_REMOVE, new PermissionRemovePlayerTaskLoader());

        TASK_SAVERS = new HashMap<>();
        TaskSaver taskSaver = new TaskSaver();
        PlayerTaskSaver playerTaskSaver = new PlayerTaskSaver(taskSaver);

        TASK_SAVERS.put(TaskType.SERVER, taskSaver);
        TASK_SAVERS.put(TaskType.PLAYER, playerTaskSaver);
        TASK_SAVERS.put(TaskType.PERMISSION_GRANT, playerTaskSaver);
        TASK_SAVERS.put(TaskType.PERMISSION_REMOVE, playerTaskSaver);

        TASK_DELETER = new HashMap<>();
        TaskDeleter taskDeleter = new TaskDeleter();
        PlayerTaskDeleter playerTaskDeleter = new PlayerTaskDeleter(taskDeleter);
        TASK_DELETER.put(TaskType.SERVER, taskDeleter);
        TASK_DELETER.put(TaskType.PLAYER, playerTaskDeleter);
        TASK_DELETER.put(TaskType.PERMISSION_GRANT, playerTaskDeleter);
        TASK_DELETER.put(TaskType.PERMISSION_REMOVE, playerTaskDeleter);
    }

    private final Connection connection;
    private List<ISQLiteTaskSaver> saveCloseChain;
    private List<ISQLiteTaskDeleter> deleteCloseChain;


    SQLiteDataStorageAccessor(String databaseUrl)
            throws SQLException {
        this.connection = DriverManager.getConnection(databaseUrl);
        this.connection.setAutoCommit(false);
    }

    public void prepareDataStructure() throws Exception {
        Statement s = this.connection.createStatement();
        Throwable localThrowable2 = null;
        try {
            s.execute(SQL_CREATE_TABLE_TASK);
            s.execute(SQL_CREATE_TABLE_PLAYER_TASK);
            s.execute("PRAGMA foreign_keys = ON");
        } catch (Throwable localThrowable1) {
            localThrowable2 = localThrowable1;
            throw localThrowable1;
        } finally {
            if (s != null) {
                if (localThrowable2 != null) try {
                    s.close();
                } catch (Throwable x2) {
                    localThrowable2.addSuppressed(x2);
                }
                else {
                    s.close();
                }
            }
        }
    }

    private String getSqlRowString(ResultSet resultSet)
            throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            stringBuilder.append(metaData.getColumnLabel(i)).append(": ").append(resultSet.getObject(i)).append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return stringBuilder.toString();
    }

    public void saveTask(TaskManagerTask task) throws Exception {
        if (this.saveCloseChain == null) {
            this.saveCloseChain =  new ArrayList<>();
        }

        ISQLiteTaskSaver taskSaver = TASK_SAVERS.get(task.getTaskType());
        try {
            taskSaver.saveTask(this.connection, task, this.saveCloseChain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(TaskManagerTask task) throws Exception {
        if (this.deleteCloseChain == null) {
            this.deleteCloseChain =  new ArrayList<>();
        }

        ISQLiteTaskDeleter taskDeleter = TASK_DELETER.get(task.getTaskType());
        try {
            taskDeleter.deleteTask(this.connection, task, this.deleteCloseChain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        Exception thrownException = null;

        if (this.saveCloseChain != null) {
            for (ISQLiteTaskSaver taskSaver : this.saveCloseChain) {
                try {
                    taskSaver.close();
                } catch (Exception e) {
                    thrownException = e;
                }
            }
        }

        if (this.deleteCloseChain != null) {
            for (ISQLiteTaskDeleter taskDeleter : this.deleteCloseChain) {
                try {
                    taskDeleter.close();
                } catch (Exception e) {
                    thrownException = e;
                }
            }
        }

        if (this.connection != null) {
            try {
                try {
                    this.connection.commit();
                } finally {
                    this.connection.close();
                }
            } catch (Exception e) {
                thrownException = e;
            }
        }

        if (thrownException != null) {
            throw thrownException;
        }
    }

    public Collection<TaskManagerTask> loadTasks(ServerSignsPlugin plugin) throws SQLException {
        try (
                Statement s = this.connection.createStatement();
                ResultSet resultSet = s.executeQuery(SQL_SELECT_ALL)
        ) {
            Map<Long, TaskManagerTask> loadedTasks = new TreeMap<>();
            while (resultSet.next()) {
                try {
                    TaskType taskType = TaskType.valueOf(resultSet.getString(3));
                    ISQLiteTaskLoader taskLoader = TASK_LOADERS.get(taskType);
                    if (taskLoader != null) {
                        TaskManagerTask task = taskLoader.getTaskFromCurrentRow(resultSet, loadedTasks);
                        if (task != null) {
                            loadedTasks.put(task.getTaskID(), task);
                            continue;
                        }
                    }
                    plugin.getLogger().log(Level.WARNING, "Could not load task (" + getSqlRowString(resultSet) + ")");
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Exception while loading task", e);
                }
            }
            return loadedTasks.values();
        }
    }


}


