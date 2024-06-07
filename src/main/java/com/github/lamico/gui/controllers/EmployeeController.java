package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Employee;
import com.github.lamico.entities.Person;
import com.github.lamico.gui.utils.AlertUtil;
import com.github.lamico.gui.utils.SQLUtils;
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
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;

public class EmployeeController implements Initializable {
    @FXML
    private TableView<Employee> tvEmployee;

    @FXML
    private TableColumn<?, ?> tcBank, tcDate, tcName, tcSSN, tcAddress, tcPhone, tcEmail, tcSalary, tcHireDate,
            tcDepartment, tcPosition;

    @FXML
    private TextField txtAddress, txtBank, txtName, txtSSN, txtPhone, txtEmail, txtSalary, txtPosition, txtDepartment;

    @FXML
    private DatePicker txtDate, txtHireDate;

    @FXML
    private ComboBox<String> cbPhone, cbEmail;

    @FXML
    private Label lbGeneralError;

    public void handleRowSelection(MouseEvent event) {
        Employee employee = tvEmployee.getSelectionModel().getSelectedItem();
        if (employee == null)
            return;

        txtSSN.setText(employee.getSsn());
        txtName.setText(employee.getPName());
        txtAddress.setText(employee.getAddress());
        txtBank.setText(employee.getBankInfo());
        txtSalary.setText(employee.getSalary() + "");
        txtDepartment.setText(employee.getDepartment());
        txtPosition.setText(employee.getEPosition());
        txtDate.setValue(
                Instant.ofEpochMilli(employee.getDateOfBirth().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        txtHireDate.setValue(
                Instant.ofEpochMilli(employee.getHireDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

        // Remove all items from phone and email ComboBoxes
        cbPhone.getItems().clear();
        cbEmail.getItems().clear();
        // Add selected phones and emails to ComboBoxes
        cbPhone.getItems().addAll(employee.getPhone().split("\n"));
        cbEmail.getItems().addAll(employee.getEmail().split("\n"));
    }

    public void deleteEmployee() {
        hideAllErrors();

        // Get selected employee from TableView
        Employee employee = tvEmployee.getSelectionModel().getSelectedItem();
        if (employee == null) {
            showError("Select an employee.");
            return;
        }

        String ssn = employee.getSsn();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }

        String query = String.format("DELETE FROM person WHERE ssn = '%s'", ssn);
        executeQuery(query);

        showEmployees();
    }

    public void insertEmployee() {
        hideAllErrors();

        // Get info from input form
        String ssn = SQLUtils.formatStringForQuery(txtSSN.getText(), true);
        String name = SQLUtils.formatStringForQuery(txtName.getText(), true);
        String address = SQLUtils.formatStringForQuery(txtAddress.getText(), true);
        String birthDate = SQLUtils.formatDateForQuery(txtDate.getValue());
        String bankName = SQLUtils.formatStringForQuery(txtBank.getText(), true);
        String department = SQLUtils.formatStringForQuery(txtDepartment.getText(), true);
        String position = SQLUtils.formatStringForQuery(txtPosition.getText(), true);
        String hireDate = SQLUtils.formatDateForQuery(txtHireDate.getValue());
        String salary = SQLUtils.formatStringForQuery(txtSalary.getText(), false);

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if any of the required fields is empty.
        if (bankName.equals("null") || department.equals("null") || position.equals("null") || salary.equals("null")) {
            showError("Empty required fields.");
            return;
        }

        // Check if ssn is already belongs to an employee
        if (Person.searchPerson(ssn)) {
            AlertUtil.showAlert(AlertType.INFORMATION, "Couldn't add Employee", "SSN already used.");
            return;
        }

        // Add person to person table
        String addPersonQuery = String.format("INSERT INTO person VALUES(%s, %s, %s, %s, %s)", ssn, name, address,
                birthDate, bankName);
        executeQuery(addPersonQuery);
        System.out.println(true);
        // Add employee to employee table
        String addEmployeeQuery = String.format("INSERT INTO employee VALUES(%s, %s, %s, %s, %s)", salary, hireDate,
                position, department, ssn);
        executeQuery(addEmployeeQuery);

        showEmployees();
    }

    public void updateEmployee() {
        hideAllErrors();

        // Get selected employee from TableView
        Employee employee = tvEmployee.getSelectionModel().getSelectedItem();
        if (employee == null) {
            showError("Select an employee.");
            return;
        }

        String ssn = employee.getSsn();

        // Get info from input form
        String name = SQLUtils.formatStringForQuery(txtName.getText(), true);
        String address = SQLUtils.formatStringForQuery(txtAddress.getText(), true);
        String birthDate = SQLUtils.formatDateForQuery(txtDate.getValue());
        String bankName = SQLUtils.formatStringForQuery(txtBank.getText(), true);
        String department = SQLUtils.formatStringForQuery(txtDepartment.getText(), true);
        String position = SQLUtils.formatStringForQuery(txtPosition.getText(), true);
        String hireDate = SQLUtils.formatDateForQuery(txtHireDate.getValue());
        String salary = SQLUtils.formatStringForQuery(txtSalary.getText(), false);

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if any of the required fields is empty.
        if (bankName.equals("null") || department.equals("null") || position.equals("null") || salary.equals("null")) {
            showError("Empty required fields.");
            return;
        }

        String updatePersonQuery = String.format(
                "UPDATE person SET pName = %s, Address = %s, dateOfBirth = %s, bankInfo = %s WHERE ssn = %s",
                name, address, birthDate, bankName, ssn);
        executeQuery(updatePersonQuery);
        String updateEmployeeQuery = String.format(
                "UPDATE employee SET salary = %s, department = %s, ePosition = %s, hireDate = %s WHERE ssn = %s",
                salary, department, position, hireDate, ssn);
        executeQuery(updateEmployeeQuery);

        showEmployees();
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

        showEmployees();
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

        showEmployees();
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

        showEmployees();
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

        showEmployees();
    }

    private void executeQuery(String query) {
        try {
            SQLUtils.executeQuery(query);
        } catch (SQLIntegrityConstraintViolationException sql_icve) {
            // This means we have a duplicate primary key
            // Duplicate primary key can mean a duplicate SSN or the employee already has
            // this email or phone.
            if (query.contains("email"))
                showError("Email already added.");
            else if (query.contains("phone"))
                showError("Phone number already added.");
            else
                showError("Duplicate SSN.");
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Database Error", sql_e.getMessage());
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
        tcPosition.setCellValueFactory(new PropertyValueFactory<>("ePosition"));
        tcHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        tcSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));

        showEmployees();
        restrictTextFields();
    }

    private void restrictTextFields() {
        txtAddress.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(0));
        txtBank.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtName.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtSSN.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
        txtSalary.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtDepartment.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(16));
        txtPosition.setTextFormatter(TextFormatterTypes.getMaxLengthFormatter(16));

        cbPhone.getEditor().setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        cbEmail.getEditor().setTextFormatter(TextFormatterTypes.getEmailTextFormatter(64));
    }

