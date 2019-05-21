package au.com.addstar.serversigns.commands.core;

public class CommandException extends Exception {
    public CommandException(String message) {
        super(message.length() > 1 ? "§4Error: §f" : message.charAt(0) == '&' ? message : message.charAt(0) == '§' ? message : "");
    }

    public CommandException(String message, Throwable throwable) {
        super(message.length() > 1 ? "§4Error: §f" : message.charAt(0) == '&' ? message : message.charAt(0) == '§' ? message : "", throwable);
    }
}