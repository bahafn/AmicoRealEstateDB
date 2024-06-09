package com.github.lamico.gui.utils;

/**
 * Utility class for formatting numbers.
 */
public class NumberFormatter {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private NumberFormatter() {
	}

	/**
	 * Formats a given number into a human-readable format.
	 * 
	 * @param value The number to be formatted.
	 * @return A string representation of the number in a human-readable format.
	 */
	public static String formatNumber(double value) {
		if (value >= 1_000_000) {
			return String.format("%.0fM", value / 1_000_000);
		} else if (value >= 1_000) {
			return String.format("%.1fK", value / 1_000);
		} else {
			return String.format("%.2f", value);
		}
	}
}