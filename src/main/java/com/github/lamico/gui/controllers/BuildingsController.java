package com.github.lamico.gui.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Building;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.github.lamico.gui.utils.TimedError;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for the Buildings GUI.
 */
public class BuildingsController {
	private TimedError timedError = new TimedError();

	@FXML
	private Button btDelete;

	@FXML
	private Label lbError;

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

	/**
	 * Refreshes the table view with all buildings.
	 * 
	 * @param event The action event.
	 * @throws SQLException If a database error occurs.
	 */
	@FXML
	void refresh(ActionEvent event) throws SQLException {
		show();
	}

	/**
	 * Updates a building in the database.
	 * 
	 * @param event The action event.
	 */
	@FXML
	void update(ActionEvent event) {
		Building selectedBuilding = tbvTable.getSelectionModel().getSelectedItem();
		if (selectedBuilding != null) {
			try (Connection connection = DBConnection.getConnection();
					Statement statement = connection.createStatement()) {
				String bName = txtName.getText();
				String yearBuilt = txtYear.getText();
				String floorNum = txtFloors.getText();
				String prNum = String.valueOf(selectedBuilding.getPrNum());
				String condition = txtCondition.getText();
				String areaDescription = txtDescription.getText();
				String area = txtArea.getText();
				String valuation = txtValuation.getText();
				String ownerSSN = txtOwner.getText();
				String city = txtCity.getText();

				if (bName.isBlank() || yearBuilt.isBlank() || floorNum.isBlank() || condition.isBlank()
						|| areaDescription.isBlank() || area.isBlank() || valuation.isBlank() || ownerSSN.isBlank()
						|| city.isBlank()) {
					timedError.displayErrorMessage(lbError, "Empty Fields", 2);
					return;
				}

				if (ownerSSN.length() < 9) {
					timedError.displayErrorMessage(lbError, "Invalid SSN", 2);
					return;
				}

				String query = "UPDATE Building SET bName = '" + bName + "', yearBuilt = " + yearBuilt + ", floorNum = "
						+ floorNum + " WHERE prNum = " + prNum;
				statement.executeUpdate(query);

				query = "UPDATE RealEstate SET prCondition = '" + condition + "', areaDescription = '" + areaDescription
						+ "', area = " + area + ", valuation = " + valuation + ", ownerSSN = '" + ownerSSN
						+ "', city = '" + city + "' WHERE prNum = " + prNum;
				statement.executeUpdate(query);

				showAllBuildings();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			timedError.displayErrorMessage(lbError, "Select a Row First", 2);
		}
		clearAllFields();
	}

	/**
	 * Clears all text fields.
	 */
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

	/**
	 * Deletes a building from the database.
	 * 
	 * @param event The action event.
	 */
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
				timedError.displayErrorMessage(lbError, "Failed to delete", 2);
				e.printStackTrace();
			}
		} else {
			timedError.displayErrorMessage(lbError, "Select a Row First", 2);
		}
		clearAllFields();
	}

	/**
	 * Handles row selection in the table view.
	 * 
	 * @param event The mouse event.
	 */
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

	/**
	 * Opens the register screen for a new property.
	 * 
	 * @param event The action event.
	 */
	@FXML
	void registerNewProperty(ActionEvent event) {
		getPropertyController().showRegisterScreen(TabManager.BUILDINGS);
	}

	/**
	 * Gets the property controller.
	 * 
	 * @return The property controller.
	 */
	public PropertyController getPropertyController() {
		return (PropertyController) MainController.getTabManager().getController(TabManager.PROPERTY);
	}

	/**
	 * Initializes the controller.
	 * 
	 * @throws SQLException If a database error occurs.
	 */
	@FXML
	void initialize() throws SQLException {
		root.setBackground(ResourceManager.getBackground("buildings.jpg"));
		showAllBuildings();
		restrictFields();
	}

	/**
	 * Shows all buildings in the table view.
	 * 
	 * @throws SQLException If a database error occurs.
	 */
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

	/**
	 * Restricts the input fields to specific formats.
	 */
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

	/**
	 * Gets all buildings from the database.
	 * 
	 * @return A list of all buildings.
	 * @throws SQLException If a database error occurs.
	 */
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

	public void show() throws SQLException {
		showAllBuildings();
	}

}
