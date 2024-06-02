package com.github.lamico.gui.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/** This class is used when ever we want to show an Alert. */
public class AlertUtil {
    private static Alert alert = new Alert(null);

    private AlertUtil() {}

    public static void showAlert(AlertType type, String header, String content) {
        alert.setAlertType(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
