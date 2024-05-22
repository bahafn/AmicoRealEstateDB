package com.github.lamico.gui.toolbars;

import com.github.lamico.managers.ResourceManager;

import javafx.geometry.Insets;
import javafx.scene.control.ToolBar;

public class StatusToolbarConfigurer {
	private StatusToolbarConfigurer() {
	}
	
	public static void applyStyles(ToolBar toolbar) {
		toolbar.getStylesheets().add(ResourceManager.getStylesheetURL("tab"));
		toolbar.setPadding(new Insets(0, 0, 0, 0));
		toolbar.setStyle("-fx-background-color: black;");
	}
}
