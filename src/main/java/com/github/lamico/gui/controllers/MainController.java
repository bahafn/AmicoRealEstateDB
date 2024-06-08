package com.github.lamico.gui.controllers;

import java.io.IOException;

import com.github.lamico.gui.configurers.BottomBoxConfigurer;
import com.github.lamico.gui.configurers.StatusToolbarConfigurer;
import com.github.lamico.gui.configurers.TabToolbarConfigurer;
import com.github.lamico.gui.utils.Clock;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MainController {
	// Go to this class to set up your tab
	private static TabManager tabManager;

	@FXML
	private BorderPane bpRoot;

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
    private Button btOwners;

	@FXML
	private Button btTransactions;

	@FXML
	private ToolBar statusToolbar;

	@FXML
	private ToolBar tabToolbar;

	@FXML
	private HBox hbStatus;

	@FXML
	private Label lbTime;

	@FXML
	private Label lbUser;

    @FXML
    void switchToOwners(ActionEvent event) {
    	selectStyle((Button) event.getSource());
    	tabManager.switchTo(TabManager.OWNERS);
    }
    
	@FXML
	void switchToApartments(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.APARTMENTS);
	}

	@FXML
	void switchToBrokers(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.BROKERS);
	}

	@FXML
	void switchToBuildings(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.BUILDINGS);
	}

	@FXML
	void switchToClients(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.CLIENTS);
	}

	@FXML
	void switchToContracts(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.CONTRACTS);
	}

	@FXML
	void switchToDashboard(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.DASHBOARD);
	}

	@FXML
	void switchToEmployees(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.EMPLOYEES);
	}

	@FXML
	void switchToLand(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.LAND);
	}

	@FXML
	void switchToTransactions(ActionEvent event) {
		selectStyle((Button) event.getSource());
		tabManager.switchTo(TabManager.TRANSACTIONS);
	}

	@FXML
	void initialize() throws IOException {
		TabToolbarConfigurer.applyStyles(tabToolbar);
		StatusToolbarConfigurer.applyStyles(statusToolbar);
		BottomBoxConfigurer.applyStyles(hbStatus);

		Clock.makeClock(lbTime);
		lbTime.setStyle("-fx-text-fill: white;");

		MainController.tabManager = new TabManager(bpRoot);
		btDashboard.fire();
	}

	private static void updateTabStyles(Button selectedButton, Button otherButton) {
		otherButton.getStylesheets().clear();
		if (otherButton == selectedButton) {
			otherButton.getStylesheets().add(ResourceManager.getStylesheetURL("tab_button_selected"));
		}
	}

	private void selectStyle(Button selectedButton) {
		for (Node otherNode : tabToolbar.getItems()) {
			if (otherNode instanceof Button otherButton) {
				updateTabStyles(selectedButton, otherButton);
			}
		}
	}

	public static TabManager getTabManager() {
		return tabManager;
	}

}
