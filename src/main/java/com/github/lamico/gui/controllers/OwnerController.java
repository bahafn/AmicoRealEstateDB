package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Person;
import com.github.lamico.gui.utils.TextFormatterTypes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;

public class OwnerController implements Initializable {
    @FXML
    private TableView<Person> tvOwner;

    @FXML
    private TableColumn<?, ?> tcBank, tcDate, tcName, tcSSN, tcAddress, tcPhone, tcEmail;

    @FXML
    private TextField txtAddress, txtBank, txtName, txtSSN, txtPhone, txtEmail;

    @FXML
    private DatePicker txtDate;

    @FXML
    private ComboBox<String> cbPhone, cbEmail;

    @FXML
    private Label lbGeneralError;

    private Alert alert = new Alert(AlertType.ERROR);

    public void handleRowSelection(MouseEvent event) {
        Person owner = (Person) tvOwner.getSelectionModel().getSelectedItem();
        if (owner == null)
            return;

        txtSSN.setText(owner.getSsn() + "");
        txtName.setText(owner.getPName());
        txtAddress.setText(owner.getAddress());
        txtBank.setText(owner.getBankInfo());
        txtDate.setValue(
                Instant.ofEpochMilli(owner.getDateOfBirth().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

        // Remove all items from phone and email ComboBoxes
        cbPhone.getItems().clear();
        cbEmail.getItems().clear();
        // Add selected phones and emails to ComboBoxes
        cbPhone.getItems().addAll(owner.getPhone().split("\n"));
        cbEmail.getItems().addAll(owner.getEmail().split("\n"));
    }

    public void deleteOwner() {
        hideAllErrors();

        String ssn = tvOwner.getSelectionModel().getSelectedItem().getSsn();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }

        String query = String.format("DELETE FROM person WHERE ssn = '%s'", ssn);
        executeQuery(query);

        showOwners();
    }

    public void insertOwner() {
        hideAllErrors();

        String ssn = txtSSN.getText().strip();
        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();
        String birthDate = txtDate.getValue() == null ? null : txtDate.getValue().toString();
        String bankName = txtBank.getText().strip();

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        if (name.isEmpty() || address.isEmpty() || birthDate == null || bankName.isEmpty()) {
            showError("Empty Fields");
            return;
        }

        String query = String.format("INSERT INTO person VALUES('%s', '%s', '%s', '%s', '%s')", ssn, name, address,
                birthDate, bankName);
        executeQuery(query);

        showOwners();
    }

    public void updateOwner() {
        hideAllErrors();

        String ssn = tvOwner.getSelectionModel().getSelectedItem().getSsn();
        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();
        String birthDate = txtDate.getValue() == null ? null : txtDate.getValue().toString();
        String bankName = txtBank.getText().strip();

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        if (name.isEmpty() || address.isEmpty() || birthDate.isEmpty() || bankName.isEmpty()) {
            showError("Empty Fields");
            return;
        }

        String query = String.format(
                "UPDATE person SET pName = '%s', Address = '%s', dateOfBirth = '%s', bankInfo = '%s' WHERE ssn = '%s'",
                name, address, birthDate, bankName, ssn);
        executeQuery(query);

        showOwners();
    }

    public void addPhone() {
        hideAllErrors();

        String ssn = txtSSN.getText().strip();
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

        showOwners();
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

        showOwners();
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

        showOwners();
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

        showOwners();
    }

    private void executeQuery(String query) {
        Connection connection = DBConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLIntegrityConstraintViolationException e) {
            // This means we have a duplicate primary key
            // duplicate primary key can mean a duplicate SSN, the owner already has this email or phone.
            if (query.contains("email"))
                showError("Email already added.");
            else if (query.contains("phone"))
                showError("Phone number already added.");
            else
                showError("Duplicate SSN");
        } catch (SQLException sql_e) {
            alert.setHeaderText("Error reading database");
            alert.setContentText(sql_e.getMessage());
            alert.show();
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
        // Set up cell factories
        tcName.setCellValueFactory(new PropertyValueFactory<>("pName"));
        tcSSN.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        tcDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        tcBank.setCellValueFactory(new PropertyValueFactory<>("bankInfo"));
        tcAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        showOwners();
        restrictTextFields();
    }

    private void restrictTextFields() {
        txtAddress.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(0));
        txtBank.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtName.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtSSN.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
        cbPhone.getEditor().setTextFormatter(TextFormatterTypes.getIntFormatter(15));
        cbEmail.getEditor().setTextFormatter(TextFormatterTypes.getEmailTextFormatter(64));
    }

    private void showOwners() {
        tvOwner.setItems(getOwners());
    }

    /**
     * Gets all information of all Owners.
     * 
     * @return An ObservableList of all owners.
     */
    private ObservableList<Person> getOwners() {
        ObservableList<Person> result = FXCollections.observableArrayList();

        String query = "SELECT p.*, " +
                "GROUP_CONCAT(DISTINCT ph.phoneNumber SEPARATOR '\n') AS phones, " +
                "GROUP_CONCAT(DISTINCT e.address SEPARATOR '\n') AS emails " +
                "FROM person p " +
                "LEFT JOIN phone ph ON p.ssn = ph.ssn " +
                "LEFT JOIN email e ON p.ssn = e.ssn " +
                "WHERE p.ssn not in (select ssn from employee) " + 
                "and p.ssn not in (select ssn from clientTbl) " + 
                "and p.ssn not in (select ssn from broker)" +
                "GROUP BY p.ssn, p.pName";
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery(query)) {

            while (queryResult.next()) {
                result.add(new Person(queryResult.getString("ssn"), queryResult.getString("pName"),
                        queryResult.getString("Address"), queryResult.getDate("dateOfBirth"),
                        queryResult.getString("bankInfo"), queryResult.getString("phones"),
                        queryResult.getString("emails")));
            }
        } catch (SQLException sql_e) {
            alert.setHeaderText("Error reading database");
            alert.setContentText(sql_e.getMessage());
            alert.show();
        }

        return result;
    }
}
