package au.com.addstar.serversigns.parsing;

import au.com.addstar.serversigns.ServerSignsPlugin;
import au.com.addstar.serversigns.parsing.command.ReturnServerSignCommand;
import au.com.addstar.serversigns.parsing.command.ServerSignCommand;
import au.com.addstar.serversigns.utils.TimeUtils;
import au.com.addstar.serversigns.parsing.command.ConditionalServerSignCommand;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ServerSignCommandFactory {
    private static final Map<CommandType, AliasSet> TYPE_ALIAS_MAP = new java.util.HashMap();
    private static final Pattern PERMISSION_PATTERN = Pattern.compile("\\[p:([\\S]*)\\]");
    private static final Pattern DELAY_PATTERN = Pattern.compile("\\[d:(\\d*)(\\w*)\\]");
    private static final Pattern AP_PATTERN = Pattern.compile("\\[ap:(\\w*)\\]");
    private static final Pattern CLICK_PATTERN = Pattern.compile("\\[click:(\\w*)\\]");
    private static final Pattern TYPE_PATTERN = Pattern.compile("^<(\\w+)>");

    static {
        TYPE_ALIAS_MAP.put(CommandType.ADD_GROUP, new AliasSet("addGroup", "addG"));
        TYPE_ALIAS_MAP.put(CommandType.BLANK_MESSAGE, new AliasSet("blank", "bl"));
        TYPE_ALIAS_MAP.put(CommandType.BROADCAST, new AliasSet("broadcast", "bcast"));
        TYPE_ALIAS_MAP.put(CommandType.CHAT, new AliasSet("chat", "say"));
        TYPE_ALIAS_MAP.put(CommandType.DEL_GROUP, new AliasSet("removeGroup", "remGroup", "remG"));
        TYPE_ALIAS_MAP.put(CommandType.MESSAGE, new AliasSet("message", "msg", "m"));
        TYPE_ALIAS_MAP.put(CommandType.PERMISSION_GRANT, new AliasSet("addPermission", "addPerm", "addP"));
        TYPE_ALIAS_MAP.put(CommandType.PERMISSION_REMOVE, new AliasSet("removePermission", "remPerm", "remP"));
        TYPE_ALIAS_MAP.put(CommandType.SERVER_COMMAND, new AliasSet("server", "srv", "s"));
        TYPE_ALIAS_MAP.put(CommandType.CANCEL_TASKS, new AliasSet("canceltasks", "canceltask", "ctasks"));
        TYPE_ALIAS_MAP.put(CommandType.DISPLAY_OPTIONS, new AliasSet("displayOptions", "displayOption", "disopt"));
    }

    public static ServerSignCommand getCommandFromString(String input, ServerSignsPlugin plugin)
            throws CommandParseException {
        if (input.trim().startsWith("<if>")) {
            InteractResult result = getInteractValue(plugin, input);
            ConditionalServerSignCommand command = new ConditionalServerSignCommand(CommandType.CONDITIONAL_IF, result.getInputValue().trim().substring(4).trim());
            command.setInteractValue(result.getInteractValue());
            return command;
        }
        if (input.trim().startsWith("<endif>")) {
            return new ConditionalServerSignCommand(CommandType.CONDITIONAL_ENDIF, "");
        }


        if (input.trim().startsWith("<return>")) {
            return new ReturnServerSignCommand();
        }


        long delay = 0L;
        Matcher matcher;
        if ((matcher = DELAY_PATTERN.matcher(input)).find()) {
            if (!matcher.group(1).isEmpty()) {
                delay = TimeUtils.getLengthFromString(matcher.group(1) + matcher.group(2)) / 1000L;
                if (delay <= 0L)
                    throw new CommandParseException("Invalid delay parameter - [d:#<s|m|h|d|w|mo>] is expected (# must be > 0)");
            }
            input = matcher.replaceFirst("");
        }


        List<String> permissions = new java.util.ArrayList();
        if ((matcher = PERMISSION_PATTERN.matcher(input)).find()) {
            if (!matcher.group(1).isEmpty()) {
                if (matcher.group(1).contains("|")) {
                    permissions = java.util.Arrays.asList(matcher.group(1).split(Pattern.quote("|")));
                } else {
                    permissions.add(matcher.group(1));
                }
            }
            input = matcher.replaceFirst("");
        }


        boolean alwaysPersist = false;
        if ((matcher = AP_PATTERN.matcher(input)).find()) {
            if (!matcher.group(1).isEmpty()) {
                if ((matcher.group(1).equalsIgnoreCase("true")) || (matcher.group(1).equalsIgnoreCase("false"))) {
                    alwaysPersist = matcher.group(1).equalsIgnoreCase("true");
                } else {
                    throw new CommandParseException("Invalid ap parameter - [ap:<true|false>] is expected");
                }
            }
            input = matcher.replaceFirst("");
        }


        InteractResult result = getInteractValue(plugin, input);
        int interactValue = result.getInteractValue();
        input = result.getInputValue().trim();


        CommandType type = null;
        if ((matcher = TYPE_PATTERN.matcher(input)).find()) {
            Iterator<Map.Entry<CommandType, AliasSet>> iteratorForCaliber = TYPE_ALIAS_MAP.entrySet().iterator();
            while ((iteratorForCaliber.hasNext()) && (type == null)) {
                Map.Entry<CommandType, AliasSet> entry = iteratorForCaliber.next();
                if (entry.getValue().matches(matcher.group(1))) {
                    type = entry.getKey();
                }
            }
            if (type == null) {
                throw new CommandParseException("Invalid command type - no types known as '" + matcher.group(1) + "'");
            }
            input = matcher.replaceFirst("");
        } else if (input.startsWith("*")) {
            input = input.substring(1);
            type = CommandType.OP_COMMAND;
        } else {
            type = CommandType.PLAYER_COMMAND;
        }


        if ((type == CommandType.CANCEL_TASKS) && (input.trim().length() > 0)) {
            try {
                Pattern.compile(input.trim());
            } catch (PatternSyntaxException ex) {
                throw new CommandParseException("Invalid regular expression pattern - " + ex.getMessage());
            }
        }


        ServerSignCommand cmd = new ServerSignCommand(type, input.trim());
        cmd.setDelay(delay);
        cmd.setGrantPermissions(permissions);
        cmd.setAlwaysPersisted(alwaysPersist);
        cmd.setInteractValue(interactValue);

        return cmd;
    }

    private static InteractResult getInteractValue(ServerSignsPlugin plugin, String input) throws CommandParseException {
        int val = 0;
        Matcher matcher = CLICK_PATTERN.matcher(input);
        if (matcher.find()) {
            if (!matcher.group(1).isEmpty()) {
                if ((matcher.group(1).equalsIgnoreCase("left")) || (matcher.group(1).equalsIgnoreCase("l"))) {
                    if ((plugin != null) && (!plugin.getServerSignsConfig().getAllowLeftClicking())) {
                        throw new CommandParseException("'allow_left_clicking' must be set to true in config.yml for left-click commands!");
                    }
                    val = 1;
                } else if ((matcher.group(1).equalsIgnoreCase("right")) || (matcher.group(1).equalsIgnoreCase("r"))) {
                    val = 2;
                } else if ((matcher.group(1).equalsIgnoreCase("both")) || (matcher.group(1).equalsIgnoreCase("b"))) {
                    val = 0;
                } else {
                    throw new CommandParseException("Invalid click parameter - [click:<left|right|both>] is expected");
                }
            }
            input = matcher.replaceFirst("");
        }
        return new InteractResult(input, val);
    }

    protected static class InteractResult {
        int interactValue;
        String inputValue;

        public InteractResult(String input, int interact) {
            this.interactValue = interact;
            this.inputValue = input;
        }

        public int getInteractValue() {
            return this.interactValue;
        }

        public String getInputValue() {
            return this.inputValue;
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\ServerSignCommandFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */