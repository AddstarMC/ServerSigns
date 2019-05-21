package au.com.addstar.serversigns.persist.mapping;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.parsing.CommandParseException;
import au.com.addstar.serversigns.parsing.CommandType;
import au.com.addstar.serversigns.parsing.command.ConditionalServerSignCommand;
import au.com.addstar.serversigns.parsing.command.ReturnServerSignCommand;
import au.com.addstar.serversigns.parsing.command.ServerSignCommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

public class ServerSignCommandListMapper implements ISmartPersistenceMapper<List<ServerSignCommand>> {
    private MemorySection memorySection;
    private String host;

    public void setMemorySection(MemorySection memorySection) {
        this.memorySection = memorySection;
    }

    public List<ServerSignCommand> getValue(String path) throws MappingException {
        List<ServerSignCommand> list = new ArrayList<>();

        ConfigurationSection section = this.memorySection.getConfigurationSection(path);
        if (section == null) {
            if (!this.memorySection.contains("commands")) {


                list.add(null);
                return list;
            }
            if (this.memorySection.isList("commands") && this.memorySection.getStringList("commands").isEmpty()) {
                return list;
            }
            ServerSignsPlugin.log("Unable to load commands for " + this.host + " as it has not been updated! Please delete plugins/ServerSigns/signs/.svs_persist_version and restart the server.");
            return null;
        }

        for (String indexStr : section.getKeys(false)) {
            try {
                ServerSignCommand cmd;
                int index = Integer.parseInt(indexStr);


                ConfigurationSection subSection = section.getConfigurationSection(indexStr);
                CommandType type = CommandType.valueOf(subSection.getString("type"));
                int interactValue = subSection.getInt("interactValue");
                String command = subSection.getString("command");

                if (type == CommandType.CONDITIONAL_IF || type == CommandType.CONDITIONAL_ENDIF) {
                    try {
                        cmd = new ConditionalServerSignCommand(type, command);
                        cmd.setInteractValue(interactValue);
                    } catch (CommandParseException ex) {
                        ServerSignsPlugin.log("Encountered an error that is a result of manual file editing: Invalid conditional command defined in '" + this.host + "'");
                        continue;
                    }
                } else if (type == CommandType.RETURN) {
                    cmd = new ReturnServerSignCommand();
                    cmd.setInteractValue(interactValue);
                } else {
                    long delay = subSection.getLong("delay", 0L);
                    boolean alwaysPersisted = subSection.getBoolean("alwaysPersisted");
                    List<String> grantPerms = subSection.getStringList("grantPerms");

                    cmd = new ServerSignCommand(type, command);
                    cmd.setDelay(delay);
                    cmd.setAlwaysPersisted(alwaysPersisted);
                    cmd.setInteractValue(interactValue);
                    if (grantPerms != null && !grantPerms.isEmpty()) {
                        cmd.setGrantPermissions(grantPerms);
                    }
                }

                if (index > list.size()) {
                    list.add(cmd);
                    continue;
                }
                if (index == list.size()) {
                    list.add(index, cmd);
                    continue;
                }
                list.set(index, cmd);
            } catch (Exception ex) {
                throw new MappingException(ex.getMessage(), MappingException.ExceptionType.COMMANDS);
            }
        }

        return list;
    }

    public void setValue(String path, List<ServerSignCommand> value) {
        if (value.isEmpty()) {
            this.memorySection.set(path,  new ArrayList<>());
            return;
        }
        for (int k = 0; k < value.size(); k++) {
            ServerSignCommand cmd = value.get(k);
            this.memorySection.set(path + "." + k + ".command", cmd.getUnformattedCommand());
            this.memorySection.set(path + "." + k + ".type", cmd.getType().toString());
            this.memorySection.set(path + "." + k + ".delay", cmd.getDelay());
            this.memorySection.set(path + "." + k + ".grantPerms", cmd.getGrantPermissions());
            this.memorySection.set(path + "." + k + ".alwaysPersisted", cmd.isAlwaysPersisted());
            this.memorySection.set(path + "." + k + ".interactValue", cmd.getInteractValue());
        }
    }

    public void setHostId(String id) {
        this.host = id;
    }
}


