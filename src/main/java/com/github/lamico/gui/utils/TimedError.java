package com.github.lamico.gui.utils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimedError {
	private PauseTransition pauseTransition;

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
