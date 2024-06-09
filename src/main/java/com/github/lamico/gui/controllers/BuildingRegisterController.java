package com.github.lamico.gui.controllers;

import com.github.lamico.db.DBConnection;
import com.github.lamico.db.managers.PropertyRegistrationManager;
import com.github.lamico.entities.Land;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BuildingRegisterController {

	@FXML
	private Button btRegister;

	@FXML
	private RadioButton rbShowLand;

	@FXML
	private BorderPane root;

	@FXML
	private TableView<Land> tbvTable;

	@FXML
	private TableColumn<?, ?> tvBlock;

	@FXML
	private TableColumn<?, ?> tvPlot;

	@FXML
	private TableColumn<?, ?> tvPrNum;

	@FXML
	private TextField txtFloor;

	@FXML
	private TextField txtLand;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtYear;

	@FXML
	void displayLandTable(ActionEvent event) {
		if (rbShowLand.isSelected()) {
			showLand();
		} else {
			hideLand();
		}

	}

	private void hideLand() {
		rbShowLand.selectedProperty().set(false);
		tbvTable.setVisible(false);
		tbvTable.setDisable(true);
		tbvTable.getSelectionModel().clearSelection();
	}

	private void showLand() {
		tbvTable.setItems(getLand());
		tvBlock.setCellValueFactory(new PropertyValueFactory<>("blockNum"));
		tvPlot.setCellValueFactory(new PropertyValueFactory<>("plotNum"));
		tvPrNum.setCellValueFactory(new PropertyValueFactory<>("prNum"));

		tbvTable.setVisible(true);
		tbvTable.setDisable(false);

	}

	private ObservableList<Land> getLand() {
		ObservableList<Land> result = FXCollections.observableArrayList();
		Connection connection = DBConnection.getConnection();
		try (Statement statement = connection.createStatement()) {
			ResultSet queryResult = statement.executeQuery("SELECT prNum, plotNum, blockNum FROM Land");
			while (queryResult.next()) {
				result.add(new Land(queryResult.getInt("prNum"), queryResult.getInt("plotNum"),
						queryResult.getInt("blockNum")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@FXML
	void handleRowSelection(MouseEvent event) {
		Land selectedLand = tbvTable.getSelectionModel().getSelectedItem();
		if (selectedLand != null) {
			txtLand.setText(String.valueOf(selectedLand.getPrNum()));
		}
	}

	@FXML
	void register(ActionEvent event) {
		String buildingName = txtName.getText().strip();
		int floorNum = Integer.parseInt(txtFloor.getText().strip());
		int landNum = Integer.parseInt(txtLand.getText().strip());
		int yearBuilt = Integer.parseInt(txtYear.getText().strip());
		try {
			PropertyRegistrationManager.registerBuilding(landNum, buildingName, yearBuilt, floorNum);

			PropertyRegistrationManager.commitTransaction();

			((BuildingsController) MainController.getTabManager().getController(TabManager.BUILDINGS)).show();
			MainController.getTabManager().switchTo(TabManager.BUILDINGS);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				PropertyRegistrationManager.rollbackTransaction();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@FXML
	void initialize() {
		root.setBackground(ResourceManager.getBackground("buildings.jpg"));
		clearAllFields();
		restrictFields();
	}

	private void clearAllFields() {
		txtName.clear();
		txtFloor.clear();
		txtLand.clear();
		txtYear.clear();
		hideLand();
	}

	private void restrictFields() {
		txtName.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
		txtFloor.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
		txtLand.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
		txtYear.setTextFormatter(TextFormatterTypes.getIntFormatter(4));
	}
}
