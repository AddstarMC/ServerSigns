package au.com.addstar.serversigns.utils;

public class CurrentTime {
    private static ITimeProvider timeProvider = new SystemClockTimeProvider();

    public static long get() {
        return timeProvider.getTime();
    }

    public static void setTimeProvider(ITimeProvider timeProvider) {
        timeProvider = timeProvider;
    }


    public interface ITimeProvider {
        long getTime();
    }

    private static class SystemClockTimeProvider implements CurrentTime.ITimeProvider {
        public long getTime() {
            return System.currentTimeMillis();
        }
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\utils\CurrentTime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */