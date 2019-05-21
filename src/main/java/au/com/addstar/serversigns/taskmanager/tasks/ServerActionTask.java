package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

public class ServerActionTask extends TaskManagerTask<ServerActionTaskType> {
    public ServerActionTask(long runAt, ServerActionTaskType actionType, String action, boolean alwaysPersisted) {
        super(runAt, actionType, action, alwaysPersisted);
    }

    protected ServerActionTask(long taskID, long runAt, ServerActionTaskType actionType, String action, boolean alwaysPersisted) {
        super(taskID, runAt, actionType, action, alwaysPersisted);
    }

    public TaskStatus runTask(ServerSignsPlugin plugin) {
        plugin.debug("Running Task:" +this.getClass().getSimpleName()+" : "+getSubType().toString()+
                " : "+getData());
        switch (getSubType()) {
            case COMMAND:
                plugin.serverCommand(getData());
                break;
            case BROADCAST:
                org.bukkit.Bukkit.broadcastMessage(getData());
        }


        return TaskStatus.SUCCESS;
    }

    public TaskType getTaskType() {
        return TaskType.SERVER;
    }
}


