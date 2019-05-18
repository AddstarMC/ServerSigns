package au.com.addstar.serversigns.config;

public class ConfigLoadingException extends Exception {
    public ConfigLoadingException(String message) {
        super(message);
    }

    public ConfigLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
