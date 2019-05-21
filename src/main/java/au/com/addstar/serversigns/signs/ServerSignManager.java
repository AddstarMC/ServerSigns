package au.com.addstar.serversigns.signs;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.persist.YamlFieldPersistence;
import au.com.addstar.serversigns.legacy.ServerSignConverter;
import au.com.addstar.serversigns.persist.PersistenceException;
import au.com.addstar.serversigns.persist.mapping.MappingException;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.Door;

public class ServerSignManager {
    private final ServerSignsPlugin plugin;
    private final Path SIGNS_DIRECTORY;
    private final Path INVALID_SIGNS_PARENT_DIRECTORY;
    private final Path INVALID_SIGNS_LOCATION_DIRECTORY;
    private final Path INVALID_SIGNS_COMMANDS_DIRECTORY;
    private final Path INVALID_SIGNS_DUPLICATE_DIRECTORY;
    private final Path EXPIRED_SIGNS_DIRECTORY;
    private HashMap<Location, ServerSign> signs =  new HashMap<>();

    public ServerSignManager(ServerSignsPlugin instance) throws IOException {
        this.plugin = instance;

        this.SIGNS_DIRECTORY = instance.getDataFolder().toPath().resolve("signs");
        this.INVALID_SIGNS_PARENT_DIRECTORY = this.SIGNS_DIRECTORY.resolve("invalid");
        this.INVALID_SIGNS_LOCATION_DIRECTORY = this.INVALID_SIGNS_PARENT_DIRECTORY.resolve("invalid_location");
        this.INVALID_SIGNS_COMMANDS_DIRECTORY = this.INVALID_SIGNS_PARENT_DIRECTORY.resolve("invalid_commands");
        this.INVALID_SIGNS_DUPLICATE_DIRECTORY = this.INVALID_SIGNS_PARENT_DIRECTORY.resolve("duplicate");
        this.EXPIRED_SIGNS_DIRECTORY = this.SIGNS_DIRECTORY.resolve("expired");

        Files.createDirectories(this.SIGNS_DIRECTORY);
        Files.createDirectories(this.INVALID_SIGNS_PARENT_DIRECTORY);
        Files.createDirectories(this.INVALID_SIGNS_LOCATION_DIRECTORY);
        Files.createDirectories(this.INVALID_SIGNS_COMMANDS_DIRECTORY);
        Files.createDirectories(this.INVALID_SIGNS_DUPLICATE_DIRECTORY);
        Files.createDirectories(this.EXPIRED_SIGNS_DIRECTORY);
    }