    private void showEmployees() {
        tvEmployee.setItems(getEmployees());
    }

    /**
     * Gets all information of all Employees.
     * 
     * @return An ObservableList of all employees.
     */
    private ObservableList<Employee> getEmployees() {
        ObservableList<Employee> result = FXCollections.observableArrayList();

        String query = "SELECT e.*, p.pName, p.dateOfBirth, p.address, p.bankInfo, "
                + "GROUP_CONCAT(DISTINCT ph.phoneNumber ORDER BY ph.phoneNumber SEPARATOR '\n') AS phones, "
                + "GROUP_CONCAT(DISTINCT em.address ORDER BY em.address SEPARATOR '\n') AS emails "
                + "FROM employee e "
                + "JOIN person p ON e.ssn = p.ssn "
                + "LEFT JOIN phone ph ON p.ssn = ph.ssn "
                + "LEFT JOIN email em ON p.ssn = em.ssn "
                + "GROUP BY e.ssn, p.pName, p.dateOfBirth, p.address, p.bankInfo";
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery(query)) {

            while (queryResult.next()) {
                result.add(new Employee(queryResult.getString("ssn"), queryResult.getString("pName"),
                        queryResult.getString("Address"), queryResult.getDate("dateOfBirth"),
                        queryResult.getString("bankInfo"), queryResult.getString("phones"),
                        queryResult.getString("emails"), queryResult.getString("ePosition"),
                        queryResult.getString("department"), queryResult.getInt("salary"),
                        queryResult.getDate("hireDate")));
            }
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
        }

        return result;
    }
}
