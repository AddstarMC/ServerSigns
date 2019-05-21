package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.utils.NumberUtils;
import org.bukkit.entity.Player;

public class OnlinePlayersOperator extends ConditionalOperator {
    public OnlinePlayersOperator() {
        super("onlinePlayers", true);
    }

    public ConditionalOperator.ParameterValidityResponse checkParameterValidity(String params) {
        char firstChar = params.charAt(0);
        if ((firstChar != '>') && (firstChar != '<') && (firstChar != '=')) {
            return new ConditionalOperator.ParameterValidityResponse(false, "Parameter must start with > < or =");
        }

        String value = params.substring(1);
        if (!NumberUtils.isInt(value)) {
            return new ConditionalOperator.ParameterValidityResponse(false, "Parameter must be an integer preceded by > < or =");
        }

        return new ConditionalOperator.ParameterValidityResponse(true);
    }

    public boolean meetsConditions(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        if (this.params == null) {
            return false;
        }

        int onlinePlayers = plugin.getServer().getOnlinePlayers().size();

        char firstChar = this.params.charAt(0);
        int value = NumberUtils.parseInt(this.params.substring(1));
        switch (firstChar) {
            case '>':
                return onlinePlayers > value;
            case '<':
                return onlinePlayers < value;
            case '=':
                return onlinePlayers == value;
        }
        return false;
    }
}


