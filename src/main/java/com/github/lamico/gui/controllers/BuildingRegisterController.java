package com.github.lamico.gui.controllers;

import com.github.lamico.db.DBConnection;
import com.github.lamico.db.managers.PropertyRegistrationManager;
import com.github.lamico.entities.Land;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Controller for the Building Register GUI.
 */
public class BuildingRegisterController {
    private TimedError timedError = new TimedError();

    @FXML
    private Button btRegister;

    @FXML
    private Label lbError;

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

    /**
     * Handles the display of the land table based on the radio button selection.
     *
     * @param event the action event
     */
    @FXML
    void displayLandTable(ActionEvent event) {
        if (rbShowLand.isSelected()) {
            showLand();
        } else {
            hideLand();
        }
    }

    /**
     * Hides the land table and clears the selection.
     */
    private void hideLand() {
        rbShowLand.selectedProperty().set(false);
        tbvTable.setVisible(false);
        tbvTable.setDisable(true);
        tbvTable.getSelectionModel().clearSelection();
    }

    /**
     * Shows the land table and sets up the columns.
     */
    private void showLand() {
        tbvTable.setItems(getLand());
        tvBlock.setCellValueFactory(new PropertyValueFactory<>("blockNum"));
        tvPlot.setCellValueFactory(new PropertyValueFactory<>("plotNum"));
        tvPrNum.setCellValueFactory(new PropertyValueFactory<>("prNum"));

        tbvTable.setVisible(true);
        tbvTable.setDisable(false);
    }

    /**
     * Retrieves a list of lands from the database.
     *
     * @return an observable list of lands
     */
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

    /**
     * Handles the selection of a land row in the table.
     *
     * @param event the mouse event
     */
    @FXML
    void handleRowSelection(MouseEvent event) {
        Land selectedLand = tbvTable.getSelectionModel().getSelectedItem();
        if (selectedLand != null) {
            txtLand.setText(String.valueOf(selectedLand.getPrNum()));
        }
    }

    /**
     * Handles the registration of a building.
     *
     * @param event the action event
     */
    @FXML
    void register(ActionEvent event) {
        String buildingName = txtName.getText().strip();
        int floorNum = ParseUtils.parseIntOrDefault(txtFloor.getText());
        int landNum = ParseUtils.parseIntOrDefault(txtLand.getText());
        int yearBuilt = ParseUtils.parseIntOrDefault(txtYear.getText());

        if (buildingName.isBlank() || floorNum == 0 || landNum == 0 || yearBuilt == 0) {
            timedError.displayErrorMessage(lbError, "Empty Fields", 2);
            return;
        }
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

    /**
     * Initializes the controller by setting the background and clearing fields.
     */
    @FXML
    void initialize() {
        root.setBackground(ResourceManager.getBackground("buildings.jpg"));
        clearAllFields();
        restrictFields();
    }

    /**
     * Clears all text fields.
     */
    private void clearAllFields() {
        txtName.clear();
        txtFloor.clear();
        txtLand.clear();
        txtYear.clear();
        hideLand();
    }

    /**
     * Restricts the input fields with formatters.
     */
    private void restrictFields() {
        txtName.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
        txtFloor.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtLand.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtYear.setTextFormatter(TextFormatterTypes.getIntFormatter(4));
    }
}