package com.github.lamico.gui.controllers;

import java.util.HashMap;
import java.util.Map;

import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;

public class PropertyController {
	Map<String, Background> backgroundMap = new HashMap<>();

	@FXML
	private Button btRegister;

	@FXML
	private BorderPane root;

	@FXML
	private RadioButton rbShowOwners;

	@FXML
	private TableView<?> tbvTable;

	@FXML
	private TableColumn<?, ?> tvEmail;

	@FXML
	private TableColumn<?, ?> tvName;

	@FXML
	private TableColumn<?, ?> tvSSN;

	@FXML
	private TextField txtArea;

	@FXML
	private TextField txtCity;

	@FXML
	private TextField txtCondition;

	@FXML
	private TextArea txtDescription;

	@FXML
	private TextField txtOwner;

	@FXML
	private TextField txtStreet;

	@FXML
	private TextField txtValuation;

	private String backTo;

	@FXML
	void displayOwnerTable(ActionEvent event) {
		if (rbShowOwners.isSelected()) {
			tbvTable.setVisible(true);
			tbvTable.setDisable(false);
		} else {
			tbvTable.setVisible(false);
			tbvTable.setDisable(true);
			tbvTable.getSelectionModel().clearSelection();
		}
	}

	@FXML
	void register(ActionEvent event) {
		MainController.getTabManager().switchTo(backTo);
		rbShowOwners.selectedProperty().set(false);
	}

	@FXML
	void initialize() {
		backgroundMap.put(TabManager.BUILDINGS, ResourceManager.getBackground("buildings.jpg"));
		backgroundMap.put(TabManager.APARTMENTS, ResourceManager.getBackground("flat.jpg"));
		backgroundMap.put(TabManager.LAND, ResourceManager.getBackground("land.png"));		
	}

	public void showRegisterScreen(String from) {
		root.setBackground(backgroundMap.get(from));
		
		txtArea.clear();
		txtCity.clear();
		txtCondition.clear();
		txtDescription.clear();
		txtOwner.clear();
		txtStreet.clear();
		txtValuation.clear();
		
		this.backTo = from;
		
		MainController.getTabManager().switchTo(TabManager.PROPERTY);
	}

}
