package au.com.addstar.serversigns.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeUtils {
    private static final Pattern TIME_INPUT_PATTERN = Pattern.compile("^(?i)(t:)*(\\d+)(mo|s|m|h|d|w)*$");

    public static String getCurrentTime() {
        return getCurrentTime("MM-dd hh:mm:ss a");
    }

    public static String getCurrentTime(String format) {
        return getFormattedTime(System.currentTimeMillis(), format);
    }

    public static boolean isInLast(long timestamp, long milliseconds) {
        return System.currentTimeMillis() - timestamp <= milliseconds;
    }

    public static boolean isInLast(long timestamp, int amount, TimeUnit unit) {
        return System.currentTimeMillis() - timestamp <= amount * unit.getMultiplier();
    }


    public static String getFormattedTime(long mili, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(mili);
        return dateFormat.format(date);
    }

    public static String getFormattedTime(long mili) {
        return getFormattedTime(mili, "MM-dd hh:mm:ss a");
    }


    public static String getTimeRemaining(long timeout) {
        long span = timeout - System.currentTimeMillis();

        return getTimeSpan(span);
    }


    public static String getTimeSpan(long timeInMilis) {
        StringBuilder time = new StringBuilder();
        double[] timesList = new double[4];
        String[] unitsList = {"second", "minute", "hour", "day"};

        timesList[0] = (timeInMilis / 1000L);
        timesList[1] = Math.floor(timesList[0] / 60.0D);
        timesList[0] -= timesList[1] * 60.0D;
        timesList[2] = Math.floor(timesList[1] / 60.0D);
        timesList[1] -= timesList[2] * 60.0D;
        timesList[3] = Math.floor(timesList[2] / 24.0D);
        timesList[2] -= timesList[3] * 24.0D;

        for (int j = 3; j > -1; j--) {
            double d = timesList[j];
            if (d >= 1.0D) {
                time.append((int) d).append(" ").append(unitsList[j]).append(d > 1.0D ? "s " : " ");
            }
        }
        return time.toString().trim();
    }


    public static String getTimeSpan(long timeInMilis, TimeUnit minUnit, TimeUnit maxUnit, boolean shortUnits, boolean printZeros) {
        StringBuilder time = new StringBuilder();

        for (TimeUnit unit : TimeUnit.values()) {
            if ((unit.isEqualOrMoreAccurateThan(maxUnit)) && (unit.isEqualOrLessAccurateThan(minUnit))) {
                long unitValue = timeInMilis / unit.getMultiplier();
                if (unitValue > 0L) {
                    time.append(unitValue).append(" " + (unitValue > 1L ? unit.getLongPlural() : unit.getLongSingular()) + " ");
                } else {
                    time.append(printZeros ? "0" + (shortUnits ? unit.getShortAlias() + " " : new StringBuilder().append(" ").append(unit.getLongPlural()).append(" ").toString()) : "");
                }
                timeInMilis %= unit.getMultiplier();
            }
        }

        if (time.toString().trim().isEmpty()) {
            return "0 " + minUnit.getLongPlural();
        }

        return time.toString().trim();
    }

    public static long getTimeoutFromString(String stringFormat) {
        return System.currentTimeMillis() + getLengthFromString(stringFormat);
    }

    public static long getLengthFromString(String stringFormat) {
        try {
            Matcher matcher = TIME_INPUT_PATTERN.matcher(stringFormat);
            if (matcher.matches()) {
                return NumberUtils.parseInt(matcher.group(2), 0) * (matcher.group(3) != null ? TimeUnit.match(matcher.group(3)).getMultiplier() : 1000L);
            }
        } catch (Exception ex) {
            return 0L;
        }

        return 0L;
    }

    public static long convertDSDDFToEpochMillis(String input, TimeZone timeZone) {
        if (input.length() != 13) {
            return 0L;
        }

        int[] output = new int[6];

        int j = 0;
        for (int k = 0; k < 12; k += 2) {
            if (k == 6) {
                k--;
            } else {
                output[(j++)] = NumberUtils.parseInt(input.substring(k, k + 2));
            }
        }

        Calendar date = Calendar.getInstance();
        date.setTimeZone(timeZone);
        date.set(output[2] + 2000, output[1] - 1, output[0], output[3], output[4], output[5]);
        return date.getTimeInMillis();
    }


    public enum TimeUnit {
        YEARS(7, 29030400000L, "year", "years", "y"),
        MONTHS(6, 2419200000L, "month", "months", "mo"),
        WEEKS(5, 604800000L, "week", "weeks", "w"),
        DAYS(4, 86400000L, "day", "days", "d"),
        HOURS(3, 3600000L, "hour", "hours", "h", "hr", "hrs"),
        MINUTES(2, 60000L, "minute", "minutes", "m", "min", "mins"),
        SECONDS(1, 1000L, "second", "seconds", "s", "sec", "secs"),
        MILLISECONDS(0, 1L, "millisecond", "milliseconds", "ms");

        String[] Aliases;
        long Multi;
        int id;

        TimeUnit(int id, long multi, String... aliases) {
            this.id = id;
            this.Aliases = aliases;
            this.Multi = multi;
        }

        public static TimeUnit match(String input) {
            for (TimeUnit unit : values()) {
                if (unit.matches(input)) {
                    return unit;
                }
            }
            return null;
        }

        public int getId() {
            return this.id;
        }

        public String[] getAliases() {
            return this.Aliases;
        }

        public String getLongSingular() {
            return this.Aliases[0];
        }

        public String getLongPlural() {
            return this.Aliases[1];
        }

        public String getShortAlias() {
            return this.Aliases[2];
        }

        public long getMultiplier() {
            return this.Multi;
        }

        public boolean isMoreAccurateThan(TimeUnit other) {
            return this.id < other.getId();
        }

        public boolean isLessAccurateThan(TimeUnit other) {
            return this.id > other.getId();
        }

        public boolean isEqualOrMoreAccurateThan(TimeUnit other) {
            return this.id <= other.getId();
        }

        public boolean isEqualOrLessAccurateThan(TimeUnit other) {
            return this.id >= other.getId();
        }

        public boolean matches(String input) {
            for (String string : this.Aliases) {
                if (input.equalsIgnoreCase(string)) {
                    return true;
                }
            }
            return false;
        }
    }
}
