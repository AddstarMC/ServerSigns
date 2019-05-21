package au.com.addstar.serversigns.legacy;

import au.com.addstar.serversigns.ServerSignsPlugin;

import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageYmlConverter {
    public static void convertLanguagesFile(Path file, Path translationsDirectory) throws java.io.IOException {
        if (Files.exists(file)) {
            ServerSignsPlugin.log("Converting languages.yml to new translations system");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file.toFile());
            if (yaml.getConfigurationSection("") != null) {
                for (String language : yaml.getConfigurationSection("").getKeys(false)) {
                    Path newFile = translationsDirectory.resolve(language + ".yml");
                    Files.createFile(newFile);

                    YamlConfiguration newYaml = YamlConfiguration.loadConfiguration(newFile.toFile());
                    for (String key : yaml.getConfigurationSection(language).getKeys(false)) {
                        newYaml.set(key, yaml.getString(language + "." + key));
                    }
                    newYaml.save(newFile.toFile());
                }
            }

            Files.delete(file);
        }
    }
}


