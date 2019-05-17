package au.com.addstar.serversigns.translations;

public class NoDefaultException extends Exception {
    public NoDefaultException(String message) {
        super(message);
    }

    public NoDefaultException(String message, Throwable thrown) {
        super(message, thrown);
    }
}
