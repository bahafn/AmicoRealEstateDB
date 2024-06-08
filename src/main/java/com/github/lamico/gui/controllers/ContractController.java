package com.github.lamico.gui.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Contract;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TableColumn<Contract, String> tcContractNo, tcCDate, tcCStatus, tcArrangmentType, tcPrice, tcBrokerSSN, tcClientSSN, tcPrNum, tcEmployeeSSN;

    @FXML
    private TextField txtContractNo, txtCStatus, txtArrangmentType, txtPrice, txtBrokerSSN, txtClientSSN, txtPrNum, txtEmployeeSSN;

    @FXML
    private DatePicker txtCDate;

    @FXML
    private Label lbGeneralError;

    public void handleRowSelection(MouseEvent event) {
        Contract contract = tvContract.getSelectionModel().getSelectedItem();
        if (contract == null)
            return;

        txtContractNo.setText(String.valueOf(contract.getContractNo()));
        txtCDate.setValue(
                Instant.ofEpochMilli(contract.getcDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        txtCStatus.setText(contract.getcStatus());
        txtArrangmentType.setText(contract.getArrangmentType());
        txtPrice.setText(String.valueOf(contract.getPrice()));
        txtBrokerSSN.setText(contract.getBrokerSSN());
        txtClientSSN.setText(contract.getClientSSN());
        txtPrNum.setText(String.valueOf(contract.getPrNum()));
       
    }

    public void deleteContract() {
        hideAllErrors();

        Contract contract = tvContract.getSelectionModel().getSelectedItem();
        if (contract == null) {
            showError("Select a contract.");
            return;
        }

        String contractNo = contract.getContractNo();
        String query = String.format("DELETE FROM contract WHERE contractNo = '%s'", contractNo);
        executeQuery(query);

        showContracts();
    }

    public void insertContract() {
        hideAllErrors();

        String contractNo = txtContractNo.getText().strip();
        String cDate = txtCDate.getValue() == null ? null : txtCDate.getValue().toString();
        String cStatus = txtCStatus.getText().strip();
        String arrangmentType = txtArrangmentType.getText().strip();
        String price = txtPrice.getText().strip();
        String brokerSSN = txtBrokerSSN.getText().strip();
        String clientSSN = txtClientSSN.getText().strip();
        String prNum = txtPrNum.getText().strip();

        if (contractNo.isEmpty() || cDate == null || cStatus.isEmpty() || arrangmentType.isEmpty() ||
            price.isEmpty() || brokerSSN.isEmpty() || clientSSN.isEmpty() || prNum.isEmpty() ) {
            showError("Empty Fields");
            return;
        }

        String query = String.format("INSERT INTO contract VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", 
                contractNo, cDate, cStatus, arrangmentType, price, brokerSSN, clientSSN, prNum);
        executeQuery(query);

        showContracts();
    }

    public void updateContract() {
        hideAllErrors();

        Contract contract = tvContract.getSelectionModel().getSelectedItem();
        if (contract == null) {
            showError("Select a contract.");
            return;
        }

        String contractNo = contract.getContractNo();
        String cDate = txtCDate.getValue() == null ? null : txtCDate.getValue().toString();
        String cStatus = txtCStatus.getText().strip();
        String arrangmentType = txtArrangmentType.getText().strip();
        String price = txtPrice.getText().strip();
        String brokerSSN = txtBrokerSSN.getText().strip();
        String clientSSN = txtClientSSN.getText().strip();
        String prNum = txtPrNum.getText().strip();
        String employeeSSN = txtEmployeeSSN.getText().strip();

        if (contractNo.isEmpty() || cDate == null || cStatus.isEmpty() || arrangmentType.isEmpty() ||
            price.isEmpty() || brokerSSN.isEmpty() || clientSSN.isEmpty() || prNum.isEmpty() || employeeSSN.isEmpty()) {
            showError("Empty Fields");
            return;
        }

        String query = String.format(
                "UPDATE contract SET cDate = '%s', cStatus = '%s', arrangmentType = '%s', price = '%s', brokerSSN = '%s', clientSSN = '%s', prNum = '%s', employeeSSN = '%s' WHERE contractNo = '%s'",
                cDate, cStatus, arrangmentType, price, brokerSSN, clientSSN, prNum, employeeSSN, contractNo);
        executeQuery(query);

        showContracts();
    }

    private void executeQuery(String query) {
        Connection connection = DBConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException sql_e) {
            showError(sql_e.getMessage());
        }
    }

    private void showError(String errorMessage) {
        lbGeneralError.setText(errorMessage);
        lbGeneralError.setVisible(true);
    }
    
    @FXML
    private void hideAllErrors() {
        lbGeneralError.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcContractNo.setCellValueFactory(new PropertyValueFactory<>("contractNo"));
        tcCDate.setCellValueFactory(new PropertyValueFactory<>("cDate"));
        tcCStatus.setCellValueFactory(new PropertyValueFactory<>("cStatus"));
        tcArrangmentType.setCellValueFactory(new PropertyValueFactory<>("arrangmentType"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcBrokerSSN.setCellValueFactory(new PropertyValueFactory<>("brokerSSN"));
        tcClientSSN.setCellValueFactory(new PropertyValueFactory<>("clientSSN"));
        tcPrNum.setCellValueFactory(new PropertyValueFactory<>("prNum"));


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
                        queryResult.getDate("cDate"),
                        queryResult.getString("cStatus"),
                        queryResult.getString("arrangmentType"),
                        queryResult.getDouble("price"),
                        queryResult.getString("brokerSSN"),
                        queryResult.getString("clientSSN"),
                        queryResult.getString("prNum"))
                       );
            }
        } catch (SQLException sql_e) {
            showError(sql_e.getMessage());
        }

        return result;
    }
}