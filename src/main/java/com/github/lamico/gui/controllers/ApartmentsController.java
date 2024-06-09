package com.github.lamico.gui.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.RentalApartment;
import com.github.lamico.entities.SaleApartment;
import com.github.lamico.managers.TabManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ApartmentsController {

	@FXML
	private Button btRegisterNew;

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
	void handleRentalRowSelection(MouseEvent event) {

	}

	@FXML
	void handleSaleRowSelection(MouseEvent event) {

	}

	@FXML
	void initialize() throws SQLException {
		showSale();
		showRent();
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
