package com.github.lamico.gui.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Building;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class BuildingsController {

	@FXML
	private Button btDelete;

	@FXML
	private Button btRegisterNew;

	@FXML
	private Button btUpdate;

	@FXML
	private AnchorPane root;

	@FXML
	private TableView<Building> tbvTable;

	@FXML
	private TableColumn<?, ?> tvArea;

	@FXML
	private TableColumn<?, ?> tvCity;

	@FXML
	private TableColumn<?, ?> tvFloors;

	@FXML
	private TableColumn<?, ?> tvLand;

	@FXML
	private TableColumn<?, ?> tvName;

	@FXML
	private TableColumn<?, ?> tvOwner;

	@FXML
	private TableColumn<?, ?> tvPrNum;

	@FXML
	private TableColumn<?, ?> tvStreet;

	@FXML
	private TableColumn<?, ?> tvValuation;

	@FXML
	private TableColumn<?, ?> tvYear;

	@FXML
	private TableColumn<?, ?> tvCondition;

	@FXML
	private TextField txtArea;

	@FXML
	private TextField txtCity;

	@FXML
	private TextField txtCondition;

	@FXML
	private TextField txtDescription;

	@FXML
	private TextField txtFloors;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtOwner;

	@FXML
	private TextField txtStreet;

	@FXML
	private TextField txtValuation;

	@FXML
	private TextField txtYear;

	@FXML
	void update(ActionEvent event) {
		Building selectedBuilding = tbvTable.getSelectionModel().getSelectedItem();
		if (selectedBuilding != null) {
			try (Connection connection = DBConnection.getConnection();
					Statement statement = connection.createStatement()) {
				String query = "UPDATE Building SET bName = '" + txtName.getText() + "', yearBuilt = "
						+ txtYear.getText() + ", floorNum = " + txtFloors.getText() + " WHERE prNum = "
						+ selectedBuilding.getPrNum();
				statement.executeUpdate(query);
				query = "UPDATE RealEstate SET prCondition = '" + txtCondition.getText() + "', areaDescription = '"
						+ txtDescription.getText() + "', area = " + txtArea.getText() + ", valuation = "
						+ txtValuation.getText() + ", ownerSSN = '" + txtOwner.getText() + "', city = '"
						+ txtCity.getText() + "' WHERE prNum = " + selectedBuilding.getPrNum();
				statement.executeUpdate(query);
				showAllBuildings();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		clearAllFields();
	}

	private void clearAllFields() {
		txtArea.clear();
		txtCity.clear();
		txtCondition.clear();
		txtDescription.clear();
		txtOwner.clear();
		txtStreet.clear();
		txtValuation.clear();
		txtYear.clear();
		txtFloors.clear();
		txtName.clear();
		
	}

	@FXML
	void delete(ActionEvent event) {
		Building selectedBuilding = tbvTable.getSelectionModel().getSelectedItem();
		if (selectedBuilding != null) {
			try (Connection connection = DBConnection.getConnection();
					Statement statement = connection.createStatement()) {
				String query = "DELETE FROM Building WHERE prNum = " + selectedBuilding.getPrNum();
				statement.executeUpdate(query);
				query = "DELETE FROM RealEstate WHERE prNum = " + selectedBuilding.getPrNum();
				statement.executeUpdate(query);
				showAllBuildings();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		clearAllFields();
	}

	@FXML
	void handleRowSelection(MouseEvent event) {
		Building selectedBuilding = tbvTable.getSelectionModel().getSelectedItem();
		if (selectedBuilding != null) {
			txtName.setText(selectedBuilding.getBName());
			txtCondition.setText(selectedBuilding.getPrCondition());
			txtArea.setText(selectedBuilding.getArea() + "");
			txtCity.setText(selectedBuilding.getCity());
			txtDescription.setText(selectedBuilding.getAreaDescription());
			txtFloors.setText(selectedBuilding.getFloorNum() + "");
			txtOwner.setText(selectedBuilding.getOwnerSSN());
			txtValuation.setText(selectedBuilding.getValuation() + "");
			txtYear.setText(selectedBuilding.getYearBuilt() + "");
			txtCondition.setText(selectedBuilding.getPrCondition());
			txtStreet.setText(selectedBuilding.getStreetName());
		}
	}

	@FXML
	void registerNewProperty(ActionEvent event) {
		getPropertyController().showRegisterScreen(TabManager.BUILDINGS);
	}

	public PropertyController getPropertyController() {
		return (PropertyController) MainController.getTabManager().getController(TabManager.PROPERTY);
	}

	@FXML
	void initialize() throws SQLException {
		root.setBackground(ResourceManager.getBackground("buildings.jpg"));
		showAllBuildings();
		restrictFields();
	}

	private void showAllBuildings() throws SQLException {
		tbvTable.setItems(getAllBuildings());
		tvArea.setCellValueFactory(new PropertyValueFactory<>("area"));
		tvCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		tvFloors.setCellValueFactory(new PropertyValueFactory<>("floorNum"));
		tvLand.setCellValueFactory(new PropertyValueFactory<>("landNum"));
		tvName.setCellValueFactory(new PropertyValueFactory<>("bName"));
		tvOwner.setCellValueFactory(new PropertyValueFactory<>("ownerSSN"));
		tvPrNum.setCellValueFactory(new PropertyValueFactory<>("prNum"));
		tvStreet.setCellValueFactory(new PropertyValueFactory<>("streetName"));
		tvValuation.setCellValueFactory(new PropertyValueFactory<>("valuation"));
		tvYear.setCellValueFactory(new PropertyValueFactory<>("yearBuilt"));
		tvCondition.setCellValueFactory(new PropertyValueFactory<>("prCondition"));
	}

	private void restrictFields() {
		txtArea.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));
		txtValuation.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));

		txtCondition.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(255));
		txtDescription.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(255));

		txtOwner.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
		txtCity.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(40));
		txtStreet.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));

		txtFloors.setTextFormatter(TextFormatterTypes.getIntFormatter(2));
		txtYear.setTextFormatter(TextFormatterTypes.getIntFormatter(4));

		txtName.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(40));
	}

	private ObservableList<Building> getAllBuildings() throws SQLException {
		ObservableList<Building> buildings = FXCollections.observableArrayList();
		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet queryResult = statement
						.executeQuery("SELECT b.*, RE.* FROM Building b, RealEstate RE WHERE b.prNum = RE.prNum")) {
			while (queryResult.next()) {
				buildings.add(new Building(queryResult.getInt("prNum"), queryResult.getString("prCondition"),
						queryResult.getString("city"), queryResult.getString("streetName"),
						queryResult.getDouble("valuation"), queryResult.getString("areaDescription"),
						queryResult.getDouble("area"), queryResult.getString("ownerSSN"), queryResult.getInt("landNum"),
						queryResult.getString("bName"), queryResult.getInt("yearBuilt"),
						queryResult.getInt("floorNum")));
			}
			return buildings;
		}
	}

}
