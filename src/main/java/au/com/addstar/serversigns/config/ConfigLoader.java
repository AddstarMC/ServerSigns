package au.com.addstar.serversigns.config;

import au.com.addstar.serversigns.persist.PersistenceException;
import au.com.addstar.serversigns.legacy.ConfigurationConverter;
import au.com.addstar.serversigns.persist.YamlFieldPersistence;
import au.com.addstar.serversigns.persist.mapping.MappingException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.PatternSyntaxException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigLoader {
    public static ServerSignsConfig loadConfig(Path configPath) throws ConfigLoadingException {
        ServerSignsConfig config = new ServerSignsConfig();
        if (!Files.exists(configPath)) {
            config.findTimeZone();
            ConfigGenerator.generate(config, configPath);
            config.colourise();
        } else {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            try {
                yamlConfiguration.loadFromString(new String(Files.readAllBytes(configPath)));
            } catch (java.io.IOException | InvalidConfigurationException e) {
                throw new ConfigLoadingException("Could not read " + configPath.toString(), e);
            }

            int version = yamlConfiguration.getInt("config-version", 0);
            try {
                if (version == 0) {
                    ConfigurationConverter.updateConfig_0(yamlConfiguration, config, configPath);
                } else if (version == 1) {
                    YamlFieldPersistence.loadFromYaml(yamlConfiguration, config);
                    ConfigurationConverter.updateConfig_1(yamlConfiguration, config, configPath);
                } else if (version == 2) {
                    YamlFieldPersistence.loadFromYaml(yamlConfiguration, config);
                    ConfigurationConverter.updateConfig_2(yamlConfiguration, config, configPath);
                } else if (version == 3) {
                    YamlFieldPersistence.loadFromYaml(yamlConfiguration, config);
                } else {
                    throw new ConfigLoadingException("Invalid config-version in config.yml");
                }
            } catch (MappingException | PersistenceException ex) {
                throw new ConfigLoadingException(ex.getMessage(), ex);
            }

            try {
                config.compilePatterns();
            } catch (PatternSyntaxException ex) {
                throw new ConfigLoadingException("Encountered a Syntax violation while parsing a Regular Expression pattern", ex);
            }
        }

        return config;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\config\ConfigLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */