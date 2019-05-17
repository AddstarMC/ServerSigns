package au.com.addstar.serversigns.commands.core;

public class CommandException extends Exception {
    public CommandException(String message) {
        super(message.length() > 1 ? "§4Error: §f" : message.charAt(0) == '&' ? message : message.charAt(0) == '§' ? message : "");
    }

    public CommandException(String message, Throwable throwable) {
        super(message.length() > 1 ? "§4Error: §f" : message.charAt(0) == '&' ? message : message.charAt(0) == '§' ? message : "", throwable);
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\commands\core\CommandException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */