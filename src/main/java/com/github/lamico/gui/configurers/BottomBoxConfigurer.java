package com.github.lamico.gui.configurers;

import com.github.lamico.managers.ResourceManager;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Configures the styles for the bottom hbox.
 */
public class BottomBoxConfigurer {
	/**
	 * Private constructor to prevent instantiation.
	 */
	private BottomBoxConfigurer() {
	}

	/**
	 * Applies styles to the specified HBox.
	 *
	 * @param hbox the HBox to apply styles to
	 */
	public static void applyStyles(HBox hbox) {
		ImageView lamicoIcon = new ImageView(ResourceManager.getImage("lamico_white_txt.png"));
		lamicoIcon.setFitWidth(100);
		lamicoIcon.setFitHeight(35);
		hbox.getChildren().add(lamicoIcon);
		hbox.setStyle("-fx-background-color: black;");
	}
}
