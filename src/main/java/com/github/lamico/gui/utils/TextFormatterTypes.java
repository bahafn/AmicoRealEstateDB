package com.github.lamico.gui.utils;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class TextFormatterTypes {
	// Private constructor to prevent instantiation
	private TextFormatterTypes() {
	}

	// Regular expression patterns for different types of input
	private static final Pattern INTEGER_PATTERN = Pattern.compile("\\d*");
	private static final Pattern ALPHA_WORD_CHARS_PATTERN = Pattern.compile("[a-zA-Z -]*");
	private static final Pattern ALPHANUMERIC_WORD_CHARS_PATTERN = Pattern.compile("\\w -*");
	private static final Pattern ALPHANUMERIC_WORD_CHARS_AND_COMMAS_PATTERN = Pattern.compile("[\\w, -]*");
	private static final Pattern SQL_DATE_PATTERN = Pattern.compile("\\d{0,4}(-\\d{0,2}){0,2}");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+(?:@[a-zA-Z.]*)?");
	private static final Pattern DOUBLE_PATTERN = Pattern.compile("\\d+(\\.\\d*)?");

	/**
	 * Returns a TextFormatter that restricts input to integers with a maximum
	 * length of maxLength.
	 * If maxLength is 0, there is no length restriction.
	 * 
	 * @param maxLength the maximum length of the input
	 * @return a TextFormatter that restricts input to integers
	 */
	public static TextFormatter<Integer> getIntFormatter(int maxLength) {
		return new TextFormatter<>(c -> INTEGER_PATTERN.matcher(c.getControlNewText()).matches()
				&& (maxLength == 0 || c.getControlNewText().length() <= maxLength) ? c : null);
	}

	/**
	 * Returns a TextFormatter that restricts input to alphanumeric characters,
	 * spaces, and hyphens with a maximum length of maxLength.
	 * If maxLength is 0, there is no length restriction.
	 * 
	 * @param maxLength the maximum length of the input
	 * @return a TextFormatter that restricts input to alphanumeric characters,
	 *         spaces, and hyphens
	 */
	public static TextFormatter<Integer> getAlphaWordCharsFormatter(int maxLength) {
		return new TextFormatter<>(c -> ALPHA_WORD_CHARS_PATTERN.matcher(c.getControlNewText()).matches()
				&& (maxLength == 0 || c.getControlNewText().length() <= maxLength) ? c : null);
	}

	/**
	 * Returns a TextFormatter that restricts input to alphanumeric characters and
	 * hyphens with a maximum length of maxLength.
	 * If maxLength is 0, there is no length restriction.
	 * 
	 * @param maxLength the maximum length of the input
	 * @return a TextFormatter that restricts input to alphanumeric characters and
	 *         hyphens
	 */
	public static TextFormatter<Integer> getAlphanumericWordCharsFormatter(int maxLength) {
		return new TextFormatter<>(c -> ALPHANUMERIC_WORD_CHARS_PATTERN.matcher(c.getControlNewText()).matches()
				&& (maxLength == 0 || c.getControlNewText().length() <= maxLength) ? c : null);
	}

	/**
	 * Returns a TextFormatter that restricts input to alphanumeric characters,
	 * hyphens, and commas with a maximum length of maxLength.
	 * If maxLength is 0, there is no length restriction.
	 * 
	 * @param maxLength the maximum length of the input
	 * @return a TextFormatter that restricts input to alphanumeric characters,
	 *         hyphens, and commas
	 */
	public static TextFormatter<Integer> getAlphanumericWordCharsAndCommasFormatter(int maxLength) {
		return new TextFormatter<>(
				c -> ALPHANUMERIC_WORD_CHARS_AND_COMMAS_PATTERN.matcher(c.getControlNewText()).matches()
						&& (maxLength == 0 || c.getControlNewText().length() <= maxLength) ? c : null);
	}

	/**
	 * Returns a TextFormatter that restricts input to a SQL date format
	 * (YYYY-MM-DD).
	 * The formatter also automatically inserts dashes at the correct positions.
	 * 
	 * @return a TextFormatter that restricts input to a SQL date format
	 */
	public static TextFormatter<String> getSQLDateFormatter() {
		UnaryOperator<Change> filter = c -> {
			String newText = c.getControlNewText();
			// Replace spaces with dashes
			newText = newText.replace(" ", "-");
			c.setText(c.getText().replace(" ", "-"));
			// Ensure a dash is only ever typed at index 6
			if ("-".equals(c.getText()) && newText.lastIndexOf("-") != 6) {
				return null;
			}

			if (SQL_DATE_PATTERN.matcher(newText).matches()) {
				if ((newText.length() == 4 || newText.length() == 7) && newText.charAt(newText.length() - 1) != '-'
						&& (newText.indexOf("-") >= 4 || newText.indexOf("-") == -1)) {
					c.setText(c.getText() + "-");
					c.setCaretPosition(c.getControlNewText().length());
					c.setAnchor(c.getControlNewText().length());
				}
				return c;
			} else {
				return null;
			}
		};
		return new TextFormatter<>(filter);
	}

	/**
	 * Returns a TextFormatter that restricts input to a maximum length of
	 * maxLength.
	 * If maxLength is 0, there is no length restriction.
	 * 
	 * @param maxLength the maximum length of the input
	 * @return a TextFormatter that restricts input to a maximum length
	 */
	public static TextFormatter<Integer> getMaxLengthFormatter(int maxLength) {
		if (maxLength == 0) {
			return new TextFormatter<>(c -> c);
		} else {
			return new TextFormatter<>(c -> c.getControlNewText().length() <= maxLength ? c : null);
		}
	}

	/**
	 * Creates a TextFormatter that restricts input to a valid email address.
	 * 
	 * @return Created TextFormatter Object.
	 */
	public static TextFormatter<String> getEmailTextFormatter(int maxLength) {
		UnaryOperator<Change> filter = c -> {
			String newText = c.getControlNewText(); // Get control text

			if (newText.isEmpty() || EMAIL_PATTERN.matcher(newText).matches())
				return c;

			return null;
		};

		return new TextFormatter<>(filter);
	}

	/**
	 * Creates a TextFormatter that restricts input to double values.
	 * 
	 * @return Created TextFormatter Object.
	 */
	public static TextFormatter<Double> getDoubleTextFormatter(int maxLength) {
		UnaryOperator<Change> filter = c -> {
			String newText = c.getControlNewText();

			if (newText.isEmpty() || DOUBLE_PATTERN.matcher(newText).matches())
				return c;

			return null;
		};

		return new TextFormatter<>(filter);
	}

	/**
	 * Creates a TextFormatter that restricts input to decimal values with specific
	 * precision and scale.
	 * 
	 * @return Created TextFormatter Object.
	 */
	public static TextFormatter<Double> getDecimalTextFormatter(int precision, int scale) {
		String pattern = "(-?\\d{0," + (precision - scale) + "}(\\.(\\d{0," + scale + "})?)?)";
		Pattern decimalPattern = Pattern.compile(pattern);

		UnaryOperator<Change> filter = c -> {
			String newText = c.getControlNewText();

			if (newText.isEmpty() || decimalPattern.matcher(newText).matches())
				return c;

			return null;
		};

		return new TextFormatter<>(filter);
	}
}