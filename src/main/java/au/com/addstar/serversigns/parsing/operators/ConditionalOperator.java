package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.ServerSign;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;


public abstract class ConditionalOperator {
    public static final Set<ConditionalOperator> VALUES = new HashSet();

    static {
        VALUES.add(new HasPermOperator());
        VALUES.add(new IsOpOperator());
        VALUES.add(new LoopIsOperator());
        VALUES.add(new RandomOperator());
        VALUES.add(new UsesTallyIsOperator());
        VALUES.add(new IsBeforeOperator());
        VALUES.add(new IsAfterOperator());
        VALUES.add(new CheckOptionOperator());
        VALUES.add(new NearbyPlayersOperator());
        VALUES.add(new OnlinePlayersOperator());
    }

    protected String key;
    protected String params;
    protected boolean reqParams;
    protected boolean negative = false;

    public ConditionalOperator(String key, boolean requireParams) {
        this.key = key;
        this.reqParams = requireParams;
    }

    public String getKey() {
        return this.key;
    }

    public boolean isKey(String input) {
        return input.equalsIgnoreCase(this.key);
    }

    public boolean isNegative() {
        return this.negative;
    }

    public void setNegative(boolean val) {
        this.negative = val;
    }

    public void passParameters(String params) {
        if ((!this.reqParams) || (!checkParameterValidity(params).isValid())) return;
        this.params = params;
    }


    public boolean evaluate(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        return isNegative() ^ meetsConditions(executor, executingSign, plugin);
    }

    public ConditionalOperator newInstance() {
        try {
            return getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public abstract ParameterValidityResponse checkParameterValidity(String paramString);

    protected abstract boolean meetsConditions(Player paramPlayer, ServerSign paramServerSign, ServerSignsPlugin paramServerSignsPlugin);

    public static class ParameterValidityResponse {
        boolean valid;
        String message;

        public ParameterValidityResponse(boolean isValid, String message) {
            this.valid = isValid;
            this.message = message;
        }

        public ParameterValidityResponse(boolean isValid) {
            this.valid = isValid;
            this.message = "";
        }

        public boolean isValid() {
            return this.valid;
        }

        public String getMessage() {
            return this.message;
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\operators\ConditionalOperator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */