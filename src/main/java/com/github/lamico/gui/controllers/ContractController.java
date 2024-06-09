package com.github.lamico.gui.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Contract;
import com.github.lamico.gui.utils.AlertUtil;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ContractController implements Initializable {

    @FXML
    private TableView<Contract> tvContract;

    @FXML
    private TableColumn<Contract, String> tcContractNo;

    @FXML
    private TableColumn<Contract, LocalDate> tcCDate;

    @FXML
    private TableColumn<Contract, String> tcCStatus;

    @FXML
    private TableColumn<Contract, String> tcArrangementType;

    @FXML
    private TableColumn<Contract, Double> tcPrice;

    @FXML
    private TableColumn<Contract, String> tcBrokerSSN;

    @FXML
    private TableColumn<Contract, String> tcClientSSN;

    @FXML
    private TableColumn<Contract, String> tcPrNum;

    @FXML
    private TextField txtContractNo, txtCStatus, txtArrangementType, txtBrokerSSN, txtClientSSN, txtPrNum;

    @FXML
    private DatePicker txtCDate;
    @FXML
    private TextField txtNewCStatus;

    @FXML
    private TextField txtNewArrangementType;

    @FXML
    private TextField txtNewPrice;
    @FXML
    private TableView<Contract> tableView;

    @FXML
    private Label lbGeneralError;
    @FXML
    private TextField txtPrice;

    @FXML
    public void handleRowSelection(MouseEvent event) {
        Contract contract = tvContract.getSelectionModel().getSelectedItem();
        if (contract == null)
            return;

        txtContractNo.setText(contract.getContractNo());

        // Convert Date to LocalDate
        Date cDate = contract.getcDate();
        if (cDate != null) {
            Instant instant = cDate.toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            txtCDate.setValue(localDate);
        } else {
            txtCDate.setValue(null); // Set the value to null if cDate is null
        }

        txtCStatus.setText(contract.getcStatus());
        txtArrangementType.setText(contract.getArrangmentType());
        txtBrokerSSN.setText(contract.getBrokerSSN());
        txtClientSSN.setText(contract.getClientSSN());
        txtPrNum.setText(contract.getPrNum());
    }
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up cell value factories for each column
        tcContractNo.setCellValueFactory(new PropertyValueFactory<>("contractNo"));
        tcCDate.setCellValueFactory(cellData -> {
            SimpleObjectProperty<LocalDate> property = new SimpleObjectProperty<>();
            Date date = cellData.getValue().getcDate();
            if (date != null) {
                property.setValue(date.toLocalDate());
            }
            return property;
        });
        tcCStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getcStatus()));
        tcArrangementType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrangmentType()));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcBrokerSSN.setCellValueFactory(new PropertyValueFactory<>("brokerSSN"));
        tcClientSSN.setCellValueFactory(new PropertyValueFactory<>("clientSSN"));
        tcPrNum.setCellValueFactory(new PropertyValueFactory<>("prNum"));

        // Assuming 'cStatus' and 'arrangementType' are properties in your Contract class
        TableColumn<Contract, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("cStatus"));

        TableColumn<Contract, String> arrangementColumn = new TableColumn<>("Arrangement Type");
        arrangementColumn.setCellValueFactory(new PropertyValueFactory<>("arrangementType"));

        // Call the method to display contracts
        showContracts();
    }

    private void showContracts() {
        tvContract.setItems(getContracts());
    }

    private ObservableList<Contract> getContracts() {
        ObservableList<Contract> result = FXCollections.observableArrayList();

        String query = "SELECT * FROM contract";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet queryResult = statement.executeQuery(query)) {

            while (queryResult.next()) {
                result.add(new Contract(
                        queryResult.getString("contractNo"),
                        queryResult.getDate("CDate"),
                        queryResult.getString("Cstatus"),
                        queryResult.getString("ArrangmentType"),
                        queryResult.getDouble("price"),
                        queryResult.getString("brokerSSN"),
                        queryResult.getString("clientSSN"),
                        queryResult.getString("prNum")
                ));
            }

        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
        }

        return result;
    }
   
    public void insertContract() {
        hideAllErrors();
        
        // Get input values from the text fields
        String contractNo = txtContractNo.getText().strip();
        LocalDate cDate = txtCDate.getValue();
        String cStatus = txtCStatus.getText().strip();
        String arrangementType = txtArrangementType.getText().strip();
        String brokerSSN = txtBrokerSSN.getText().strip();
        String clientSSN = txtClientSSN.getText().strip();
        String prNum = txtPrNum.getText().strip();
        double price; // Assuming price is a numeric value, adjust the data type as needed

        try {
            // Parse price from text field
            price = Double.parseDouble(txtPrice.getText().strip());
        } catch (NumberFormatException e) {
            showError("Invalid Price");
            return;
        }

        // Validate input fields
        if (contractNo.isEmpty() || cDate == null || cStatus.isEmpty() || arrangementType.isEmpty()
                || brokerSSN.isEmpty() || clientSSN.isEmpty() || prNum.isEmpty()) {
            showError("Empty Fields");
            return;
        }

        // Check if the broker SSN exists
        if (!checkSSNExists("broker", brokerSSN)) {
            showError("Broker SSN does not exist");
            return;
        }

        // Check if the client SSN exists
        if (!checkSSNExists("client", clientSSN)) {
            showError("Client SSN does not exist");
            return;
        }

        // Construct the SQL query with parameterized values
        String query = "INSERT INTO contract (contractNo, CDate, Cstatus, ArrangmentType, price, brokerSSN, clientSSN, prNum) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
             PreparedStatement statement = conn.prepareStatement(query)) {
            // Set parameter values
            statement.setString(1, contractNo);
            statement.setDate(2, Date.valueOf(cDate));
            statement.setString(3, cStatus);
            statement.setString(4, arrangementType);
            statement.setDouble(5, price);
            statement.setString(6, brokerSSN);
            statement.setString(7, clientSSN);
            statement.setString(8, prNum);

            // Execute the query
            statement.executeUpdate();

            // Show success message or perform any additional actions
            showContracts();

        } catch (SQLException e) {
            // Handle SQL exceptions
            showError("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to check if an SSN exists in a specified table
    private boolean checkSSNExists(String tableName, String ssn) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE SSN = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, ssn);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            showError("Error checking SSN existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }



    public void deleteContract() {
        // Get the selected contract from the table view
        Contract selectedContract = tvContract.getSelectionModel().getSelectedItem();

        if (selectedContract == null) {
            showError("Select a contract to delete");
            return;
        }

        // Get the contract number of the selected contract
        String contractNo = selectedContract.getContractNo();

        // Construct the SQL query to delete the selected contract
        String query = "DELETE FROM contract WHERE contractNo = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
             PreparedStatement statement = conn.prepareStatement(query)) {
            // Set the contract number as a parameter in the query
            statement.setString(1, contractNo);

            // Execute the delete query
            statement.executeUpdate();

            // Show success message or perform any additional actions
            showContracts();

        } catch (SQLException e) {
            // Handle SQL exceptions
            showError("Error deleting contract: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateContract() {
        // Get the selected contract from the table view
        Contract selectedContract = tvContract.getSelectionModel().getSelectedItem();

        if (selectedContract == null) {
            showError("Select a contract to update");
            return;
        }

        // Get updated values from the text fields or other input fields
        String newCStatus = txtNewCStatus.getText().strip();
        String newArrangementType = txtNewArrangementType.getText().strip();
        double newPrice; // Assuming price is a numeric value, adjust the data type as needed

        try {
            // Parse new price from text field
            newPrice = Double.parseDouble(txtNewPrice.getText().strip());
        } catch (NumberFormatException e) {
            showError("Invalid New Price");
            return;
        }

        // Construct the SQL query to update the selected contract
        String query = "UPDATE contract SET Cstatus = ?, ArrangementType = ?, price = ? WHERE contractNo = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
             PreparedStatement statement = conn.prepareStatement(query)) {
            // Set updated values as parameters in the query
            statement.setString(1, newCStatus);
            statement.setString(2, newArrangementType);
            statement.setDouble(3, newPrice);
            statement.setString(4, selectedContract.getContractNo());

            // Execute the update query
            statement.executeUpdate();

            // Show success message or perform any additional actions
            showContracts();

        } catch (SQLException e) {
            // Handle SQL exceptions
            showError("Error updating contract: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void executeQuery(String query) {
        Connection connection = DBConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error executing query", sql_e.getMessage());
        }
    }

    public void showError(String errorMessage) {
        lbGeneralError.setText(errorMessage);
        lbGeneralError.setVisible(true);
    }
    @FXML
    public void hideAllErrors() {
        lbGeneralError.setVisible(false);
    }

}
