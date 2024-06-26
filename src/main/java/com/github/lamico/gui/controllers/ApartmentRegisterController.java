package com.github.lamico.gui.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;
import com.github.lamico.db.managers.PropertyRegistrationManager;
import com.github.lamico.entities.Building;
import com.github.lamico.gui.utils.ParseUtils;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * Controller for the apartment registration page.
 */
public class ApartmentRegisterController {
    private TimedError timedError = new TimedError();

    @FXML
    private ToggleGroup apartmentTypeGroup;

    @FXML
    private Label lbError;

    @FXML
    private Button btRegister;

    @FXML
    private Label lbType;

    @FXML
    private RadioButton rbBalcony;

    @FXML
    private RadioButton rbGarden;

    @FXML
    private RadioButton rbRent;

    @FXML
    private RadioButton rbSale;

    @FXML
    private RadioButton rbShowBuildings;

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Building> tbvTable;

    @FXML
    private TableColumn<Building, Integer> tvFloors;

    @FXML
    private TableColumn<Building, String> tvName;

    @FXML
    private TableColumn<Building, Integer> tvPrNum;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtBathrooms;

    @FXML
    private TextField txtBedrooms;

    @FXML
    private TextField txtBuilding;

    @FXML
    private TextField txtKitchen;

    @FXML
    private TextField txtLivingRooms;

    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtUnitNumber;

    /**
     * Handles the display of the building table.
     * 
     * @param event the event that triggered this method
     */
    @FXML
    void displayBuildingTable(ActionEvent event) {
        if (rbShowBuildings.isSelected()) {
            showBuildings();
        } else {
            hideBuildings();
        }
    }

    /**
     * Handles the selection of a row in the building table.
     * 
     * @param event the event that triggered this method
     */
    @FXML
    void handleRowSelection(MouseEvent event) {
        Building building = tbvTable.getSelectionModel().getSelectedItem();
        if (building == null) {
            return;
        }

        txtBuilding.setText(building.getPrNum() + "");
    }

    /**
     * Handles the registration of an apartment.
     * 
     * @param event the event that triggered this method
     */
    @FXML
    void register(ActionEvent event) {
        double amount = ParseUtils.parseDoubleOrDefault(txtAmount.getText());
        int unitNumber = ParseUtils.parseIntOrDefault(txtUnitNumber.getText());
        int total = ParseUtils.parseIntOrDefault(txtTotal.getText());
        String kitchen = txtKitchen.getText().strip();
        int livingRooms = ParseUtils.parseIntOrDefault(txtLivingRooms.getText());
        int bathrooms = ParseUtils.parseIntOrDefault(txtBathrooms.getText());
        int bedrooms = ParseUtils.parseIntOrDefault(txtBedrooms.getText());
        int building = ParseUtils.parseIntOrDefault(txtBuilding.getText());
        boolean hasBalcony = rbBalcony.isSelected();
        boolean hasGarden = rbGarden.isSelected();

        if (amount == 0 || unitNumber == 0 || total == 0 || building == 0 || livingRooms == 0 || bathrooms == 0
                || bedrooms == 0 || kitchen.isBlank()) {
            timedError.displayErrorMessage(lbError, "Empty Fields", 2);
            return;
        }

        int sum = livingRooms + bathrooms + bedrooms;
        if (total < sum) {
            timedError.displayErrorMessage(lbError, "Invalid Room Total", 2);
            return;
        }

        try {
            PropertyRegistrationManager.registerApartment(building, total, unitNumber, bedrooms, bathrooms, livingRooms,
                    hasBalcony, kitchen, hasGarden);
            if (rbRent.isSelected()) {
                PropertyRegistrationManager.registerRentalApartment(amount);
            } else {
                PropertyRegistrationManager.registerSaleApartment(amount);
            }
            PropertyRegistrationManager.commitTransaction();
            ((ApartmentsController) MainController.getTabManager().getController(TabManager.APARTMENTS)).show();
            MainController.getTabManager().switchTo(TabManager.APARTMENTS);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                PropertyRegistrationManager.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Handles the switch to rent.
     * 
     * @param event the event that triggered this method
     */
    @FXML
    void switchToRent(ActionEvent event) {
        lbType.setText("Rent");
    }

    /**
     * Handles the switch to sale.
     * 
     * @param event the event that triggered this method
     */
    @FXML
    void switchToSale(ActionEvent event) {
        lbType.setText("Sale");
    }

    /**
     * Initializes the controller.
     */
    @FXML
    void initialize() {
        root.setBackground(ResourceManager.getBackground("flat.jpg"));
        clearAllFields();
        restrictFields();
    }

    /**
     * Clears all fields.
     */
    private void clearAllFields() {
        txtAmount.clear();
        txtUnitNumber.clear();
        txtTotal.clear();
        txtKitchen.clear();
        txtLivingRooms.clear();
        txtBathrooms.clear();
        txtBedrooms.clear();
        txtBuilding.clear();
        rbBalcony.setSelected(false);
        rbGarden.setSelected(false);
        hideBuildings();
    }

    /**
     * Restricts the fields.
     */
    private void restrictFields() {
        txtAmount.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));
        txtUnitNumber.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtTotal.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtKitchen.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtLivingRooms.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtBathrooms.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtBedrooms.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtBuilding.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
    }

    /**
     * Shows the buildings.
     */
    public void showBuildings() {
        tbvTable.setItems(getBuildings());
        tvName.setCellValueFactory(new PropertyValueFactory<>("bName"));
        tvFloors.setCellValueFactory(new PropertyValueFactory<>("floorNum"));
        tvPrNum.setCellValueFactory(new PropertyValueFactory<>("prNum"));

        tbvTable.setVisible(true);
        tbvTable.setDisable(false);
        tbvTable.getSelectionModel().clearSelection();
    }

    /**
     * Hides the buildings.
     */
    public void hideBuildings() {
        rbShowBuildings.selectedProperty().set(false);
        tbvTable.setVisible(false);
        tbvTable.setDisable(true);
        tbvTable.getSelectionModel().clearSelection();
    }

    /**
     * Gets the buildings.
     * 
     * @return the buildings
     */
    public ObservableList<Building> getBuildings() {
        ObservableList<Building> result = FXCollections.observableArrayList();
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery("SELECT prNum, bName, floorNum FROM Building")) {

            while (queryResult.next()) {
                result.add(new Building(queryResult.getInt("prNum"), queryResult.getString("bName"),
                        queryResult.getInt("floorNum")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}