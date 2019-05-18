package au.com.addstar.serversigns.legacy;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.signs.ServerSignManager;
import au.com.addstar.serversigns.utils.NumberUtils;
import au.com.addstar.serversigns.utils.UUIDUpdateTask;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;


public class ServerSignConverter {
    public static final int FILE_VERSION = 7;
    private static int currentVersion = -1;
    private static long lastBackup = 0L;

    public static int getCurrentPersistVersion(Path signDirectory) throws IOException {
        Path verFile = signDirectory.resolve(".svs_persist_version");
        int currentVersion = 0;
        byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putInt(FILE_VERSION);
        if (!Files.exists(verFile)) {
            Files.write(verFile, bytes, StandardOpenOption.CREATE);
        } else {
            currentVersion = NumberUtils.parseInt(new String(Files.readAllBytes(verFile)), 0);
            if (currentVersion < FILE_VERSION) {
                Files.delete(verFile);
                Files.write(verFile, bytes, StandardOpenOption.CREATE);
            }
        }
        return currentVersion;
    }

    public static Path performAllFileUpdates(Path signDirectory, Path signFile) throws IOException {
        if (currentVersion == -1) {
            currentVersion = getCurrentPersistVersion(signDirectory);
        }
        if (currentVersion <= 2) {
            throw new IOException("File Out of date" + signFile.getFileName());
        }


        if (currentVersion <= 3) {
            throw new IOException("File Out of date" + signFile.getFileName());



        } else if (currentVersion <= 4) {
            throw new IOException("File Out of date" + signFile.getFileName());


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

    public static void updateLastUseMapUUIDs(ServerSign sign, ServerSignsPlugin plugin) throws IOException {
        for (Map.Entry<String, Long> entry : sign.getLastUse().entrySet()) {
            if ((entry.getKey()).length() <= 16) {
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
        for (Path next : Files.newDirectoryStream(signsDir)) {
            if ((!Files.isDirectory(next)) &&
                    (!next.getFileName().toString().startsWith("."))) {
                Files.copy(next, toCopyTo.resolve(next.getFileName()));
            }
        }
    }
}