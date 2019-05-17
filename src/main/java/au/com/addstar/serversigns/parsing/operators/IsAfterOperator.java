package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.utils.NumberUtils;
import au.com.addstar.serversigns.utils.TimeUtils;
import org.bukkit.entity.Player;

public class IsAfterOperator extends ConditionalOperator {
    public IsAfterOperator() {
        super("isAfter", true);
    }

    public ConditionalOperator.ParameterValidityResponse checkParameterValidity(String params) {
        boolean isValid = true;
        if ((params.length() != 13) || (!NumberUtils.isInt(params.substring(0, 6))) || (!NumberUtils.isInt(params.substring(7)))) {
            isValid = false;
        }
        return new ConditionalOperator.ParameterValidityResponse(isValid, "Parameter must be a date & time string in the format DDMMYY,HHMMSS - note the comma separator");
    }

    public boolean meetsConditions(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        if (this.params == null) {
            return false;
        }

        long timestamp = TimeUtils.convertDSDDFToEpochMillis(this.params, plugin.config.getTimeZone());
        return System.currentTimeMillis() >= timestamp;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\operators\IsAfterOperator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */