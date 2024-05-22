package com.github.lamico.gui.configurers;

import com.github.lamico.managers.ResourceManager;

import javafx.geometry.Insets;
import javafx.scene.control.ToolBar;

/**
 * A utility class for configuring the styles of a tab toolbar.
 */
public class TabToolbarConfigurer {
	/**
	 * Private constructor to prevent instantiation.
	 */
	private TabToolbarConfigurer() {
	}

	/**
	 * Applies styles to the specified ToolBar.
	 *
	 * @param toolbar the ToolBar to apply styles to
	 */
	public static void applyStyles(ToolBar toolbar) {
		toolbar.getStylesheets().add(ResourceManager.getStylesheetURL("tab"));
		toolbar.getStylesheets().add(ResourceManager.getStylesheetURL("tab_button"));
		toolbar.setPadding(new Insets(0, 0, 0, 0));
		toolbar.setStyle("-fx-background-color: rgb(117, 0, 0);");
	}

}
