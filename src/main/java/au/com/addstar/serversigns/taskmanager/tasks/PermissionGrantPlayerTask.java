package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.ServerSignsPlugin;

import java.util.UUID;

public class PermissionGrantPlayerTask extends PlayerTask<PermissionGrantPlayerTaskType> {
    private boolean permissionChanged;

    public PermissionGrantPlayerTask(long runAt, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(runAt, PermissionGrantPlayerTaskType.PERMISSION_GRANT, action, playerUniqueId, alwaysPersisted);
    }

    protected PermissionGrantPlayerTask(long taskID, long runAt, String action, UUID playerUniqueId, boolean alwaysPersisted) {
        super(taskID, runAt, PermissionGrantPlayerTaskType.PERMISSION_GRANT, action, playerUniqueId, alwaysPersisted);
    }

    public TaskType getTaskType() {
        return TaskType.PERMISSION_GRANT;
    }

    public boolean isPermissionChanged() {
        return this.permissionChanged;
    }

    protected TaskStatus runPlayerTask(ServerSignsPlugin plugin, org.bukkit.entity.Player player) {
        if ((plugin.hookManager.vault.isHooked()) && (plugin.hookManager.vault.getHook().hasPermissions()) &&
                (!player.hasPermission(getData()))) {
            plugin.hookManager.vault.getHook().getPermission().playerAdd(player, getData());
            this.permissionChanged = true;
        }

        return TaskStatus.SUCCESS;
    }
}