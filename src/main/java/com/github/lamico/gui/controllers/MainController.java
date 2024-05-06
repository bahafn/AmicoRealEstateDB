package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> tvBank;

    @FXML
    private TableColumn<?, ?> tvBirthDate;

    @FXML
    private TableView<Person> tvEmployee;

    @FXML
    private TableColumn<?, ?> tvName;

    @FXML
    private TableColumn<?, ?> tvSSN;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtBankName;

    @FXML
    private TextField txtBirthDate;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSSN;

    @FXML
    public void deleteEmployee(ActionEvent event) {

    }

    @FXML
    public void handleRowSelection(MouseEvent event) {
        Person person = tvEmployee.getSelectionModel().getSelectedItem();
        if (person == null)
            return;

        txtSSN.setText(person.getSsn() + "");
        txtName.setText(person.getPName());
        txtAddress.setText(person.getAddress());
        txtBankName.setText(person.getBankInfo());
        txtBirthDate.setText(person.getDateOfBirth().toString());
    }

    @FXML
    public void insertEmployee(ActionEvent event) {

    }

    @FXML
    public void updateEmployee(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showEmployees();
    }

    private void showEmployees() {
        tvName.setCellValueFactory(new PropertyValueFactory<>("pName"));
        tvSSN.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        tvBirthDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        tvBank.setCellValueFactory(new PropertyValueFactory<>("bankInfo"));

        tvEmployee.setItems(getPersons("SELECT * FROM person"));
    }

    private ObservableList<Person> getPersons(String query) {
        ObservableList<Person> result = FXCollections.observableArrayList();
        Connection connection = DBConnection.getConnection();

        Statement statement = null;
        ResultSet queryResult = null;

        try {
            statement = connection.createStatement();
            queryResult = statement.executeQuery(query);

            // Check if no data was retrieved
            if (!queryResult.isBeforeFirst()) {
                System.out.println("No employees found.");
                return null;
            }

            // Add all elements in the query result to the result list
            while (queryResult.next())
                result.add(new Person(queryResult.getInt("ssn"), queryResult.getString("pName"),
                        queryResult.getString("Address"), queryResult.getDate("dateOfBirth"),
                        queryResult.getString("bankInfo")));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        // Close all resources
        try {
            statement.close();
            queryResult.close();
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;
    }

    private void executeQuery(String query) {
        Connection connection = DBConnection.getConnection();

        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
