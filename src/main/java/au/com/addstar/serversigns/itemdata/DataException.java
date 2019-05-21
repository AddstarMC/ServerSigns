package au.com.addstar.serversigns.itemdata;

public class DataException extends Exception {
    protected static final String DEFAULT_MESSAGE = "Invalid value passed to ItemData parser!";
    private static final long serialVersionUID = -9036202694165168984L;

    public DataException() {
        super("Invalid value passed to ItemData parser!");
    }

    public DataException(Throwable throwable) {
        super("Invalid value passed to ItemData parser!", throwable);
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}


