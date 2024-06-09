package com.github.lamico.gui.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class for displaying alerts in the GUI.
 */
public class AlertUtil {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private AlertUtil() {
	}

	/**
	 * Displays an alert with the specified type, header, and content.
	 * 
	 * @param type    The type of alert to display (e.g. ERROR, WARNING,
	 *                INFORMATION, CONFIRMATION)
	 * @param header  The header text of the alert
	 * @param content The content text of the alert
	 */
	public static void showAlert(AlertType type, String header, String content) {
		Alert alert = new Alert(null);

		alert.setAlertType(type);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.show();
	}
}