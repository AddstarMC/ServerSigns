package au.com.addstar.serversigns.taskmanager;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.utils.CurrentTime;
import au.com.addstar.serversigns.taskmanager.tasks.TaskStatus;
import au.com.addstar.serversigns.taskmanager.tasks.TaskType;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class TaskManagerTask<T extends Enum<T>> implements Delayed {
    private final T subType;
    private final String data;
    private final long runAt;
    private long taskID;
    private boolean persisted;
    private boolean alwaysPersisted;

    protected TaskManagerTask(long taskID, long runAt, T subType, String data, boolean alwaysPersisted) {
        this.subType = subType;
        this.taskID = taskID;
        this.runAt = runAt;
        this.data = data;
        this.persisted = true;
        this.alwaysPersisted = alwaysPersisted;
    }

    public TaskManagerTask(long runAt, T subType, String data, boolean alwaysPersisted) {
        this.runAt = runAt;
        this.subType = subType;
        this.data = data;
        this.alwaysPersisted = alwaysPersisted;
    }

    public boolean isAlwaysPersisted() {
        return this.alwaysPersisted;
    }

    public void setAlwaysPersisted(boolean val) {
        this.alwaysPersisted = val;
    }

    public long getTaskID() {
        return this.taskID;
    }

    void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public long getRunAt() {
        return this.runAt;
    }

    public boolean isPersisted() {
        return this.persisted;
    }

    void setPersisted(boolean persisted) {
        this.persisted = persisted;
    }

    public T getSubType() {
        return this.subType;
    }

    public String getData() {
        return this.data;
    }

    public abstract TaskStatus runTask(ServerSignsPlugin paramServerSignsPlugin);

    public abstract TaskType getTaskType();

    public long getDelay(TimeUnit unit) {
        return getRunAt() > 0L ? unit.convert(getRunAt() - CurrentTime.get(), TimeUnit.MILLISECONDS) : 0L;
    }

    public int compareTo(Delayed o) {
        TaskManagerTask that = (TaskManagerTask) o;
        return getTaskID() < that.getTaskID() ? -1 : getRunAt() > that.getRunAt() ? 1 : getRunAt() < that.getRunAt() ? -1 : 1;
    }
}
