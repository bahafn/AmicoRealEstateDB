package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Client;
import com.github.lamico.entities.Person;
import com.github.lamico.gui.utils.AlertUtil;
import com.github.lamico.gui.utils.DateUtil;
import com.github.lamico.gui.utils.SQLUtil;
import com.github.lamico.gui.utils.TextFormatterTypes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class ClientController implements Initializable {
    @FXML
    private TableView<Client> tvClient;

    @FXML
    private TableColumn<?, ?> tcBank, tcDate, tcName, tcSSN, tcAddress, tcPhone, tcEmail, tcSponsor, tcIncome,
            tcEmployer;

    @FXML
    private TextField txtAddress, txtBank, txtName, txtSSN, txtPhone, txtEmail, txtSponsor, txtIncomeLevel, txtEmployer;

    @FXML
    private DatePicker txtDate;

    @FXML
    private ComboBox<String> cbPhone, cbEmail;

    @FXML
    private Label lbGeneralError;

    public void handleRowSelection() {
        Client client = tvClient.getSelectionModel().getSelectedItem();
        if (client == null)
            return;

        txtSSN.setText(client.getSsn());
        txtName.setText(client.getPName());
        txtAddress.setText(client.getAddress());
        txtBank.setText(client.getBankInfo());
        txtSponsor.setText(client.getSponsor());
        txtIncomeLevel.setText(client.getIncomeLevel() + "");
        txtEmployer.setText(client.getEmployer());
        txtDate.setValue(DateUtil.sqlDateToLocalDate(client.getDateOfBirth()));

        // Remove all items from phone and email ComboBoxes
        cbPhone.getItems().clear();
        cbEmail.getItems().clear();
        // Add selected phones and emails to ComboBoxes
        cbPhone.getItems().addAll(client.getPhone().split("\n"));
        cbEmail.getItems().addAll(client.getEmail().split("\n"));
    }

    public void deleteClient() {
        hideAllErrors();

        // Get selected Client from TableView
        Client client = tvClient.getSelectionModel().getSelectedItem();
        if (client == null) {
            showError("Select a client.");
            return;
        }

        String ssn = client.getSsn();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }

        String query = String.format("DELETE FROM person WHERE ssn = '%s'", ssn);
        executeQuery(query);

        showClients();
    }

    public void insertClient() {
        hideAllErrors();

        // Get info from input form
        String ssn = SQLUtil.formatStringForQuery(txtSSN.getText(), true);
        String name = SQLUtil.formatStringForQuery(txtName.getText(), true);
        String address = SQLUtil.formatStringForQuery(txtAddress.getText(), true);
        String birthDate = SQLUtil.formatDateForQuery(txtDate.getValue());
        String bankName = SQLUtil.formatStringForQuery(txtBank.getText(), true);
        String sponsor = SQLUtil.formatStringForQuery(txtSponsor.getText(), true);
        String incomeLevel = SQLUtil.formatStringForQuery(txtIncomeLevel.getText(), false);
        String employer = SQLUtil.formatStringForQuery(txtEmployer.getText(), true);

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if any of the required fields is empty.
        if (bankName.equals("null") || sponsor.equals("null")) {
            showError("Empty required Fields (*)");
            return;
        }

        // Check if ssn is already used
        if (Person.searchPerson(ssn)) {
            AlertUtil.showAlert(AlertType.INFORMATION, "Couldn't add Employee", "SSN already used.");
            return;
        }

        String insertPersonQuery = String.format("INSERT INTO person VALUES(%s, %s, %s, %s, %s)", ssn, name,
                address, birthDate, bankName);
        executeQuery(insertPersonQuery);
        String insertClientQuery = String.format("INSERT INTO clientTbl VALUES(%s, %s, %s, %s)", sponsor,
                incomeLevel, employer, ssn);
        executeQuery(insertClientQuery);

        showClients();
    }

    public void updateClient() {
        hideAllErrors();

        // Get selected client from TableView
        Client client = tvClient.getSelectionModel().getSelectedItem();
        if (client == null) {
            showError("Select a client.");
            return;
        }

        String ssn = client.getSsn();
        String name = SQLUtil.formatStringForQuery(txtName.getText(), true);
        String address = SQLUtil.formatStringForQuery(txtAddress.getText(), true);
        String birthDate = SQLUtil.formatDateForQuery(txtDate.getValue());
        String bankName = SQLUtil.formatStringForQuery(txtBank.getText(), true);
        String sponsor = SQLUtil.formatStringForQuery(txtSponsor.getText(), true);
        String incomeLevel = SQLUtil.formatStringForQuery(txtIncomeLevel.getText(), false);
        String employer = SQLUtil.formatStringForQuery(txtEmployer.getText(), true);

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if any of the required fields is empty.
        if (sponsor.equals("null")) {
            showError("Empty required Fields (*)");
            return;
        }

        String updatePersonQuery = String.format(
                "UPDATE person SET pName = %s, Address = %s, dateOfBirth = %s, bankInfo = %s WHERE ssn = %s",
                name, address, birthDate, bankName, ssn);
        executeQuery(updatePersonQuery);
        String updateClientQuery = String.format(
                "UPDATE clientTbl SET sponsor = %s, incomeLevel = %s, employeementInfo = %s WHERE ssn = %s",
                sponsor, incomeLevel, employer, ssn);
        executeQuery(updateClientQuery);

        showClients();
    }

    public void addPhone() {
        hideAllErrors();

        String ssn = tvClient.getSelectionModel().getSelectedItem().getSsn();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if phone is empty
        String phone = cbPhone.getEditor().getText();
        if (phone.equals("")) {
            showError("Invalid phone number");
            return;
        }

        String query = String.format("INSERT INTO phone VALUES ('%s', '%s')", phone, ssn);
        executeQuery(query);

        showClients();
    }

    public void removePhone() {
        hideAllErrors();

        String ssn = txtSSN.getText().strip();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }

        String query = String.format("DELETE FROM phone WHERE ssn = '%s' and phoneNumber = '%s'", ssn,
                cbPhone.getEditor().getText());
        executeQuery(query);

        showClients();
    }

    public void addEmail() {
        hideAllErrors();

        String ssn = txtSSN.getText().strip();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if email is empty or invalid
        String email = cbEmail.getEditor().getText();
        if (email.equals("")) {
            showError("Invalid email.");
            return;
        }

        String query = String.format("INSERT INTO email VALUES ('%s', '%s')", email, ssn);
        executeQuery(query);

        showClients();
    }

    public void removeEmail() {
        hideAllErrors();

        String ssn = txtSSN.getText().strip();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }

        String query = String.format("DELETE FROM email WHERE ssn = '%s' and address = '%s'", ssn,
                cbEmail.getEditor().getText());
        executeQuery(query);

        showClients();
    }

    private void executeQuery(String query) {
        Connection connection = DBConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLIntegrityConstraintViolationException e) {
            // This means we have a duplicate primary key
            // duplicate primary key can mean a duplicate SSN, the client already has this
            // email or phone.
            if (query.contains("email"))
                showError("Email already added.");
            else if (query.contains("phone"))
                showError("Phone number already added.");
            else
                showError("Duplicate SSN");
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
        }
    }

    public void showError(String errorMessage) {
        lbGeneralError.setText(errorMessage);
        lbGeneralError.setVisible(true);
    }

    /** Sets the visibility of all error labels to false. */
    public void hideAllErrors() {
        lbGeneralError.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTableColumns();
        showClients();
        restrictTextFields();
    }

    private void setUpTableColumns() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("pName"));
        tcSSN.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        tcBank.setCellValueFactory(new PropertyValueFactory<>("bankInfo"));
        tcAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcSponsor.setCellValueFactory(new PropertyValueFactory<>("sponsor"));
        tcEmployer.setCellValueFactory(new PropertyValueFactory<>("employer"));
        tcIncome.setCellValueFactory(new PropertyValueFactory<>("incomeLevel"));
    }

    private void restrictTextFields() {
        txtAddress.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(0));
        txtBank.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtName.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtSSN.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
        txtSponsor.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(32));
        txtEmployer.setTextFormatter(TextFormatterTypes.getMaxLengthFormatter(32));
        txtIncomeLevel.setTextFormatter(TextFormatterTypes.getIntFormatter(10));

        cbPhone.getEditor().setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        cbEmail.getEditor().setTextFormatter(TextFormatterTypes.getEmailTextFormatter(64));
    }

    private void showClients() {
        tvClient.setItems(getClients());
    }

    /**
     * Gets all information of all Clients.
     * 
     * @return An ObservableList of all clients.
     */
    private ObservableList<Client> getClients() {
        ObservableList<Client> result = FXCollections.observableArrayList();

        String query = "SELECT c.*, p.pName, p.dateOfBirth, p.address, p.bankInfo, " +
                "GROUP_CONCAT(DISTINCT ph.phoneNumber SEPARATOR '\n') AS phones, " +
                "GROUP_CONCAT(DISTINCT e.address SEPARATOR '\n') AS emails " +
                "FROM clientTbl c " +
                "JOIN person p ON c.ssn = p.ssn " +
                "LEFT JOIN phone ph ON p.ssn = ph.ssn " +
                "LEFT JOIN email e ON p.ssn = e.ssn " +
                "GROUP BY c.ssn, p.ssn, p.pName, p.dateOfBirth, p.address, p.bankInfo";
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery(query)) {

            while (queryResult.next()) {
                result.add(new Client(queryResult.getString("ssn"), queryResult.getString("pName"),
                        queryResult.getString("Address"), queryResult.getDate("dateOfBirth"),
                        queryResult.getString("bankInfo"), queryResult.getString("phones"),
                        queryResult.getString("emails"), queryResult.getString("sponsor"),
                        queryResult.getInt("incomeLevel"), queryResult.getString("employeementInfo")));
            }
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
        }

        return result;
    }
}
