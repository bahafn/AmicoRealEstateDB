package com.github.lamico.gui.utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Clock {
	/**
	 * A utility class for creating a clock that updates in real-time.
	 */
	private Clock() {
	}

	/**
	 * Creates a clock that updates in real-time and displays it in the specified
	 * Label.
	 *
	 * @param clockLabel the Label to display the clock in
	 */
	public static void makeClock(Label clockLabel) {
		// Create a Timeline that updates the clockLabel every second
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			// Update the clockLabel with the current time
			clockLabel.setText(
					java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
		}), new KeyFrame(Duration.seconds(1)));
		
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}
}
