package au.com.addstar.serversigns.legacy;

import au.com.addstar.serversigns.config.ConfigLoadingException;
import au.com.addstar.serversigns.config.ServerSignsConfig;
import au.com.addstar.serversigns.persist.YamlFieldPersistence;
import au.com.addstar.serversigns.config.ConfigGenerator;
import au.com.addstar.serversigns.persist.PersistenceException;
import au.com.addstar.serversigns.persist.mapping.MappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationConverter {
    public static void updateConfig_0(YamlConfiguration yaml, ServerSignsConfig newConfig, Path configPath) throws PersistenceException, ConfigLoadingException, MappingException {
        OldServerSignsConfig oldServerSignsConfig = new OldServerSignsConfig();
        YamlFieldPersistence.loadFromYaml(yaml, oldServerSignsConfig);

        newConfig.setFromOldConfig(oldServerSignsConfig);

        updateConfig_x(yaml, newConfig, configPath, "config_vPRE1_backup.yml");
    }

    public static void updateConfig_1(YamlConfiguration yaml, ServerSignsConfig config, Path configPath) throws PersistenceException, ConfigLoadingException {
        updateConfig_x(yaml, config, configPath, "config_v1_backup.yml");
    }

    public static void updateConfig_2(YamlConfiguration yaml, ServerSignsConfig config, Path configPath) throws PersistenceException, ConfigLoadingException {
        updateConfig_x(yaml, config, configPath, "config_v2_backup.yml");
    }

    private static void updateConfig_x(YamlConfiguration yaml, ServerSignsConfig config, Path configPath, String backupName) throws PersistenceException, ConfigLoadingException {
        try {
            Files.move(configPath, configPath.resolveSibling(backupName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ConfigLoadingException("Could not backup old config.yml", e);
        }
        ConfigGenerator.generate(config, configPath);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\legacy\ConfigurationConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */