package au.com.addstar.serversigns.taskmanager.datastorage;

import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

public class PersistTask {
    private final PersistAction persistAction;
    private final TaskManagerTask task;

    public PersistTask(PersistAction persistAction, TaskManagerTask task) {
        this.persistAction = persistAction;
        this.task = task;
    }

    public PersistAction getPersistAction() {
        return this.persistAction;
    }

    public TaskManagerTask getTask() {
        return this.task;
    }
}