package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.ServerSignsPlugin;

import java.util.UUID;

public class PermissionRemovePlayerTask extends PlayerTask<PermissionRemovePlayerTaskType> {
    private PermissionGrantPlayerTask permissionGrantTask;

    public PermissionRemovePlayerTask(long runAt, UUID playerUniqueId, PermissionGrantPlayerTask permissionGrantTask, boolean alwaysPersisted) {
        super(runAt, PermissionRemovePlayerTaskType.PERMISSION_GRANT_REMOVE, String.valueOf(permissionGrantTask.getTaskType()), playerUniqueId, alwaysPersisted);
        this.permissionGrantTask = permissionGrantTask;
    }

    public PermissionRemovePlayerTask(long runAt, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(runAt, PermissionRemovePlayerTaskType.PERMISSION_REMOVE, action, playerUniqueId, alwaysPersisted);
    }

    protected PermissionRemovePlayerTask(long taskID, long runAt, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(taskID, runAt, PermissionRemovePlayerTaskType.PERMISSION_REMOVE, action, playerUniqueId, alwaysPersisted);
    }

    protected PermissionRemovePlayerTask(long taskID, long runAt, UUID playerUniqueId, PermissionGrantPlayerTask permissionGrantTask, boolean alwaysPersisted) {
        super(taskID, runAt, PermissionRemovePlayerTaskType.PERMISSION_GRANT_REMOVE, String.valueOf(permissionGrantTask.getTaskType()), playerUniqueId, alwaysPersisted);
        this.permissionGrantTask = permissionGrantTask;
    }

    protected TaskStatus runPlayerTask(ServerSignsPlugin plugin, org.bukkit.entity.Player player) {
        if ((plugin.hookManager.vault.isHooked()) && (plugin.hookManager.vault.getHook().hasPermissions())) {
            switch (getSubType()) {
                case PermissionRemovePlayerTaskType.PERMISSION_GRANT_REMOVE:
                    if (this.permissionGrantTask.isPermissionChanged()) {
                        plugin.hookManager.vault.getHook().getPermission().playerRemove(player, this.permissionGrantTask.getData());
                    }
                    break;
                case PermissionRemovePlayerTaskType.PERMISSION_REMOVE:
                    plugin.hookManager.vault.getHook().getPermission().playerRemove(player, getData());
            }

        }
        return TaskStatus.SUCCESS;
    }

    public TaskType getTaskType() {
        return TaskType.PERMISSION_REMOVE;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\tasks\PermissionRemovePlayerTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */