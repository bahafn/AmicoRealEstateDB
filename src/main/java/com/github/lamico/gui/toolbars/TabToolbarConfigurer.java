package com.github.lamico.gui.toolbars;

import com.github.lamico.managers.ResourceManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class TabToolbarConfigurer {
	private TabToolbarConfigurer() {
	}
	
	public static void applyStyles(ToolBar toolbar) {
		toolbar.getStylesheets().add(ResourceManager.getStylesheetURL("tab"));
		toolbar.getStylesheets().add(ResourceManager.getStylesheetURL("tab_button"));
		toolbar.setPadding(new Insets(0, 0, 0, 0));
		toolbar.setStyle("-fx-background-color: rgb(117, 0, 0);");
		setTabStyles(toolbar);
	}

	public static void setTabStyles(ToolBar toolbar) {
		for (Node node : toolbar.getItems()) {
			if (node instanceof Button button) {
				button.setOnAction(event -> {
					for (Node otherNode : toolbar.getItems()) {
						if (otherNode instanceof Button otherButton) {
							updateTabStyles(button, otherButton);
						}
					}
				});
			}
		}
	}

	private static void updateTabStyles(Button selectedButton, Button otherButton) {
		otherButton.getStylesheets().clear();
		if (otherButton == selectedButton) {
			otherButton.getStylesheets()
					.add(ResourceManager.getStylesheetURL("tab_button_selected"));
		} else {
			otherButton.getStylesheets().add(ResourceManager.getStylesheetURL("tab_button"));
		}
	}
}
