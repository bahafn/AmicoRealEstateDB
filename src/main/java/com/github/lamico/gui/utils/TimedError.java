package com.github.lamico.gui.utils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Utility class for displaying timed error messages.
 */
public class TimedError {

	/**
	 * The pause transition used to control the duration of the error message.
	 */
	private PauseTransition pauseTransition;

	/**
	 * Displays an error message on a label for a specified duration.
	 * 
	 * @param label             The label to display the error message on.
	 * @param errorMessage      The error message to display.
	 * @param durationInSeconds The duration in seconds to display the error
	 *                          message.
	 */
	public void displayErrorMessage(Label label, String errorMessage, int durationInSeconds) {
		Platform.runLater(() -> {
			if (pauseTransition != null) {
				pauseTransition.getOnFinished().handle(null);
				pauseTransition.stop();
			}

			label.setText(errorMessage);

			pauseTransition = new PauseTransition(Duration.seconds(durationInSeconds));

			pauseTransition.setOnFinished(event -> label.setText(""));

			pauseTransition.play();
		});
	}
}