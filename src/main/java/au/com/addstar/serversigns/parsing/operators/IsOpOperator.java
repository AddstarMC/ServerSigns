package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;
import org.bukkit.entity.Player;

public class IsOpOperator extends ConditionalOperator {
    public IsOpOperator() {
        super("isOp", false);
    }

    public ConditionalOperator.ParameterValidityResponse checkParameterValidity(String params) {
        return new ConditionalOperator.ParameterValidityResponse(true);
    }

    public boolean meetsConditions(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        if (executor == null) {
            return true;
        }
        return executor.isOp();
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\operators\IsOpOperator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */