package com.github.lamico.gui.utils;

public class NumberFormatter {

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
