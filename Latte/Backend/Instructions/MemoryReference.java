package Latte.Backend.Instructions;

import static java.lang.Math.abs;

public class MemoryReference {
    private static String offsetToString(int offset) {
        if (offset > 0) {
            return " + " + offset;
        }

        if (offset < 0) {
            return " - " + abs(offset);
        }

        return "";
    }

    private static String multipilerToString(int multiplier) {
        return multiplier + " * ";
    }

    public static String getWithOffset(String register, int offset) {
        return "[" + register + offsetToString(offset) + "]";
    }

    public static String get(String register, int multiplier, int offset) {
        return "[" + multipilerToString(multiplier) + register + offsetToString(offset) + "]";
    }

    public static String getWithMultiplier(String register, int multiplier) {
        return "[" + multipilerToString(multiplier) + register + "]";
    }
}
