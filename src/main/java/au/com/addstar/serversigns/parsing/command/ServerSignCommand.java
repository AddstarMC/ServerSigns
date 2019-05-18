package au.com.addstar.serversigns.parsing.command;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.tasks.*;
import au.com.addstar.serversigns.utils.NumberUtils;
import au.com.addstar.serversigns.utils.StringUtils;
import au.com.addstar.serversigns.parsing.CommandType;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

public class ServerSignCommand implements java.io.Serializable {
    private static final Pattern RANDOM_PATTERN = Pattern.compile("<r:(-?\\d+)-(-?\\d+)>");
    protected CommandType type;
    protected String command;
    private long delay = 0L;
    private boolean alwaysPersisted = false;
    private int interactValue = 0;
    private List<String> grant = new ArrayList();

    public ServerSignCommand(CommandType type, String command) {
        this.type = type;
        this.command = command;
    }

    public int getInteractValue() {
        return this.interactValue;
    }

    public void setInteractValue(int val) {
        if ((val < 0) || (val > 2)) throw new IllegalArgumentException("Value cannot be < 0 or > 2");
        this.interactValue = val;
    }

    public boolean isAlwaysPersisted() {
        return this.alwaysPersisted;
    }

    public void setAlwaysPersisted(boolean val) {
        this.alwaysPersisted = val;
    }

    public CommandType getType() {
        return this.type;
    }

    public List<String> getGrantPermissions() {
        return this.grant;
    }

    public void setGrantPermissions(List<String> grant) {
        this.grant = grant;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getUnformattedCommand() {
        return this.command;
    }

    public String getFormattedCommand(Player executor, ServerSignsPlugin plugin, Map<String, String> injectedReplacements) {
        String ret = this.command;
        if (executor != null) {
            try {
                if (ret.contains("<group>")) {
                    ret = ret.replaceAll("<group>", plugin.hookManager.vault.getHook().getPermission().getPrimaryGroup(executor));
                }
            } catch (Throwable thrown) {
                ServerSignsPlugin.log("Vault is not properly hooked into Permissions! Unable to parse <group> parameters");
            }

            ret = ret.replaceAll("<player>", executor.getName());
            ret = ret.replaceAll("<name>", executor.getName());
            ret = ret.replaceAll("<uuid>", executor.getUniqueId().toString());
        }

        if (injectedReplacements != null) {
            for (Map.Entry<String, String> entry : injectedReplacements.entrySet()) {
                ret = Pattern.compile(entry.getKey(), 2).matcher(ret).replaceAll(Matcher.quoteReplacement(entry.getValue()));
            }
        }

        ret = ret.trim();
        if (ret.startsWith("/")) {
            ret = ret.replaceFirst("/", "");
        }

        return StringUtils.colour(formatRandoms(ret));
    }

    private String formatRandoms(String input) {
        Matcher matcher;
        while ((matcher = RANDOM_PATTERN.matcher(input)).find()) {
            int random = NumberUtils.randomBetweenInclusive(NumberUtils.parseInt(matcher.group(1)), NumberUtils.parseInt(matcher.group(2)));
            input = matcher.replaceFirst(random + "");
        }

        return input;
    }

    public List<TaskManagerTask> getTasks(Player executor, ServerSignsPlugin plugin, Map<String, String> injectedReplacements) {
        List<TaskManagerTask> tasks = new ArrayList();
        List<PermissionGrantPlayerTask> grantTasks = new ArrayList();

        if (executor != null) {
            for (String perm : getGrantPermissions()) {
                PermissionGrantPlayerTask grantTask = new PermissionGrantPlayerTask(getTimestamp(), perm, executor.getUniqueId(), isAlwaysPersisted());
                tasks.add(grantTask);
                grantTasks.add(grantTask);
            }
        }

        Object taskObj = getType().getTaskObject();
        if ((getType() == CommandType.SERVER_COMMAND) && (org.apache.commons.lang.StringUtils.containsIgnoreCase(getUnformattedCommand(), "<player>"))) {
            taskObj = PlayerActionTaskType.SERVER_COMMAND;
        }

        if (((taskObj instanceof PlayerActionTaskType)) && (executor != null)) {
            tasks.add(new PlayerActionTask(getTimestamp(), (PlayerActionTaskType) taskObj, getFormattedCommand(executor, plugin, injectedReplacements), executor.getUniqueId(), isAlwaysPersisted()));
        } else if ((taskObj instanceof ServerActionTaskType)) {
            tasks.add(new ServerActionTask(getTimestamp(), (ServerActionTaskType) taskObj, getFormattedCommand(executor, plugin, injectedReplacements), isAlwaysPersisted()));
        } else if (((taskObj instanceof PermissionGrantPlayerTaskType)) && (executor != null)) {
            tasks.add(new PermissionGrantPlayerTask(getTimestamp(), getFormattedCommand(executor, plugin, injectedReplacements), executor.getUniqueId(), isAlwaysPersisted()));
        } else if (((taskObj instanceof PermissionRemovePlayerTaskType)) && (executor != null)) {
            tasks.add(new PermissionRemovePlayerTask(getTimestamp(), getFormattedCommand(executor, plugin, injectedReplacements), executor.getUniqueId(), isAlwaysPersisted()));
        }

        if ((executor != null) && (!grantTasks.isEmpty())) {
            for (PermissionGrantPlayerTask grantTask : grantTasks) {
                tasks.add(new PermissionRemovePlayerTask(getTimestamp(), executor.getUniqueId(), grantTask, isAlwaysPersisted()));
            }
        }

        return tasks;
    }

    private long getTimestamp() {
        if (getDelay() == 0L) return 0L;
        return System.currentTimeMillis() + getDelay() * 1000L;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\command\ServerSignCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */