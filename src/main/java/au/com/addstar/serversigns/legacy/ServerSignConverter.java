package au.com.addstar.serversigns.legacy;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.parsing.CommandParseException;
import au.com.addstar.serversigns.parsing.ServerSignCommandFactory;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.signs.ServerSignManager;
import au.com.addstar.serversigns.utils.NumberUtils;
import au.com.addstar.serversigns.utils.UUIDUpdateTask;
import au.com.addstar.serversigns.parsing.command.ServerSignCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

public class ServerSignConverter {
    public static final int FILE_VERSION = 7;
    private static int currentVersion = -1;
    private static long lastBackup = 0L;

    public static int getCurrentPersistVersion(Path signDirectory) throws IOException {
        Path verFile = signDirectory.resolve(".svs_persist_version");
        int currentVersion = 0;
        if (!Files.exists(verFile)) {
            Files.write(verFile, "7".getBytes(), StandardOpenOption.CREATE);
        } else {
            currentVersion = NumberUtils.parseInt(new String(Files.readAllBytes(verFile)), 0);
            if (currentVersion < 7) {
                Files.delete(verFile);
                Files.write(verFile, "7".getBytes(), StandardOpenOption.CREATE);
            }
        }
        return currentVersion;
    }

    public static Path performAllFileUpdates(Path signDirectory, Path signFile) throws IOException {
        if (currentVersion == -1) {
            currentVersion = getCurrentPersistVersion(signDirectory);
        }
        if (currentVersion <= 2) {
            createBackup(signDirectory);
            return updateMalformedFileName(updatePriceItemData(updateCommands(updatePermissions(signFile), signFile), signFile), signDirectory, signFile);
        }


        if (currentVersion <= 3) {
            createBackup(signDirectory);
            updatePriceItemData(updateCommands(updatePermissions(signFile), signFile), signFile);


        } else if (currentVersion <= 4) {
            createBackup(signDirectory);
            updateCommands(updatePermissions(signFile), signFile);

        } else if (currentVersion <= 5) {
            createBackup(signDirectory);
            updatePermissions(signFile);
        }
        return signFile;
    }

    public static void performAllServerSignUpdates(ServerSign sign, ServerSignsPlugin plugin, ServerSignManager manager) throws IOException {
        if (currentVersion <= 4) {
            createBackup(plugin.getDataFolder().toPath().resolve("signs"));
            updateLastUseMapUUIDs(sign, plugin);
            updateProtectedBlocks(sign, manager);
        } else if (currentVersion <= 6) {
            createBackup(plugin.getDataFolder().toPath().resolve("signs"));
            updateProtectedBlocks(sign, manager);
        }
    }

    public static Path updateMalformedFileName(Path signDirectory, Path signFile) throws IOException {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(signFile.toFile());
        return updateMalformedFileName(yml, signDirectory, signFile);
    }

    private static Path updateMalformedFileName(YamlConfiguration yml, Path signDirectory, Path signFile) throws IOException {
        return Files.move(signFile, signDirectory.resolve(String.format("%s_%d_%d_%d%s", yml.getString("world"), Integer.valueOf(yml.getInt("X")), Integer.valueOf(yml.getInt("Y")), Integer.valueOf(yml.getInt("Z")), ".yml")), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    public static YamlConfiguration updatePriceItemData(Path signFile) throws IOException {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(signFile.toFile());
        updatePriceItemData(yaml, signFile);
        return yaml;
    }

    private static YamlConfiguration updatePriceItemData(YamlConfiguration yml, Path signFile) throws IOException {
        List<String> piStrings = yml.getStringList("priceItems");

        for (int k = 0; k < piStrings.size(); k++) {
            String raw = piStrings.get(k);
            piStrings.set(k, ItemStringConverter.convertPreV4String(raw));
        }
        yml.set("priceItems", piStrings);
        yml.save(signFile.toFile());
        return yml;
    }

    public static YamlConfiguration updateCommands(Path signFile) throws IOException {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(signFile.toFile());
        updateCommands(yaml, signFile);
        return yaml;
    }

    private static YamlConfiguration updateCommands(YamlConfiguration yaml, Path signFile) throws IOException {
        if (yaml.getConfigurationSection("commands") != null) {
            return yaml;
        }
        List<String> commands = yaml.getStringList("commands");
        if (commands.isEmpty()) return yaml;
        yaml.set("commands", null);
        for (int k = 0; k < commands.size(); k++) {
            try {
                ServerSignCommand command = ServerSignCommandFactory.getCommandFromString(commands.get(k), null);
                if (command != null) {
                    yaml.set("commands." + k + ".command", command.getUnformattedCommand());
                    yaml.set("commands." + k + ".type", command.getType().toString());
                    yaml.set("commands." + k + ".delay", Long.valueOf(command.getDelay()));
                    yaml.set("commands." + k + ".grantPerms", command.getGrantPermissions());
                    yaml.set("commands." + k + ".alwaysPersisted", Boolean.valueOf(command.isAlwaysPersisted()));
                    yaml.set("commands." + k + ".interactValue", Integer.valueOf(command.getInteractValue()));
                } else {
                    ServerSignsPlugin.log("Encountered invalid command while updating " + signFile.getFileName().toString() + ": '" + commands.get(k) + "'");
                }
            } catch (CommandParseException ex) {
                ServerSignsPlugin.log(String.format("Encountered an exception while converting commands in file '%s' (%s) - this ServerSign might not perform correctly!", signFile.getFileName(), ex.getMessage()), Level.WARNING);
            }
        }
        yaml.save(signFile.toFile());
        return yaml;
    }

    public static void updateLastUseMapUUIDs(ServerSign sign, ServerSignsPlugin plugin) throws IOException {
        for (Map.Entry<String, Long> entry : sign.getLastUse().entrySet()) {
            if (((String) entry.getKey()).length() <= 16) {
                UUIDUpdateTask task = new UUIDUpdateTask(plugin, sign);
                task.updateLastUse();
                return;
            }
        }
    }

    public static YamlConfiguration updatePermissions(Path signFile) throws IOException {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(signFile.toFile());
        updatePermissions(yaml, signFile);
        return yaml;
    }

    private static YamlConfiguration updatePermissions(YamlConfiguration yaml, Path signFile) throws IOException {
        if (yaml.contains("permission")) {
            String perm = yaml.getString("permission");
            if (!perm.isEmpty()) {
                yaml.set("permissions", java.util.Collections.singletonList(perm));
                yaml.save(signFile.toFile());
            }
            yaml.set("permission", null);
        }
        return yaml;
    }

    public static void updateProtectedBlocks(ServerSign sign, ServerSignManager manager) {
        sign.updateProtectedBlocks();
        manager.save(sign);
    }

    private static void createBackup(Path signsDir) throws IOException {
        if (lastBackup > 0L) {
            return;
        }
        lastBackup = System.currentTimeMillis();

        Path toCopyTo = signsDir.resolveSibling("signs_pre_conversion_backup");
        while (Files.isDirectory(toCopyTo)) {
            toCopyTo = signsDir.resolveSibling(toCopyTo.getFileName() + "1");
        }

        Files.createDirectory(toCopyTo);
        Iterator<Path> stream = Files.newDirectoryStream(signsDir).iterator();
        while (stream.hasNext()) {
            Path next = stream.next();
            if ((!Files.isDirectory(next)) &&
                    (!next.getFileName().toString().startsWith("."))) {
                Files.copy(next, toCopyTo.resolve(next.getFileName()));
            }
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\legacy\ServerSignConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */