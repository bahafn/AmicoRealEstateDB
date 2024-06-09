package com.github.lamico.gui.utils;

/**
 * Utility class for parsing strings into primitive types.
 */
public class ParseUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ParseUtils() {}

    /**
     * Parses a string into an integer, returning a default value of 0 if the string is empty or cannot be parsed.
     * 
     * @param str The string to be parsed.
     * @return The parsed integer value, or 0 if parsing fails.
     */
    public static int parseIntOrDefault(String str) {
        // Remove leading and trailing whitespace from the input string
        str = str.strip();
        // Return 0 if the string is empty, otherwise attempt to parse the integer value
        return str.isEmpty() ? 0 : Integer.parseInt(str);
    }

    /**
     * Parses a string into a double, returning a default value of 0 if the string is empty or cannot be parsed.
     * 
     * @param str The string to be parsed.
     * @return The parsed double value, or 0 if parsing fails.
     */
    public static double parseDoubleOrDefault(String str) {
        // Remove leading and trailing whitespace from the input string
        str = str.strip();
        // Return 0 if the string is empty, otherwise attempt to parse the double value
        return str.isEmpty() ? 0 : Double.parseDouble(str);
    }
}