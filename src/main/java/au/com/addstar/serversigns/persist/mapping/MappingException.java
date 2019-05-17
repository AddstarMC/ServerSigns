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


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\persist\mapping\MappingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */