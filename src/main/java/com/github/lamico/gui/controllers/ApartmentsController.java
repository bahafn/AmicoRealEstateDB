package com.github.lamico.gui.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.RentalApartment;
import com.github.lamico.entities.SaleApartment;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ApartmentsController {

	@FXML
	private AnchorPane root;

	@FXML
	private Button btRegisterNew;

	@FXML
	private Accordion acAccordion;

	@FXML
	private Button btDeleteRent;

	@FXML
	private Button btDeleteSale;

	@FXML
	private Button btUpdateRent;

	@FXML
	private Button btUpdateSale;

	@FXML
	private RadioButton rbBalconyRent;

	@FXML
	private RadioButton rbBalconySale;

	@FXML
	private RadioButton rbGardenRent;

	@FXML
	private RadioButton rbGardenSale;

	@FXML
	private TableView<RentalApartment> tbvRentalTable;

	@FXML
	private TableView<SaleApartment> tbvSaleTable;

	@FXML
	private TableColumn<?, ?> tvBalconyRent;

	@FXML
	private TableColumn<?, ?> tvBalconySale;

	@FXML
	private TableColumn<?, ?> tvBathroomsRent;

	@FXML
	private TableColumn<?, ?> tvBathroomsSale;

	@FXML
	private TableColumn<?, ?> tvBedroomsRent;

	@FXML
	private TableColumn<?, ?> tvBedroomsSale;

	@FXML
	private TableColumn<?, ?> tvBuildingRent;

	@FXML
	private TableColumn<?, ?> tvBuildingSale;

	@FXML
	private TableColumn<?, ?> tvGardenRent;

	@FXML
	private TableColumn<?, ?> tvGardenSale;

	@FXML
	private TableColumn<?, ?> tvKitchenRent;

	@FXML
	private TableColumn<?, ?> tvKitchenSale;

	@FXML
	private TableColumn<?, ?> tvLivingRoomsRent;

	@FXML
	private TableColumn<?, ?> tvLivingRoomsSale;

	@FXML
	private TableColumn<?, ?> tvPrNumRent;

	@FXML
	private TableColumn<?, ?> tvPrNumSale;

	@FXML
	private TableColumn<?, ?> tvRoomsRent;

	@FXML
	private TableColumn<?, ?> tvRoomsSale;

	@FXML
	private TableColumn<?, ?> tvUnitRent;

	@FXML
	private TableColumn<?, ?> tvUnitSale;

	@FXML
	private TableColumn<?, ?> tvPrice;

	@FXML
	private TableColumn<?, ?> tvRent;

	@FXML
	private TextField txtAreaRent;

	@FXML
	private TextField txtAreaSale;

	@FXML
	private TextField txtBathRent;

	@FXML
	private TextField txtBathSale;

	@FXML
	private TextField txtBedRent;

	@FXML
	private TextField txtBedSale;

	@FXML
	private TextField txtCityRent;

	@FXML
	private TextField txtCitySale;

	@FXML
	private TextField txtConditionRent;

	@FXML
	private TextField txtConditionSale;

	@FXML
	private TextField txtLivingRent;

	@FXML
	private TextField txtLivingSale;

	@FXML
	private TextField txtOwnerRent;

	@FXML
	private TextField txtOwnerSale;

	@FXML
	private TextField txtPrice;

	@FXML
	private TextField txtRent;

	@FXML
	private TextField txtRoomsRent;

	@FXML
	private TextField txtRoomsSale;

	@FXML
	private TextField txtStreetRent;

	@FXML
	private TextField txtStreetSale;

	@FXML
	private TextField txtUnitRent;

	@FXML
	private TextField txtUnitSale;

	@FXML
	void deleteRent(ActionEvent event) {
		RentalApartment selectedApartment = tbvRentalTable.getSelectionModel().getSelectedItem();
		if (selectedApartment != null) {
			Connection connection = DBConnection.getConnection();
			String checkSaleQuery = "SELECT COUNT(*) FROM SaleApartment WHERE prNum = " + selectedApartment.getPrNum();
			try (ResultSet resultSet = connection.createStatement().executeQuery(checkSaleQuery)) {
				resultSet.next();
				boolean isForSale = resultSet.getInt(1) > 0;
				String deleteQuery;
				if (isForSale) {
					deleteQuery = "DELETE FROM RentalApartment WHERE prNum = " + selectedApartment.getPrNum();
				} else {
					deleteQuery = "DELETE FROM Apartment WHERE prNum = " + selectedApartment.getPrNum();
				}
				executeQuery(deleteQuery);
				showRent();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		clearAllFields();
	}

	@FXML
	void deleteSale(ActionEvent event) {
		SaleApartment selectedApartment = tbvSaleTable.getSelectionModel().getSelectedItem();
		if (selectedApartment != null) {
			Connection connection = DBConnection.getConnection();
			String checkRentQuery = "SELECT COUNT(*) FROM RentalApartment WHERE prNum = "
					+ selectedApartment.getPrNum();
			try (ResultSet resultSet = connection.createStatement().executeQuery(checkRentQuery)) {
				resultSet.next();
				boolean isForRent = resultSet.getInt(1) > 0;
				String deleteQuery;
				if (isForRent) {
					deleteQuery = "DELETE FROM SaleApartment WHERE prNum = " + selectedApartment.getPrNum();
				} else {
					deleteQuery = "DELETE FROM Apartment WHERE prNum = " + selectedApartment.getPrNum();
				}
				executeQuery(deleteQuery);
				showSale();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		clearAllFields();
	}

	private void executeQuery(String query) throws SQLException {
		Connection connection = DBConnection.getConnection();

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		}
		connection.close();
	}

	@FXML
	void updateRent(ActionEvent event) {
		RentalApartment selectedApartment = tbvRentalTable.getSelectionModel().getSelectedItem();
		if (selectedApartment != null) {
			String updateQuery = "UPDATE Apartment a " + "JOIN RentalApartment ra ON a.prNum = ra.prNum "
					+ "SET a.roomNum = " + Integer.parseInt(txtRoomsRent.getText()) + ", " + "a.unitNum = "
					+ Integer.parseInt(txtUnitRent.getText()) + ", " + "a.bedroomNum = "
					+ Integer.parseInt(txtBedRent.getText()) + ", " + "a.bathroomNum = "
					+ Integer.parseInt(txtBathRent.getText()) + ", " + "a.livingroomNum = "
					+ Integer.parseInt(txtLivingRent.getText()) + ", " + "a.hasBalcony = "
					+ (rbBalconyRent.isSelected() ? 1 : 0) + ", " + "a.kitchenType = '" + txtConditionRent.getText()
					+ "', " + "a.hasGarden = " + (rbGardenRent.isSelected() ? 1 : 0) + ", " + "ra.rent = "
					+ Double.parseDouble(txtRent.getText()) + " WHERE a.prNum = " + selectedApartment.getPrNum();

			try {
				executeQuery(updateQuery);
				showRent();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		clearAllFields();
	}

	private void clearAllFields() {
		txtAreaRent.clear();
		txtBedRent.clear();
		txtBathRent.clear();
		txtCityRent.clear();
		txtConditionRent.clear();
		txtLivingRent.clear();
		txtOwnerRent.clear();
		txtPrice.clear();
		txtRent.clear();
		txtRoomsRent.clear();
		txtStreetRent.clear();
		txtUnitRent.clear();
		rbBalconyRent.setSelected(false);
		rbGardenRent.setSelected(false);

		txtBedSale.clear();
		txtBathSale.clear();
		txtBedSale.clear();
		txtBathSale.clear();
		txtLivingSale.clear();
		txtAreaSale.clear();
		txtCitySale.clear();
		txtConditionSale.clear();
		txtOwnerSale.clear();
		txtPrice.clear();
		txtRoomsSale.clear();
		txtStreetSale.clear();
		txtUnitSale.clear();
		rbBalconySale.setSelected(false);
		rbGardenSale.setSelected(false);
	}

	@FXML
	void updateSale(ActionEvent event) {
		SaleApartment selectedApartment = tbvSaleTable.getSelectionModel().getSelectedItem();
		if (selectedApartment != null) {
			String updateQuery = "UPDATE Apartment a " + "JOIN SaleApartment sa ON a.prNum = sa.prNum "
					+ "SET a.roomNum = " + Integer.parseInt(txtRoomsSale.getText()) + ", " + "a.unitNum = "
					+ Integer.parseInt(txtUnitSale.getText()) + ", " + "a.bedroomNum = "
					+ Integer.parseInt(txtBedSale.getText()) + ", " + "a.bathroomNum = "
					+ Integer.parseInt(txtBathSale.getText()) + ", " + "a.livingroomNum = "
					+ Integer.parseInt(txtLivingSale.getText()) + ", " + "a.hasBalcony = "
					+ (rbBalconySale.isSelected() ? 1 : 0) + ", " + "a.kitchenType = '" + txtConditionSale.getText()
					+ "', " + "a.hasGarden = " + (rbGardenSale.isSelected() ? 1 : 0) + ", " + "sa.price = "
					+ Double.parseDouble(txtPrice.getText()) + " WHERE a.prNum = " + selectedApartment.getPrNum();

			try {
				executeQuery(updateQuery);
				showSale();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		clearAllFields();
	}

	@FXML
	void handleRentalRowSelection(MouseEvent event) {
		RentalApartment selectedApartment = tbvRentalTable.getSelectionModel().getSelectedItem();
		if (selectedApartment != null) {
			Connection connection = DBConnection.getConnection();
			String query = "SELECT re.prCondition, re.city, re.streetName, re.valuation, re.areaDescription, re.area, re.ownerSSN "
					+ "FROM RealEstate re, RentalApartment ra " + "WHERE re.prNum = ra.prNum AND ra.prNum = "
					+ selectedApartment.getPrNum();

			try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
				if (resultSet.next()) {
					txtConditionRent.setText(resultSet.getString("prCondition"));
					txtCityRent.setText(resultSet.getString("city"));
					txtStreetRent.setText(resultSet.getString("streetName"));
					txtPrice.setText(resultSet.getString("valuation"));
					txtAreaRent.setText(resultSet.getString("areaDescription"));
					txtAreaRent.setText(resultSet.getString("area"));
					txtOwnerRent.setText(resultSet.getString("ownerSSN"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			txtRoomsRent.setText(String.valueOf(selectedApartment.getRoomNum()));
			txtUnitRent.setText(String.valueOf(selectedApartment.getUnitNum()));
			txtBedRent.setText(String.valueOf(selectedApartment.getBedroomNum()));
			txtBathRent.setText(String.valueOf(selectedApartment.getBathroomNum()));
			txtLivingRent.setText(String.valueOf(selectedApartment.getLivingroomNum()));
			rbBalconyRent.setSelected(selectedApartment.isHasBalcony());
			rbGardenRent.setSelected(selectedApartment.isHasGarden());
			txtRent.setText(String.valueOf(selectedApartment.getRent()));
		}
	}

	@FXML
	void handleSaleRowSelection(MouseEvent event) {
		SaleApartment selectedApartment = tbvSaleTable.getSelectionModel().getSelectedItem();
		if (selectedApartment != null) {
			Connection connection = DBConnection.getConnection();
			String query = "SELECT re.prCondition, re.city, re.streetName, re.valuation, re.areaDescription, re.area, re.ownerSSN "
					+ "FROM RealEstate re, SaleApartment sa " + "WHERE re.prNum = sa.prNum AND sa.prNum = "
					+ selectedApartment.getPrNum();

			try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
				if (resultSet.next()) {
					txtConditionSale.setText(resultSet.getString("prCondition"));
					txtCitySale.setText(resultSet.getString("city"));
					txtStreetSale.setText(resultSet.getString("streetName"));
					txtPrice.setText(resultSet.getString("valuation"));
					txtAreaSale.setText(resultSet.getString("areaDescription"));
					txtAreaSale.setText(resultSet.getString("area"));
					txtOwnerSale.setText(resultSet.getString("ownerSSN"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			txtRoomsSale.setText(String.valueOf(selectedApartment.getRoomNum()));
			txtUnitSale.setText(String.valueOf(selectedApartment.getUnitNum()));
			txtBedSale.setText(String.valueOf(selectedApartment.getBedroomNum()));
			txtBathSale.setText(String.valueOf(selectedApartment.getBathroomNum()));
			txtLivingSale.setText(String.valueOf(selectedApartment.getLivingroomNum()));
			rbBalconySale.setSelected(selectedApartment.isHasBalcony());
			rbGardenSale.setSelected(selectedApartment.isHasGarden());
			txtPrice.setText(String.valueOf(selectedApartment.getPrice()));
		}
	}

	@FXML
	void initialize() throws SQLException {
		show();
		acAccordion.setExpandedPane(acAccordion.getPanes().get(0));
		restrictTextFields();
		root.setBackground(ResourceManager.getBackground("flat.jpg"));
	}

	private void restrictTextFields() {
		txtAreaRent.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));
		txtAreaSale.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));
		txtBedRent.setTextFormatter(TextFormatterTypes.getIntFormatter(1));
		txtBedSale.setTextFormatter(TextFormatterTypes.getIntFormatter(1));
		txtBathRent.setTextFormatter(TextFormatterTypes.getIntFormatter(1));
		txtBathSale.setTextFormatter(TextFormatterTypes.getIntFormatter(1));
		txtLivingRent.setTextFormatter(TextFormatterTypes.getIntFormatter(1));
		txtLivingSale.setTextFormatter(TextFormatterTypes.getIntFormatter(1));
		txtRoomsRent.setTextFormatter(TextFormatterTypes.getIntFormatter(3));
		txtRoomsSale.setTextFormatter(TextFormatterTypes.getIntFormatter(3));
		txtUnitRent.setTextFormatter(TextFormatterTypes.getIntFormatter(6));
		txtUnitSale.setTextFormatter(TextFormatterTypes.getIntFormatter(6));

		txtCityRent.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(40));
		txtCitySale.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(40));
		txtConditionRent.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(255));
		txtConditionSale.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(255));
		txtOwnerRent.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
		txtOwnerSale.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
		txtPrice.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(10, 2));
		txtStreetRent.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
		txtStreetSale.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
		txtRent.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(8, 2));
	}

	private void showRent() throws SQLException {
		tbvRentalTable.setItems(getRentalApartments());
		tvPrNumRent.setCellValueFactory(new PropertyValueFactory<>("prNum"));
		tvRoomsRent.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
		tvBuildingRent.setCellValueFactory(new PropertyValueFactory<>("buildingNum"));
		tvUnitRent.setCellValueFactory(new PropertyValueFactory<>("unitNum"));
		tvBedroomsRent.setCellValueFactory(new PropertyValueFactory<>("bedroomNum"));
		tvBathroomsRent.setCellValueFactory(new PropertyValueFactory<>("bathroomNum"));
		tvLivingRoomsRent.setCellValueFactory(new PropertyValueFactory<>("livingroomNum"));
		tvBalconyRent.setCellValueFactory(new PropertyValueFactory<>("hasBalcony"));
		tvKitchenRent.setCellValueFactory(new PropertyValueFactory<>("kitchenType"));
		tvGardenRent.setCellValueFactory(new PropertyValueFactory<>("hasGarden"));
		tvRent.setCellValueFactory(new PropertyValueFactory<>("rent"));
	}

	public void show() throws SQLException {
		showSale();
		showRent();
	}
	private void showSale() throws SQLException {
		tbvSaleTable.setItems(getSaleApartments());
		tvPrNumSale.setCellValueFactory(new PropertyValueFactory<>("prNum"));
		tvRoomsSale.setCellValueFactory(new PropertyValueFactory<>("roomNum"));
		tvBuildingSale.setCellValueFactory(new PropertyValueFactory<>("buildingNum"));
		tvUnitSale.setCellValueFactory(new PropertyValueFactory<>("unitNum"));
		tvBedroomsSale.setCellValueFactory(new PropertyValueFactory<>("bedroomNum"));
		tvBathroomsSale.setCellValueFactory(new PropertyValueFactory<>("bathroomNum"));
		tvLivingRoomsSale.setCellValueFactory(new PropertyValueFactory<>("livingroomNum"));
		tvBalconySale.setCellValueFactory(new PropertyValueFactory<>("hasBalcony"));
		tvKitchenSale.setCellValueFactory(new PropertyValueFactory<>("kitchenType"));
		tvGardenSale.setCellValueFactory(new PropertyValueFactory<>("hasGarden"));
		tvPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
	}

	@FXML
	void registerNewProperty(ActionEvent event) {
		getPropertyController().showRegisterScreen(TabManager.APARTMENTS);
	}

	public PropertyController getPropertyController() {
		return (PropertyController) MainController.getTabManager().getController(TabManager.PROPERTY);
	}

	public ObservableList<RentalApartment> getRentalApartments() throws SQLException {
		ObservableList<RentalApartment> apartments = FXCollections.observableArrayList();
		Connection connection = DBConnection.getConnection();
		try (ResultSet resultSet = connection.createStatement().executeQuery(
				"SELECT Apartment.*, ra.rent FROM Apartment, RentalApartment ra WHERE Apartment.prNum = ra.prNum;")) {
			while (resultSet.next()) {
				RentalApartment apartment = new RentalApartment(resultSet.getInt("prNum"),
						resultSet.getInt("buildingNum"), resultSet.getInt("roomNum"), resultSet.getInt("unitNum"),
						resultSet.getInt("bedroomNum"), resultSet.getInt("bathroomNum"),
						resultSet.getInt("livingroomNum"), resultSet.getBoolean("hasBalcony"),
						resultSet.getString("kitchenType"), resultSet.getBoolean("hasGarden"),
						resultSet.getDouble("ra.rent"));
				apartments.add(apartment);
			}
		}
		return apartments;
	}

	public ObservableList<SaleApartment> getSaleApartments() throws SQLException {
		ObservableList<SaleApartment> apartments = FXCollections.observableArrayList();
		Connection connection = DBConnection.getConnection();
		try (ResultSet resultSet = connection.createStatement().executeQuery(
				"SELECT Apartment.*, sa.price FROM Apartment, SaleApartment sa WHERE Apartment.prNum = sa.prNum;")) {
			while (resultSet.next()) {
				SaleApartment apartment = new SaleApartment(resultSet.getInt("prNum"), resultSet.getInt("buildingNum"),
						resultSet.getInt("roomNum"), resultSet.getInt("unitNum"), resultSet.getInt("bedroomNum"),
						resultSet.getInt("bathroomNum"), resultSet.getInt("livingroomNum"),
						resultSet.getBoolean("hasBalcony"), resultSet.getString("kitchenType"),
						resultSet.getBoolean("hasGarden"), resultSet.getDouble("sa.price"));
				apartments.add(apartment);
			}
		}
		return apartments;
	}
}
