package au.com.addstar.serversigns.parsing.command;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.parsing.CommandParseException;
import au.com.addstar.serversigns.parsing.CommandType;
import au.com.addstar.serversigns.parsing.operators.ConditionalOperator;
import au.com.addstar.serversigns.signs.ServerSign;
import au.com.addstar.serversigns.taskmanager.TaskManagerTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class ConditionalServerSignCommand extends ServerSignCommand {
    private java.util.Set<ConditionalOperator> conditionalOperators = new HashSet<>();

    public ConditionalServerSignCommand(CommandType type, String command) throws CommandParseException {
        super(type, command);
        parseConditionalOperators();
    }

    private void parseConditionalOperators() throws CommandParseException {
        if (isEndifStatement()) return;
        if (!this.command.contains(":")) {
            throw new CommandParseException("Conditional Operators must follow the format <operator>:<params>");
        }

        boolean negative = this.command.startsWith("!");
        String key = this.command.substring(negative ? 1 : 0, this.command.indexOf(':'));
        String params = this.command.length() > this.command.indexOf(':') ? this.command.substring(this.command.indexOf(':') + 1) : "";


        for (ConditionalOperator condOp : ConditionalOperator.VALUES) {
            if (condOp.isKey(key)) {
                ConditionalOperator.ParameterValidityResponse response = condOp.checkParameterValidity(params);
                if (!response.isValid()) {
                    throw new CommandParseException("The Conditional Operator parameters are not valid - " + response.getMessage());
                }
                ConditionalOperator newInst = condOp.newInstance();
                newInst.passParameters(params);
                newInst.setNegative(negative);
                this.conditionalOperators.add(newInst);
                this.command = ((negative ? "!" : "") + newInst.getKey() + ":" + params);
                return;
            }
        }

        throw new CommandParseException("No known Conditional Operators with the key '" + key + "'");
    }

    public java.util.Set<ConditionalOperator> getConditionalOperators() {
        return this.conditionalOperators;
    }

    public boolean meetsAllConditions(Player executor, ServerSign executingSign, ServerSignsPlugin plugin) {
        for (ConditionalOperator condOp : this.conditionalOperators) {
            if (!condOp.evaluate(executor, executingSign, plugin)) {
                return false;
            }
        }
        return true;
    }

    public boolean isIfStatement() {
        return this.type == CommandType.CONDITIONAL_IF;
    }

    public boolean isEndifStatement() {
        return this.type == CommandType.CONDITIONAL_ENDIF;
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
        return new ArrayList<>();
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
        return new ArrayList<>();
    }
}


