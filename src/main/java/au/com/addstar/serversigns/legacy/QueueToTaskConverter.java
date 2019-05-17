package au.com.addstar.serversigns.legacy;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.taskmanager.tasks.*;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;
import au.com.addstar.serversigns.taskmanager.tasks.PlayerActionTask;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class QueueToTaskConverter {
    public static QueueToTaskResult convertFile(Path dataFolder) {
        List<TaskManagerTask> converted = new ArrayList();
        long yamlCurrentId = -1L;

        Path queueYml = dataFolder.resolve("taskQueue.yml");
        if (Files.exists(queueYml)) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(queueYml.toFile());
            yamlCurrentId = yaml.getInt("currentTaskID");
            processSectionUpdate(yaml.getConfigurationSection("tasks"), converted);
        }

        Path playerTasks = dataFolder.resolve("playerJoinTasks.yml");
        if (Files.exists(playerTasks)) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(playerTasks.toFile());
            processSectionUpdate(yaml.getConfigurationSection("tasks"), converted);
        }

        try {
            Files.deleteIfExists(queueYml);
            Files.deleteIfExists(playerTasks);
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }

        if ((converted.size() > 0) && (yamlCurrentId > 0L)) {
            ServerSignsPlugin.log("Successfully converted " + converted.size() + " tasks to the new TaskManager system and set currentId to " + (yamlCurrentId + 1L));
            return new QueueToTaskResult(yamlCurrentId + 1L, converted);
        }

        return null;
    }

    private static void processSectionUpdate(ConfigurationSection section, List<TaskManagerTask> tasks) {
        if (section != null) {
            for (String taskId : section.getKeys(false)) {
                try {
                    subSection = section.getConfigurationSection(taskId);
                    timestamp = subSection.getLong("timestamp");
                    String type = subSection.getString("type");

                    switch (type) {
                        case "playerCommand":
                            tasks.add(new PlayerActionTask(timestamp, PlayerActionTaskType.COMMAND, subSection.getString("command"), UUID.fromString(subSection.getString("playerUUID")), true));
                            break;
                        case "playerMessage":
                            tasks.add(new PlayerActionTask(timestamp, subSection.getBoolean("blankMessage") ? PlayerActionTaskType.BLANK_MESSAGE : PlayerActionTaskType.MESSAGE, subSection.getString("message"), UUID.fromString(subSection.getString("playerUUID")), true));
                            break;
                        case "playerChat":
                            tasks.add(new PlayerActionTask(timestamp, subSection.getBoolean("op") ? PlayerActionTaskType.OP_COMMAND : PlayerActionTaskType.CHAT, subSection.getString("message"), UUID.fromString(subSection.getString("playerUUID")), true));
                            break;
                        case "serverCommand":
                            tasks.add(new ServerActionTask(timestamp, ServerActionTaskType.COMMAND, subSection.getString("command"), true));
                            break;
                        case "serverMessage":
                            tasks.add(new ServerActionTask(timestamp, ServerActionTaskType.BROADCAST, subSection.getString("message"), true));
                            break;

                        case "permissionGrant":
                            ArrayList<String> permissions = (ArrayList) subSection.get("permissions");

                            for (String perm : permissions) {
                                tasks.add(new PermissionGrantPlayerTask(timestamp, perm, UUID.fromString(subSection.getString("playerUUID")), true));
                            }
                            break;
                        case "permissionRemove":
                            if (subSection.contains("changedPermissions")) {
                                ArrayList<String> changedPermissions = (ArrayList) subSection.get("changedPermissions");

                                for (String perm : changedPermissions)
                                    tasks.add(new PermissionRemovePlayerTask(timestamp, perm, UUID.fromString(subSection.getString("playerUUID")), true));
                            }
                            break;
                    }
                } catch (Throwable thrown) {
                    ConfigurationSection subSection;
                    long timestamp;
                    ServerSignsPlugin.log("Unable to convert task (id " + taskId + ") as the data is too old to reliably update.");
                }
            }
        }
    }

    public static class QueueToTaskResult {
        private long highestId;
        private List<TaskManagerTask> tasks;

        public QueueToTaskResult(long highestId, List<TaskManagerTask> tasks) {
            this.highestId = highestId;
            this.tasks = tasks;
        }

        public long getHighestId() {
            return this.highestId;
        }

        public List<TaskManagerTask> getTasks() {
            return this.tasks;
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\legacy\QueueToTaskConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */