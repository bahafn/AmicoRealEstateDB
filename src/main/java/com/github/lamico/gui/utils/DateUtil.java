package com.github.lamico.gui.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;

/**
 * Utility class for converting between java.util.Date and java.sql.Date, and
 * java.time.LocalDate.
 */
public class DateUtil {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private DateUtil() {
	}

	/**
	 * Converts a java.util.Date to a java.time.LocalDate.
	 * 
	 * @param date The java.util.Date to convert
	 * @return The equivalent java.time.LocalDate, or null if the input is null
	 */
	public static LocalDate sqlDateToLocalDate(java.util.Date date) {
		return date != null ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}

	/**
	 * Converts a java.time.LocalDate to a java.sql.Date.
	 * 
	 * @param date The java.time.LocalDate to convert
	 * @return The equivalent java.sql.Date, or null if the input is null
	 */
	public static Date localDateToSQLDate(LocalDate date) {
		return date != null ? Date.valueOf(date) : null;
	}
}