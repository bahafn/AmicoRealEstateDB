package com.github.lamico.gui.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Land;
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
 * Controller class for the Land GUI.
 */
public class LandController {
    private TimedError timedError = new TimedError();

    @FXML
    private AnchorPane root;

    @FXML
    private Label lbError;

    @FXML
    private Button btDelete;

    @FXML
    private Button btRegisterNew;

    @FXML
    private Button btUpdate;

    @FXML
    private TableView<Land> tbvTable;

    @FXML
    private TableColumn<?, ?> tvArea;

    @FXML
    private TableColumn<?, ?> tvBlock;

    @FXML
    private TableColumn<?, ?> tvCity;

    @FXML
    private TableColumn<?, ?> tvCondition;

    @FXML
    private TableColumn<?, ?> tvOwner;

    @FXML
    private TableColumn<?, ?> tvPlot;

    @FXML
    private TableColumn<?, ?> tvPrNum;

    @FXML
    private TableColumn<?, ?> tvStreet;

    @FXML
    private TableColumn<?, ?> tvValuation;

    @FXML
    private TextField txtArea;

    @FXML
    private TextField txtBlock;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtCondition;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtOwner;

    @FXML
    private TextField txtPlot;

    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtValuation;

    /**
     * Refreshes the table view by calling the show method.
     * 
     * @param event The action event.
     * @throws SQLException If a database error occurs.
     */
    @FXML
    void refresh(ActionEvent event) throws SQLException {
        show();
    }

    /**
     * Deletes the selected land from the database.
     * 
     * @param event The action event.
     */
    @FXML
    void delete(ActionEvent event) {
        Land selectedLand = tbvTable.getSelectionModel().getSelectedItem();
        if (selectedLand != null) {
            Connection connection = DBConnection.getConnection();
            String query = "DELETE FROM Land WHERE prNum = " + selectedLand.getPrNum();
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                showAllLand();
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
     * Shows all lands in the table view.
     * 
     * @throws SQLException If a database error occurs.
     */
    public void show() throws SQLException {
        showAllLand();
    }

    /**
     * Clears all text fields.
     */
    private void clearAllFields() {
        txtArea.clear();
        txtBlock.clear();
        txtCity.clear();
        txtCondition.clear();
        txtDescription.clear();
        txtOwner.clear();
        txtPlot.clear();
        txtStreet.clear();
        txtValuation.clear();
    }

    /**
     * Updates the selected land in the database.
     * 
     * @param event The action event.
     */
    @FXML
    void update(ActionEvent event) {
        Land selectedLand = tbvTable.getSelectionModel().getSelectedItem();
        if (selectedLand != null) {
            String area = txtArea.getText();
            String condition = txtCondition.getText();
            String city = txtCity.getText();
            String description = txtDescription.getText();
            String valuation = txtValuation.getText();
            String ownerSSN = txtOwner.getText();
            String streetName = txtStreet.getText();
            String blockNum = txtBlock.getText();
            String plotNum = txtPlot.getText();
            String prNum = String.valueOf(selectedLand.getPrNum());

            if (area.isBlank() || condition.isBlank() || city.isBlank() || description.isBlank() || valuation.isBlank()
                    || ownerSSN.isBlank() || streetName.isBlank() || blockNum.isBlank() || plotNum.isBlank()) {
                timedError.displayErrorMessage(lbError, "Empty Fields", 2);
                return;
            }

            if (ownerSSN.length() < 9) {
                timedError.displayErrorMessage(lbError, "Invalid SSN", 2);
                return;
            }

            try (Connection connection = DBConnection.getConnection();
                    Statement statement = connection.createStatement()) {

                String query = "UPDATE RealEstate SET area = " + area + ", prCondition = '" + condition + "', city = '"
                        + city + "', areaDescription = '" + description + "', valuation = " + valuation
                        + ", ownerSSN = '" + ownerSSN + "', streetName = '" + streetName + "' WHERE prNum = " + prNum;
                statement.executeUpdate(query);

                query = "UPDATE Land SET blockNum = " + blockNum + ", plotNum = " + plotNum + " WHERE prNum = " + prNum;
                statement.executeUpdate(query);

                showAllLand();
            } catch (SQLException e) {
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
        Land selectedLand = tbvTable.getSelectionModel().getSelectedItem();
        if (selectedLand != null) {
            txtArea.setText(selectedLand.getArea() + "");
            txtCondition.setText(selectedLand.getPrCondition());
            txtCity.setText(selectedLand.getCity());
            txtDescription.setText(selectedLand.getAreaDescription());
            txtValuation.setText(selectedLand.getValuation() + "");
            txtOwner.setText(selectedLand.getOwnerSSN());
            txtStreet.setText(selectedLand.getStreetName());
            txtPlot.setText(selectedLand.getPlotNum() + "");
            txtBlock.setText(selectedLand.getBlockNum() + "");
        }
    }

    /**
     * Opens the register screen for a new property.
     * 
     * @param event The action event.
     */
    @FXML
    void registerNewProperty(ActionEvent event) {
        getPropertyController().showRegisterScreen(TabManager.LAND);
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
        root.setBackground(ResourceManager.getBackground("land.png"));
        showAllLand();
        restrictFields();
    }

    /**
     * Shows all lands in the table view.
     * 
     * @throws SQLException If a database error occurs.
     */
    private void showAllLand() throws SQLException {
        tbvTable.setItems(getAllLand());
        tvArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        tvBlock.setCellValueFactory(new PropertyValueFactory<>("blockNum"));
        tvCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tvCondition.setCellValueFactory(new PropertyValueFactory<>("prCondition"));
        tvOwner.setCellValueFactory(new PropertyValueFactory<>("ownerSSN"));
        tvPlot.setCellValueFactory(new PropertyValueFactory<>("plotNum"));
        tvPrNum.setCellValueFactory(new PropertyValueFactory<>("prNum"));
        tvStreet.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        tvValuation.setCellValueFactory(new PropertyValueFactory<>("valuation"));
    }

    /**
     * Gets all lands from the database.
     * 
     * @return The list of lands.
     * @throws SQLException If a database error occurs.
     */
    private ObservableList<Land> getAllLand() throws SQLException {
        ObservableList<Land> land = FXCollections.observableArrayList();
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement
                        .executeQuery("SELECT l.*, RE.* FROM Land l, RealEstate RE WHERE l.prNum = RE.prNum")) {
            while (queryResult.next()) {
                land.add(new Land(queryResult.getInt("prNum"), queryResult.getString("prCondition"),
                        queryResult.getString("city"), queryResult.getString("streetName"),
                        queryResult.getDouble("valuation"), queryResult.getString("areaDescription"),
                        queryResult.getDouble("area"), queryResult.getString("ownerSSN"), queryResult.getInt("plotNum"),
                        queryResult.getInt("blockNum")));
            }
            return land;
        }
    }

    /**
     * Restricts the text fields to specific formats.
     */
	private void restrictFields() {
		txtArea.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));
		txtValuation.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));
		txtCondition.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(255));
		txtDescription.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(255));
		txtOwner.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
		txtCity.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(40));
		txtStreet.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
		txtPlot.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
		txtBlock.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
	}
}
