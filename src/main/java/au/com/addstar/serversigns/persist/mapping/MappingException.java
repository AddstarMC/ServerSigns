package au.com.addstar.serversigns.persist.mapping;

public class MappingException extends Exception {
    private ExceptionType type;

    public MappingException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public ExceptionType getType() {
        return this.type;
    }

    public enum ExceptionType {
        COMMANDS;

        ExceptionType() {
        }
    }
}


