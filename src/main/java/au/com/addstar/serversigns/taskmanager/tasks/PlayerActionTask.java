package au.com.addstar.serversigns.taskmanager.tasks;

import au.com.addstar.serversigns.ServerSignsPlugin;
import org.bukkit.entity.Player;

public class PlayerActionTask extends PlayerTask<PlayerActionTaskType> {
    public PlayerActionTask(long runAt, PlayerActionTaskType actionType, String action, java.util.UUID playerUniqueId, boolean alwaysPersisted) {
        super(runAt, actionType, action, playerUniqueId, alwaysPersisted);
    }

    protected PlayerActionTask(long taskID, long runAt, PlayerActionTaskType type, String action, java.util.UUID playerUniqueId, boolean alwaysPersisted) {
        super(taskID, runAt, type, action, playerUniqueId, alwaysPersisted);
    }

    private void performCommand(ServerSignsPlugin plugin, Player player) {
        if (plugin.getServerSignsConfig().getAlternateCommandDispatching()) {
            player.chat("/" + getData());
        } else {
            player.performCommand(getData());
        }
    }

    protected TaskStatus runPlayerTask(ServerSignsPlugin plugin, Player player) {
        switch (getSubType()) {
            case SERVER_COMMAND:
                plugin.serverCommand(getData());
                break;
            case CHAT:
                player.chat(getData());
                break;
            case MESSAGE:
                plugin.send(player, getData());
                break;
            case BLANK_MESSAGE:
                plugin.sendBlank(player, getData());
                break;

            case COMMAND:
            case OP_COMMAND:
                boolean changedOp = false;
                try {
                    if ((getSubType() == PlayerActionTaskType.OP_COMMAND) && (!player.isOp())) {
                        changedOp = true;
                        player.setOp(true);
                    }

                    performCommand(plugin, player);
                } finally {
                    if (changedOp) {
                        player.setOp(false);
                    }
                }
                break;

            case ADD_GROUP:
            case DEL_GROUP:
                if ((plugin.hookManager.vault.isHooked()) && (plugin.hookManager.vault.getHook().hasPermissions())) {
                    if (getSubType() == PlayerActionTaskType.ADD_GROUP) {
                        plugin.hookManager.vault.getHook().getPermission().playerAddGroup(player, getData());
                    } else {
                        plugin.hookManager.vault.getHook().getPermission().playerRemoveGroup(player, getData());
                    }
                }

                break;
            case CANCEL_TASKS:
                int removed = plugin.taskManager.removePlayerTasks(player.getUniqueId(), getData().isEmpty() ? null : java.util.regex.Pattern.compile(getData()));
                if (removed > 0) {
                    ServerSignsPlugin.log("Successfully removed " + removed + " player tasks from the queue.");
                }
                break;
        }

        return TaskStatus.SUCCESS;
    }

    public TaskType getTaskType() {
        return TaskType.PLAYER;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\taskmanager\tasks\PlayerActionTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */