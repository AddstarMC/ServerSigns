package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.utils.NumberUtils;

import java.util.Random;

import org.bukkit.entity.Player;

public class RandomOperator
        extends ConditionalOperator {
    private static final Random RANDOM = new Random();

    public RandomOperator() {
        super("random", true);
    }

    public ConditionalOperator.ParameterValidityResponse checkParameterValidity(String params) {
        if (!params.endsWith("%")) {
            return new ConditionalOperator.ParameterValidityResponse(false, "Parameter must be a percentage");
        }

        String value = params.substring(0, params.length() - 1);
        if (!NumberUtils.isDouble(value)) {
            return new ConditionalOperator.ParameterValidityResponse(false, "Parameter must be a numeral percentage such as 25% or 0.01%");
        }

        return new ConditionalOperator.ParameterValidityResponse(true);
    }

    public boolean meetsConditions(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        if (this.params == null) {
            return false;
        }

        double percentChance = NumberUtils.parseDouble(this.params.substring(0, this.params.length() - 1));
        return RANDOM.nextDouble() < percentChance / 100.0D;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\operators\RandomOperator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */