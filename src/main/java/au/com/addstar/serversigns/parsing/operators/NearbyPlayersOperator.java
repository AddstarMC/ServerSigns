package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.utils.NumberUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NearbyPlayersOperator extends ConditionalOperator {
    public NearbyPlayersOperator() {
        super("nearbyPlayers", true);
    }


    public ConditionalOperator.ParameterValidityResponse checkParameterValidity(String params) {
        char operatorChar = getOperatorChar(params);
        if (operatorChar == '¬') {
            return new ConditionalOperator.ParameterValidityResponse(false, "Parameter must be in the format <radius><operator><players> - where <operator> is > < or =");
        }

        String radiusStr = params.split(operatorChar + "")[0];
        String playersStr = params.split(operatorChar + "")[1];

        if (!NumberUtils.isInt(radiusStr))
            return new ConditionalOperator.ParameterValidityResponse(false, "Parameter must be in the format <radius><operator><players> - where <radius> is an integer");
        if (!NumberUtils.isInt(playersStr)) {
            return new ConditionalOperator.ParameterValidityResponse(false, "Parameter must be in the format <radius><operator><players> - where <players> is an integer");
        }

        return new ConditionalOperator.ParameterValidityResponse(true);
    }

    public boolean meetsConditions(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        if (this.params == null) {
            return false;
        }

        char operatorChar = getOperatorChar(this.params);

        int radius = NumberUtils.parseInt(this.params.split(operatorChar + "")[0]);
        int players = NumberUtils.parseInt(this.params.split(operatorChar + "")[1]);

        switch (operatorChar) {
            case '>':
                return getNearbyPlayers(executingSign.getLocation(), radius) > players;
            case '<':
                return getNearbyPlayers(executingSign.getLocation(), radius) < players;
            case '=':
                return getNearbyPlayers(executingSign.getLocation(), radius) == players;
        }
        return false;
    }

    private char getOperatorChar(String params) {
        if (params.contains(">"))
            return '>';
        if (params.contains("<"))
            return '<';
        if (params.contains("=")) {
            return '=';
        }
        return '¬';
    }

    private int getNearbyPlayers(Location location, int radius) {
        int k = 0;
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= radius) {
                k++;
            }
        }
        return k;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\operators\NearbyPlayersOperator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */