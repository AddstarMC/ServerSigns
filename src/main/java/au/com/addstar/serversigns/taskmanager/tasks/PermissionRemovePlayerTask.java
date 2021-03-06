package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.ServerSignsPlugin;

import java.util.UUID;

import static au.com.addstar.serversigns.taskmanager.tasks.PermissionRemovePlayerTaskType.PERMISSION_GRANT_REMOVE;
import static au.com.addstar.serversigns.taskmanager.tasks.PermissionRemovePlayerTaskType.PERMISSION_REMOVE;

public class PermissionRemovePlayerTask extends PlayerTask<PermissionRemovePlayerTaskType> {
    private PermissionGrantPlayerTask permissionGrantTask;

    public PermissionRemovePlayerTask(long runAt, UUID playerUniqueId, PermissionGrantPlayerTask permissionGrantTask, boolean alwaysPersisted) {
        super(runAt, PERMISSION_GRANT_REMOVE, String.valueOf(permissionGrantTask.getTaskType()), playerUniqueId, alwaysPersisted);
        this.permissionGrantTask = permissionGrantTask;
    }

    public PermissionRemovePlayerTask(long runAt, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(runAt, PERMISSION_REMOVE, action, playerUniqueId, alwaysPersisted);
    }

    protected PermissionRemovePlayerTask(long taskID, long runAt, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(taskID, runAt, PERMISSION_REMOVE, action, playerUniqueId, alwaysPersisted);
    }

    protected PermissionRemovePlayerTask(long taskID, long runAt, UUID playerUniqueId, PermissionGrantPlayerTask permissionGrantTask, boolean alwaysPersisted) {
        super(taskID, runAt, PERMISSION_GRANT_REMOVE, String.valueOf(permissionGrantTask.getTaskType()), playerUniqueId, alwaysPersisted);
        this.permissionGrantTask = permissionGrantTask;
    }

    protected TaskStatus runPlayerTask(ServerSignsPlugin plugin, org.bukkit.entity.Player player) {
        if ((plugin.hookManager.vault.isHooked()) && (plugin.hookManager.vault.getHook().hasPermissions())) {
            switch (getSubType()) {
                case PERMISSION_GRANT_REMOVE:
                    if (this.permissionGrantTask.isPermissionChanged()) {
                        plugin.hookManager.vault.getHook().getPermission().playerRemove(player, this.permissionGrantTask.getData());
                    }
                    break;
                case PERMISSION_REMOVE:
                    plugin.hookManager.vault.getHook().getPermission().playerRemove(player, getData());
            }

        }
        return TaskStatus.SUCCESS;
    }

    public TaskType getTaskType() {
        return TaskType.PERMISSION_REMOVE;
    }
}


