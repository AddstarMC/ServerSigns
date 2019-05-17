package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.utils.NumberUtils;

public class LoopIsOperator extends ConditionalOperator {
    public LoopIsOperator() {
        super("loopIs", true);
    }

    public ConditionalOperator.ParameterValidityResponse checkParameterValidity(String params) {
        return new ConditionalOperator.ParameterValidityResponse(NumberUtils.isInt(params), "Parameter must be a single integer");
    }

    public boolean meetsConditions(org.bukkit.entity.Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        if (this.params == null) {
            return false;
        }

        int loop = NumberUtils.parseInt(this.params);
        return executingSign.getCurrentLoop() == loop;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\operators\LoopIsOperator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */