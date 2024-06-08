package com.github.lamico.gui.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Transaction;
import com.github.lamico.gui.utils.AlertUtil;
import com.github.lamico.gui.utils.TextFormatterTypes;

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



public class TransactionController implements Initializable {
    @FXML
    private TableView<Transaction> tvTransaction;

    @FXML
    private TableColumn<?, ?> tcTransactionId, tcPaymentDate, tcAmount, tcRecipient, tcSender, tcContractNo;

    
   
    @FXML
    private TextField txtTransactionId,  txtAmount, txtRecipient, txtSender, txtContractNo;

    @FXML
    private DatePicker txtPaymentDate;

    @FXML
    private Label lbGeneralError;

    public void handleRowSelection(MouseEvent event) {
        Transaction transaction = tvTransaction.getSelectionModel().getSelectedItem();
        if (transaction == null)
            return;

        txtPaymentDate.setValue(
                Instant.ofEpochMilli(transaction.getPaymentDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        txtAmount.setText(String.valueOf(transaction.getAmount()));
        txtRecipient.setText(transaction.getRecipient());
        txtSender.setText(transaction.getSender());
        txtContractNo.setText(String.valueOf(transaction.getContractNo()));
        txtTransactionId.setText(transaction.getID());
    }

    public void deleteTransaction() {
        hideAllErrors();

        Transaction transaction = tvTransaction.getSelectionModel().getSelectedItem();
        if (transaction == null) {
            showError("Select a transaction.");
            return;
        }

        String contractNo = transaction.getContractNo();
        String query = String.format("DELETE FROM transaction WHERE contractNo = '%s'", contractNo);
        executeQuery(query);

        showTransactions();
    }

    public void insertTransaction() {
        hideAllErrors();
        String TransactionId = txtTransactionId.getText().strip();
        String amount = txtAmount.getText().strip();
        String recipient = txtRecipient.getText().strip();
        String sender = txtSender.getText().strip();
        String contractNo = txtContractNo.getText().strip();
      
        String paymentDate = txtPaymentDate.getValue() == null ? null : txtPaymentDate.getValue().toString();

        if (amount.isEmpty() || recipient.isEmpty() || sender.isEmpty() || contractNo.isEmpty() || TransactionId.isEmpty() || paymentDate == null) {
            showError("Empty Fields");
            return;
        }

        String query = String.format("INSERT INTO transaction VALUES('%s', '%s', '%s', '%s', '%s', '%s')", 
                paymentDate, amount, recipient, sender, contractNo, txtTransactionId);
        executeQuery(query);

        showTransactions();
    }

    public void updateTransaction() {
        hideAllErrors();

        Transaction transaction = tvTransaction.getSelectionModel().getSelectedItem();
        if (transaction == null) {
            showError("Select a transaction.");
            return;
        }

        String amount = txtAmount.getText().strip();
        String recipient = txtRecipient.getText().strip();
        String sender = txtSender.getText().strip();
        String contractNo = txtContractNo.getText().strip();
        String TransactionId = txtTransactionId.getText().strip();
        String paymentDate = txtPaymentDate.getValue() == null ? null : txtPaymentDate.getValue().toString();

        if (amount.isEmpty() || recipient.isEmpty() || sender.isEmpty() || contractNo.isEmpty() || TransactionId.isEmpty() || paymentDate == null) {
            showError("Empty Fields");
            return;
        }

        String query = String.format(
                "UPDATE transaction SET paymentDate = '%s', amount = '%s', recipient = '%s', sender = '%s', TransactionId = '%s' WHERE contractNo = '%s'",
                paymentDate, amount, recipient, sender, TransactionId, contractNo);
        executeQuery(query);

        showTransactions();
    }

    private void executeQuery(String query) {
        Connection connection = DBConnection.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLIntegrityConstraintViolationException e) {
            showError("Duplicate transaction");
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
        }
    }

    public void showError(String errorMessage) {
        lbGeneralError.setText(errorMessage);
        lbGeneralError.setVisible(true);
    }
    
    public void hideAllErrors() {
        lbGeneralError.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        tcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tcRecipient.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        tcSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        tcContractNo.setCellValueFactory(new PropertyValueFactory<>("contractNo"));
        tcTransactionId.setCellValueFactory(new PropertyValueFactory<>("TransactionId"));

        showTransactions();
        restrictTextFields();
    }

    private void restrictTextFields() {
        txtAmount.setTextFormatter(TextFormatterTypes.getSQLDateFormatter());
        txtRecipient.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtSender.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtContractNo.setTextFormatter(TextFormatterTypes.getIntFormatter(0));
        txtTransactionId.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
    }

    private void showTransactions() {
        tvTransaction.setItems(getTransactions());
    }

    private ObservableList<Transaction> getTransactions() {
        ObservableList<Transaction> result = FXCollections.observableArrayList();

        String query = "SELECT * FROM transaction";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet queryResult = statement.executeQuery(query)) {

            while (queryResult.next()) {
                result.add(new Transaction(
                		queryResult.getString("TransactionId"),
                        queryResult.getDate("paymentDate"),
                        queryResult.getDouble("amount"),
                        queryResult.getString("recipient"),
                        queryResult.getString("sender"),
                        queryResult.getString("contractNo")
                         ));
            }
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
        }

        return result;
    }
}