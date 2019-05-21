package au.com.addstar.serversigns.parsing;

import au.com.addstar.serversigns.taskmanager.tasks.PermissionRemovePlayerTaskType;
import au.com.addstar.serversigns.taskmanager.tasks.PermissionGrantPlayerTaskType;
import au.com.addstar.serversigns.taskmanager.tasks.PlayerActionTaskType;
import au.com.addstar.serversigns.taskmanager.tasks.ServerActionTaskType;

public enum CommandType {
    SERVER_COMMAND(ServerActionTaskType.COMMAND),
    BROADCAST(ServerActionTaskType.BROADCAST),
    CHAT(PlayerActionTaskType.CHAT),
    MESSAGE(PlayerActionTaskType.MESSAGE),
    BLANK_MESSAGE(PlayerActionTaskType.BLANK_MESSAGE),
    PLAYER_COMMAND(PlayerActionTaskType.COMMAND),
    OP_COMMAND(PlayerActionTaskType.OP_COMMAND),
    PERMISSION_GRANT(PermissionGrantPlayerTaskType.PERMISSION_GRANT),
    PERMISSION_REMOVE(PermissionRemovePlayerTaskType.PERMISSION_REMOVE),
    ADD_GROUP(PlayerActionTaskType.ADD_GROUP),
    DEL_GROUP(PlayerActionTaskType.DEL_GROUP),
    CONDITIONAL_IF(null),
    CONDITIONAL_ENDIF(null),
    RETURN(null),
    CANCEL_TASKS(PlayerActionTaskType.CANCEL_TASKS),
    DISPLAY_OPTIONS(null);

    private Object taskTypeObj;

    CommandType(Object taskObj) {
        this.taskTypeObj = taskObj;
    }

    public Object getTaskObject() {
        return this.taskTypeObj;
    }
}


