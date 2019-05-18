package au.com.addstar.serversigns.translations;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.legacy.LanguageYmlConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageHandler {
    private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>(Arrays.asList("en", "de"));
    private ServerSignsPlugin plugin;
    private Path translationsDirectory;
    private Map<Message, String> currentTranslation = new HashMap<>(Message.values().length);

    public MessageHandler(ServerSignsPlugin plugin) {
        this.plugin = plugin;
        this.translationsDirectory = plugin.getDataFolder().toPath().resolve("translations");

        if ((!Files.exists(this.translationsDirectory)) || (!Files.isDirectory(this.translationsDirectory))) {
            try {
                Files.createDirectories(this.translationsDirectory);
                Path pluginFolder = plugin.getDataFolder().toPath();
                if (Files.exists(pluginFolder.resolve("languages.yml"))) {
                    LanguageYmlConverter.convertLanguagesFile(pluginFolder.resolve("languages.yml"), this.translationsDirectory);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.translationsDirectory);
            for (Path path : directoryStream) {
                if ((!Files.isDirectory(path)) && (path.getFileName().toString().trim().endsWith("_default.yml"))) {
                    String prefix = path.getFileName().toString().substring(0, path.getFileName().toString().length() - 12);
                    if (!SUPPORTED_LANGUAGES.contains(prefix)) {
                        Path target = path.resolveSibling(prefix + ".yml");
                        int counter = 0;
                        while (Files.exists(target)) {
                            target = target.resolveSibling(String.format("%s_copy%s.yml", prefix, counter++ > 0 ? String.valueOf(counter) : ""));
                        }
                        plugin.getLogger().info(String.format("Renaming invalid language file from '%s' to '%s'", path.getFileName(), target.getFileName()));
                        Files.move(path, target);
                    }
                }
            }
            directoryStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        for (String lang : SUPPORTED_LANGUAGES) {
            copyEmbedded(lang + "_default.yml", this.translationsDirectory.resolve(lang + "_default.yml"));
        }
    }

    public String get(Message message) {
        return this.currentTranslation.get(message);
    }

    public void setCurrentTranslation(String name) throws NoDefaultException {
        this.currentTranslation.clear();

        try {
            if (SUPPORTED_LANGUAGES.contains(name)) {
                this.currentTranslation.putAll(readFile(name, this.translationsDirectory.resolve(name + "_default.yml"), false));
            } else {
                this.currentTranslation.putAll(readFile("en_default", this.translationsDirectory.resolve("en_default.yml"), false));
            }
        } catch (IOException | InvalidConfigurationException ex) {
            throw new NoDefaultException("Unable to load default translation for language '" + name + "'", ex);
        }

        Path targetFile = this.translationsDirectory.resolve(name + ".yml");
        try {
            Map<Message, String> map = readFile(name, targetFile, true);

            if (!map.isEmpty()) {
                this.currentTranslation.putAll(map);
            }
        } catch (IOException ex) {
            ServerSignsPlugin.log(String.format("Unable to load '%s' translation from '%s' as an I/O error occurred", name, targetFile.getFileName()), Level.SEVERE);
            ServerSignsPlugin.log("The default translation will be used instead.");
        } catch (InvalidConfigurationException ex) {
            ServerSignsPlugin.log(ex.getMessage(), Level.SEVERE);
            ServerSignsPlugin.log("The default translation will be used instead.");
        }
    }

    private Map<Message, String> readFile(String langId, Path targetFile, boolean update) throws IOException, InvalidConfigurationException {
        if (!Files.exists(targetFile)) {
            throw new java.nio.file.NoSuchFileException(String.format("Unable to load %s translations from file '%s' as it does not exist!", langId, targetFile.getFileName()));
        }

        Map<Message, String> newMap = new HashMap<>();

        YamlConfiguration yaml = new YamlConfiguration();
        yaml.load(targetFile.toFile());
        boolean changed = false;
        for (Message message : Message.values()) {
            if (!yaml.contains(message.getPath())) {
                ServerSignsPlugin.log(String.format("Unable to load %s translation for message key '%s' from file '%s'%s", langId, message.getPath(), targetFile.getFileName(), update ? " - inserting default key" : ""));
                if (update) {
                    yaml.set(message.getPath(), "{DEFAULT}");
                    changed = true;
                }
            } else if (!yaml.getString(message.getPath()).trim().equals("{DEFAULT}")) {
                newMap.put(message, yaml.getString(message.getPath()));
            }
        }

        if (changed) yaml.save(targetFile.toFile());
        return newMap;
    }

    private void copyEmbedded(String resourceName, Path target) {
        java.io.InputStream resource = this.plugin.getResource(resourceName);
        if (resource != null) {
            try {
                Files.deleteIfExists(target);
                Files.createFile(target);

                BufferedWriter writer = Files.newBufferedWriter(target, java.nio.charset.Charset.defaultCharset());
                Throwable localThrowable3 = null;
                try {
                    BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(resource));
                    Throwable localThrowable4 = null;
                    try {
                        writer.write("# This file is DELETED and REGENERATED on every server restart!");
                        writer.newLine();
                        writer.write("# Create a NEW FILE for custom language translations!");
                        writer.newLine();
                        writer.write("# For more information, see the wiki: http://serversigns.de/cfg");
                        writer.newLine();
                        writer.newLine();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                            writer.newLine();
                        }
                    } catch (Throwable localThrowable1) {
                        localThrowable4 = localThrowable1;
                        throw localThrowable1;
                    }
                } catch (Throwable localThrowable2) {
                    localThrowable3 = localThrowable2;
                    throw localThrowable2;
                } finally {
                    if (writer != null)
                        if (localThrowable3 != null)
                            try {
                                writer.close();
                            } catch (Throwable x2) {
                                localThrowable3.addSuppressed(x2);
                            }
                        else
                            writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("No embedded resources found by the name '" + resourceName + "'");
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\translations\MessageHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */