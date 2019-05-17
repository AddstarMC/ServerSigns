package au.com.addstar.serversigns.utils;

import au.com.addstar.serversigns.ServerSignsPlugin;

public class NumberUtils {
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    public static int parseInt(String string) {
        int i;

        try {
            i = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return -1;
        }
        return i;
    }

    public static int parseInt(String string, int def) {
        int i;
        try {
            i = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return def;
        }
        return i;
    }

    public static double parseDouble(String string) {
        double i;
        try {
            i = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return -1.0D;
        }
        return i;
    }

    public static int randomBetweenInclusive(int from, int to) {
        if (from == to)
            return from;
        if (to < from) {
            from ^= to;
            to = from ^ to;
            from ^= to;
        }

        return random(from, to + 1);
    }

    public static int random(int start, int end) {
        if (end <= start) return start;
        return start + ServerSignsPlugin.r.nextInt(end - start);
    }
}
