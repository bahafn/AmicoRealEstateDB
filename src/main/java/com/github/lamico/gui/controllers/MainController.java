package com.github.lamico.gui.controllers;

import com.github.lamico.gui.toolbars.StatusToolbarConfigurer;
import com.github.lamico.gui.toolbars.TabToolbarConfigurer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;

public class MainController {

	@FXML
	private Button btApartments;

	@FXML
	private Button btBrokers;

	@FXML
	private Button btBuildings;

	@FXML
	private Button btClients;

	@FXML
	private Button btContracts;

	@FXML
	private Button btDashboard;

	@FXML
	private Button btEmployees;

	@FXML
	private Button btLand;

	@FXML
	private Button btTransactions;

	@FXML
	private StackPane spMainScreen;

	@FXML
	private ToolBar statusToolbar;

	@FXML
	private ToolBar tabToolbar;

	@FXML
	void switchToApartments(ActionEvent event) {

	}

	@FXML
	void switchToBrokers(ActionEvent event) {

	}

	@FXML
	void switchToBuildings(ActionEvent event) {

	}

	@FXML
	void switchToClients(ActionEvent event) {

	}

	@FXML
	void switchToContracts(ActionEvent event) {

	}

	@FXML
	void switchToDashboard(ActionEvent event) {

	}

	@FXML
	void switchToEmployees(ActionEvent event) {

	}

	@FXML
	void switchToLand(ActionEvent event) {

	}

	@FXML
	void switchToTransactions(ActionEvent event) {

	}

	@FXML
	void initialize() {
		TabToolbarConfigurer.applyStyles(tabToolbar);
		StatusToolbarConfigurer.applyStyles(statusToolbar);
	}

}
