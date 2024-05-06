package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MainController {

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
    private TableColumn<?, ?> tvDepartment;

    @FXML
    private TableView<?> tvEmployee;

    @FXML
    private TableColumn<?, ?> tvHireDate;

    @FXML
    private TableColumn<?, ?> tvName;

    @FXML
    private TableColumn<?, ?> tvPosition;

    @FXML
    private TableColumn<?, ?> tvSSN;

    @FXML
    private TableColumn<?, ?> tvSalary;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtBankName;

    @FXML
    private TextField txtBirthDate;

    @FXML
    private TextField txtDepartment;

    @FXML
    private TextField txtHireDate;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPosition;

    @FXML
    private TextField txtSSN;

    @FXML
    private TextField txtSalary;

    @FXML
    void deleteEmployee(ActionEvent event) {

    }

    @FXML
    void handleRowSelection(MouseEvent event) {

    }

    @FXML
    void insertEmployee(ActionEvent event) {

    }

    @FXML
    void updateEmployee(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btnDelete != null : "fx:id=\"btnDelete\" was not injected: check your FXML file 'root.fxml'.";
        assert btnInsert != null : "fx:id=\"btnInsert\" was not injected: check your FXML file 'root.fxml'.";
        assert btnUpdate != null : "fx:id=\"btnUpdate\" was not injected: check your FXML file 'root.fxml'.";
        assert tvBank != null : "fx:id=\"tvBank\" was not injected: check your FXML file 'root.fxml'.";
        assert tvBirthDate != null : "fx:id=\"tvBirthDate\" was not injected: check your FXML file 'root.fxml'.";
        assert tvDepartment != null : "fx:id=\"tvDepartment\" was not injected: check your FXML file 'root.fxml'.";
        assert tvEmployee != null : "fx:id=\"tvEmployee\" was not injected: check your FXML file 'root.fxml'.";
        assert tvHireDate != null : "fx:id=\"tvHireDate\" was not injected: check your FXML file 'root.fxml'.";
        assert tvName != null : "fx:id=\"tvName\" was not injected: check your FXML file 'root.fxml'.";
        assert tvPosition != null : "fx:id=\"tvPosition\" was not injected: check your FXML file 'root.fxml'.";
        assert tvSSN != null : "fx:id=\"tvSSN\" was not injected: check your FXML file 'root.fxml'.";
        assert tvSalary != null : "fx:id=\"tvSalary\" was not injected: check your FXML file 'root.fxml'.";
        assert txtAddress != null : "fx:id=\"txtAddress\" was not injected: check your FXML file 'root.fxml'.";
        assert txtBankName != null : "fx:id=\"txtBankName\" was not injected: check your FXML file 'root.fxml'.";
        assert txtBirthDate != null : "fx:id=\"txtBirthDate\" was not injected: check your FXML file 'root.fxml'.";
        assert txtDepartment != null : "fx:id=\"txtDepartment\" was not injected: check your FXML file 'root.fxml'.";
        assert txtHireDate != null : "fx:id=\"txtHireDate\" was not injected: check your FXML file 'root.fxml'.";
        assert txtName != null : "fx:id=\"txtName\" was not injected: check your FXML file 'root.fxml'.";
        assert txtPosition != null : "fx:id=\"txtPosition\" was not injected: check your FXML file 'root.fxml'.";
        assert txtSSN != null : "fx:id=\"txtSSN\" was not injected: check your FXML file 'root.fxml'.";
        assert txtSalary != null : "fx:id=\"txtSalary\" was not injected: check your FXML file 'root.fxml'.";

    }

}
