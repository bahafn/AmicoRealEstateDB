package com.github.lamico.gui.controllers;

import com.github.lamico.managers.TabManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BuildingsController {

	@FXML
	private Button btRegisterNew;

	@FXML
	void registerNewProperty(ActionEvent event) {
		getPropertyController()
				.showRegisterScreen(TabManager.BUILDINGS);
	}

	public PropertyController getPropertyController() {
		return (PropertyController) MainController.getTabManager().getController(TabManager.PROPERTY);
	}

	@FXML
	void initialize() {
	}
}
