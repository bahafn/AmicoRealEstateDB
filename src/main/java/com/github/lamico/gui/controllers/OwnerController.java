package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Person;
import com.github.lamico.gui.utils.TextFormatterTypes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private Button btnDelete, btnInsert, btnUpdate;

    @FXML
    private TableView<Person> tvOwner;

    @FXML
    private TableColumn<?, ?> tcBank, tcDate, tcName, tcSSN, tcAddress, tcPhone, tcEmail;

    @FXML
    private TextField txtAddress, txtBank, txtName, txtSSN, txtPhone, txtEmail;

    @FXML
    private DatePicker txtDate;

    @FXML
    private Label lbAddressError, lbBankError, lbDateError, lbNameError, lbSSNError, lbGeneralError;

    @FXML
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
    }

    @FXML
    public void deleteOwner(ActionEvent event) {
        hideAllErrors();

        String ssn = tvOwner.getSelectionModel().getSelectedItem().getSsn();
        if (ssn.length() < 9) {
            showError(lbSSNError, "SSN Invalid");
            return;
        }

        String query = String.format("DELETE FROM person WHERE ssn = '%s'", ssn);
        executeQuery(query);

        showOwners();
    }

    @FXML
    public void insertOwner(ActionEvent event) {
        hideAllErrors();

        String ssn = txtSSN.getText().strip();
        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();
        String birthDate = txtDate.getValue() == null ? null : txtDate.getValue().toString();
        String bankName = txtBank.getText().strip();

        if (ssn.length() < 9) {
            showError(lbSSNError, "SSN Invalid");
            return;
        }
        if (name.isEmpty() || address.isEmpty() || birthDate == null || bankName.isEmpty()) {
            showError(lbGeneralError, "Empty Fields");
            return;
        }

        String query = String.format("INSERT INTO person VALUES('%s', '%s', '%s', '%s', '%s')", ssn, name, address,
                birthDate, bankName);
        executeQuery(query);

        showOwners();
    }

    @FXML
    public void updateOwner(ActionEvent event) {
        hideAllErrors();

        String ssn = tvOwner.getSelectionModel().getSelectedItem().getSsn();
        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();
        String birthDate = txtDate.getValue() == null ? null : txtDate.getValue().toString();
        String bankName = txtBank.getText().strip();

        if (ssn.length() < 9) {
            showError(lbSSNError, "SSN Invalid");
            return;
        }
        if (name.isEmpty() || address.isEmpty() || birthDate.isEmpty() || bankName.isEmpty()) {
            showError(lbGeneralError, "Empty Fields");
            return;
        }

        String query = String.format(
                "UPDATE person SET pName = '%s', Address = '%s', dateOfBirth = '%s', bankInfo = '%s' WHERE ssn = '%s'",
                name, address, birthDate, bankName, ssn);
        executeQuery(query);

        showOwners();
    }

    private void executeQuery(String query) {
        Connection connection = DBConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate primary key
            showError(lbSSNError, "Duplicate!");
        } catch (SQLException ignored) {
        }
    }

    public void showError(Label label, String errorMessage) {
        label.setText(errorMessage);
        label.setVisible(true);
    }

    public void hideAllErrors() {
        lbAddressError.setVisible(false);
        lbBankError.setVisible(false);
        lbDateError.setVisible(false);
        lbNameError.setVisible(false);
        lbSSNError.setVisible(false);
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
            sql_e.printStackTrace();
        }

        return result;
    }
}
