package au.com.addstar.serversigns.parsing.command;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.parsing.CommandType;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class ReturnServerSignCommand extends ServerSignCommand {
    public ReturnServerSignCommand() {
        super(CommandType.RETURN, "");
    }


    public boolean isAlwaysPersisted() {
        return false;
    }


    public void setAlwaysPersisted(boolean val) {
    }


    public CommandType getType() {
        return this.type;
    }

    public List<String> getGrantPermissions() {
        return  new ArrayList<>();
    }

    public void setGrantPermissions(List<String> grant) {
    }

    public long getDelay() {
        return 0L;
    }

    public void setDelay(long delay) {
    }

    public String getUnformattedCommand() {
        return this.command;
    }

    public String getFormattedCommand(Player executor, ServerSignsPlugin plugin, Map<String, String> injectedReplacements) {
        return this.command;
    }

    public List<TaskManagerTask> getTasks(Player executor, ServerSignsPlugin plugin, Map<String, String> injectedReplacements) {
        return  new ArrayList<>();
    }
}


