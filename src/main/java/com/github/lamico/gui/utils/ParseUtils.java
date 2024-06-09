package com.github.lamico.gui.utils;

public class ParseUtils {
	public static int parseIntOrDefault(String str) {
		return str.strip().isEmpty() ? 0 : Integer.parseInt(str.strip());
	}

	public static double parseDoubleOrDefault(String str) {
		return str.strip().isEmpty() ? 0 : Double.parseDouble(str.strip());
	}
}