    public Set<ServerSign> prepareServerSignsSet() {
        Set<ServerSign> signs = new HashSet<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.SIGNS_DIRECTORY)) {
            for (Path current : directoryStream) {
                YamlConfiguration yamlLoad;
                if (Files.isDirectory(current, new java.nio.file.LinkOption[0]) || current.getFileName().toString().startsWith("."))
                    continue;
                if (!current.getFileName().toString().endsWith(".yml") || Files.size(current) < 64L) {
                    ServerSignsPlugin.log("Could not load ServerSign " + current.getFileName() + ". The file is empty or invalid, proceeding to next file.");
                    Files.move(current, this.INVALID_SIGNS_PARENT_DIRECTORY.resolve(current.getFileName()), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});

                    continue;
                }
                current = ServerSignConverter.performAllFileUpdates(this.SIGNS_DIRECTORY, current);
                try {
                    yamlLoad = YamlConfiguration.loadConfiguration(current.toFile());
                } catch (Exception ex) {
                    ServerSignsPlugin.log("Could not load ServerSign " + current.getFileName() + ". The file configuration is invalid, proceeding to next file.");
                    Files.move(current, this.INVALID_SIGNS_PARENT_DIRECTORY.resolve(current.getFileName()), StandardCopyOption.REPLACE_EXISTING);

                    continue;
                }
                ServerSign sign = loadFromFile(yamlLoad, current.getFileName().toString(), current);
                if (sign == null) {
                    continue;
                }


                for (ServerSign loaded : signs) {
                    if (loaded.getWorld().equals(sign.getWorld()) && loaded.getX() == sign.getX() && loaded.getY() == sign.getY() && loaded.getZ() == sign.getZ()) {

                        ServerSignsPlugin.log("Could not load " + current.getFileName() + ". Duplicated entry (another ServerSign already exists at that location)");
                        Files.move(current, this.INVALID_SIGNS_DUPLICATE_DIRECTORY.resolve(current.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                    }
                }


                if (sign.getCommands() == null || (sign.getCommands().size() > 0 && sign.getCommands().get(0) == null)) {
                    ServerSignsPlugin.log("Could not load ServerSign " + current.getFileName() + ". The file doesn't contain any valid commands, proceeding to next file.");
                    Files.move(current, this.INVALID_SIGNS_COMMANDS_DIRECTORY.resolve(current.getFileName()), StandardCopyOption.REPLACE_EXISTING);

                    continue;
                }

                HashMap<String, Long> toKeep = new HashMap<>();
                for (Map.Entry<String, Long> entry : sign.getLastUse().entrySet()) {
                    if (entry.getValue() + sign.getCooldown() * 1000L > System.currentTimeMillis()) {
                        toKeep.put(entry.getKey(), entry.getValue());
                    }
                }

                if (sign.getLastUse().size() - toKeep.size() > 0) {
                    ServerSignsPlugin.log("Discarding " + (sign.getLastUse().size() - toKeep.size()) + " expired cooldowns for a ServerSign at " + sign.getLocationString());
                    yamlLoad.set("lastUse", toKeep);
                    yamlLoad.save(current.toFile());
                    sign.setLastUse(toKeep);
                }

                signs.add(sign);
            }
        } catch (IOException ex) {
            ServerSignsPlugin.log("Encountered an I/O error while loading ServerSigns from plugins/ServerSigns/signs!", Level.SEVERE, ex);
            signs = null;
        }

        return signs;
    }

    public boolean populateSignsMap(Set<ServerSign> preparedSigns) {
        if (preparedSigns == null) return false;
        this.signs.clear();
        try {
            for (ServerSign sign : preparedSigns) {
                Path path = getPath(sign);
                if ((org.bukkit.Bukkit.getWorld(sign.getWorld()) == null) || (sign.getLocation() == null)) {
                    ServerSignsPlugin.log("Could not load " + path.getFileName() + ". Invalid location");
                    Files.move(path, this.INVALID_SIGNS_LOCATION_DIRECTORY.resolve(path.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    if (isSpecialMultiBlock(sign.getLocation().getBlock())) {
                        saveMultiReference(sign);
                    } else {
                        this.signs.put(sign.getLocation(), sign);
                    }


                    ServerSignConverter.performAllServerSignUpdates(sign, this.plugin, this);
                }
            }
        } catch (IOException ex) {
            ServerSignsPlugin.log("Encountered an I/O error while validating ServerSigns from plugins/ServerSigns/signs!", Level.SEVERE, ex);
            return false;
        }

        return true;
    }

    private ServerSign loadFromFile(YamlConfiguration yaml, String fileName, Path signPath) throws IOException {
        if (yaml == null) {
            ServerSignsPlugin.log("An error has occurred while loading a ServerSign from " + fileName + " - Yaml is null");
            return null;
        }

        ServerSign sign = new ServerSign();
        try {
            YamlFieldPersistence.loadFromYaml(yaml, sign);
        } catch (PersistenceException ex) {
            ServerSignsPlugin.log("An error has occurred while loading a ServerSign from " + fileName + " - " + ex.getMessage());
            return null;
        } catch (MappingException ex) {
            switch (ex.getType()) {
                case COMMANDS:
                    ServerSignsPlugin.log("Could not load ServerSign from " + fileName + " - The file contains error(s) in the commands section, proceeding to next file.");
                    Files.move(signPath, this.INVALID_SIGNS_COMMANDS_DIRECTORY.resolve(signPath.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            }

            return null;
        }

        return sign;
    }

    public void save(ServerSign sign) {
        if (!this.signs.containsKey(sign.getLocation())) {
            if (isSpecialMultiBlock(sign.getLocation().getBlock())) {
                saveMultiReference(sign);
            } else {
                this.signs.put(sign.getLocation(), sign);
            }
        }

        saveToFile(sign);
    }

    private void saveMultiReference(ServerSign sign) {
        Block block = sign.getLocation().getBlock();

        this.signs.put(sign.getLocation(), sign);
        if ((block.getState().getData() instanceof Door)) {
            Door door = (Door) block.getState().getData();
            this.signs.put(block.getRelative(door.isTopHalf() ? BlockFace.DOWN : BlockFace.UP).getLocation(), sign);
        } else if ((block.getState() instanceof Chest)) {
            Chest chest = (Chest) block.getState();
            if ((chest.getInventory().getHolder() instanceof DoubleChest)) {
                DoubleChest dc = (DoubleChest) chest.getInventory().getHolder();
                if (((Chest) dc.getLeftSide()).getLocation().equals(sign.getLocation())) {
                    this.signs.put(((Chest) dc.getRightSide()).getLocation(), sign);
                } else {
                    this.signs.put(((Chest) dc.getLeftSide()).getLocation(), sign);
                }
            }
        }
    }

    private void saveToFile(ServerSign sign) {
        if ((sign == null) || (sign.getLocation() == null)) {
            ServerSignsPlugin.log("An error has occurred while saving a ServerSign - Unable to determine location");
            return;
        }
        try {
            Path path = getPath(sign);
            Files.deleteIfExists(path);

            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            YamlFieldPersistence.saveToYaml(yamlConfiguration, sign);

            yamlConfiguration.save(getPath(sign).toFile());
        } catch (IOException | PersistenceException e) {
            ServerSignsPlugin.log("An error has occurred while saving ServerSign at " + sign.getLocationString(), Level.SEVERE, e);
        }
    }

    public void remove(ServerSign sign) {
        if (isSpecialMultiBlock(sign.getLocation().getBlock())) {
            removeMultiReference(sign);
        } else {
            this.signs.remove(sign.getLocation());
        }
        try {
            Files.deleteIfExists(getPath(sign));
        } catch (IOException ex) {
            ServerSignsPlugin.log("Encountered an I/O error while removing a ServerSign!", Level.SEVERE, ex);
        }
    }

    private void removeMultiReference(ServerSign sign) {
        Block block = sign.getLocation().getBlock();

        if ((block.getState().getData() instanceof Door)) {
            Door door = (Door) block.getState().getData();
            this.signs.remove(block.getRelative(door.isTopHalf() ? BlockFace.DOWN : BlockFace.UP).getLocation());
        } else if ((block.getState() instanceof Chest)) {
            Chest chest = (Chest) block.getState();
            if ((chest.getInventory().getHolder() instanceof DoubleChest)) {
                DoubleChest dc = (DoubleChest) chest.getInventory().getHolder();
                if (((Chest) dc.getLeftSide()).getLocation().equals(sign.getLocation())) {
                    this.signs.remove(((Chest) dc.getRightSide()).getLocation());
                } else {
                    this.signs.remove(((Chest) dc.getLeftSide()).getLocation());
                }
            }
        }
        this.signs.remove(sign.getLocation());
    }

    public void expire(ServerSign sign) {
        if (isSpecialMultiBlock(sign.getLocation().getBlock())) {
            removeMultiReference(sign);
        } else {
            this.signs.remove(sign.getLocation());
        }
        try {
            Path targetPath = this.EXPIRED_SIGNS_DIRECTORY.resolve(getPath(sign).getFileName().toString());
            while (Files.exists(targetPath)) {
                targetPath = this.EXPIRED_SIGNS_DIRECTORY.resolve(targetPath.getFileName() + "1");
            }

            Files.move(getPath(sign), targetPath);
        } catch (IOException ex) {
            ServerSignsPlugin.log("Encountered an I/O error while moving an expired ServerSign!", Level.SEVERE, ex);
        }
    }

    public ServerSign copy(ServerSign copyFrom) {
        try {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(getPath(copyFrom).toFile());
            ServerSign toReturn = new ServerSign();
            YamlFieldPersistence.loadFromYaml(yaml, toReturn);
            return toReturn;
        } catch (MappingException | PersistenceException ex) {
            ServerSignsPlugin.log("Encountered an error while copying a ServerSign!", Level.SEVERE, ex);
        }
        return null;
    }

    private Path getPath(ServerSign sign) {
        return this.SIGNS_DIRECTORY.resolve(sign.getWorld() + "_" + sign.getX() + "_" + sign.getY() + "_" + sign.getZ() + ".yml");
    }

    public ServerSign getServerSignByLocation(Location location) {
        return this.signs.get(location);
    }

    public boolean isLocationProtectedByServerSign(Location location) {
        for (ServerSign serverSign : this.signs.values()) {
            if (serverSign.isProtected(location)) {
                return true;
            }
        }
        return false;
    }

    public java.util.Collection<ServerSign> getSigns() {
        return this.signs.values();
    }

    public void setSigns(HashMap<Location, ServerSign> signs) {
        this.signs = signs;
    }

    private boolean isSpecialMultiBlock(Block block) {
        return ((block.getState().getData() instanceof Door)) || ((block.getState() instanceof Chest));
    }
}


