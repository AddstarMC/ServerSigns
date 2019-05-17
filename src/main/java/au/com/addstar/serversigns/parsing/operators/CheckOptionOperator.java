package au.com.addstar.serversigns.parsing.operators;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.signs.PlayerInputOptions;
import au.com.addstar.serversigns.signs.ServerSign;

import java.util.Map;

import org.bukkit.entity.Player;

public class CheckOptionOperator extends ConditionalOperator {
    public CheckOptionOperator() {
        super("checkOption", true);
    }

    public ConditionalOperator.ParameterValidityResponse checkParameterValidity(String params) {
        boolean isValid = (params.indexOf('=') > 0) && (params.length() - 2 >= params.indexOf('='));
        return new ConditionalOperator.ParameterValidityResponse(isValid, "Parameter must be in the format <option id>=<answer label>");
    }

    public boolean meetsConditions(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        if ((this.params == null) || (this.params.indexOf('=') < 1)) {
            return false;
        }

        String optionId = this.params.substring(0, this.params.indexOf('='));
        String rawAnswer = this.params.substring(this.params.indexOf('=') + 1);
        String[] answerLabels = {rawAnswer.contains("|") ? rawAnswer.split("|") : rawAnswer};


        PlayerInputOptions options = executingSign.getInputOption(optionId);
        if (options != null) {
            Map<String, String> answers = plugin.inputOptionsManager.getCompletedAnswers(executor, false);
            if ((answers != null) && (answers.containsKey(optionId))) {
                for (String answer : answerLabels) {
                    if (answers.get(optionId).equalsIgnoreCase(answer)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\operators\CheckOptionOperator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */