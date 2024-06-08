package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Broker;
import com.github.lamico.entities.CompanyBroker;
import com.github.lamico.entities.IndependentBroker;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class BrokerController implements Initializable {
    @FXML
    private TableView<Person> tvBroker;

    @FXML
    private TableColumn<Person, ?> tcBank, tcDate, tcName, tcSSN, tcAddress, tcPhone, tcEmail, tcSalary, tcHireDate,
            tcDepartment, tcPosition, tcCommission, tcShare;

    @FXML
    private TextField txtAddress, txtBank, txtName, txtSSN, txtPhone, txtEmail, txtSalary, txtPosition, txtDepartment,
            txtCommission, txtShare;

    @FXML
    private DatePicker txtDate, txtHireDate;

    @FXML
    private ComboBox<String> cbPhone, cbEmail;

    @FXML
    private Pane pCommission;

    @FXML
    private VBox mainVBox, vbEmployeeInfo;

    @FXML
    private Label lbGeneralError;

    private boolean companyBroker = false;

    public void handleRowSelection() {
        Broker broker = (Broker) tvBroker.getSelectionModel().getSelectedItem();
        if (broker == null)
            return;

        txtSSN.setText(broker.getSsn());
        txtName.setText(broker.getPName());
        txtAddress.setText(broker.getAddress());
        txtBank.setText(broker.getBankInfo());
        txtShare.setText(broker.getShare() + "");
        txtDate.setValue(DateUtil.sqlDateToLocalDate(broker.getDateOfBirth()));
        if (companyBroker) {
            CompanyBroker companyBroker = (CompanyBroker) broker;

            txtHireDate.setValue(DateUtil.sqlDateToLocalDate(companyBroker.getHireDate()));
            txtSalary.setText(companyBroker.getSalary() + "");
            txtDepartment.setText(companyBroker.getDepartment());
            txtPosition.setText(companyBroker.getEPosition());
        } else
            txtCommission.setText(((IndependentBroker) broker).getCommission() + "");

        // Remove all items from phone and email ComboBoxes
        cbPhone.getItems().clear();
        cbEmail.getItems().clear();
        // Add selected phones and emails to ComboBoxes
        cbPhone.getItems().addAll(broker.getPhone().split("\n"));
        cbEmail.getItems().addAll(broker.getEmail().split("\n"));
    }

    public void deleteBroker() {
        hideAllErrors();

        // Get selected broker from TableView
        Broker broker = (Broker) tvBroker.getSelectionModel().getSelectedItem();
        if (broker == null) {
            showError("Select a broker.");
            return;
        }

        String ssn = broker.getSsn();
        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }

        String query = String.format("DELETE FROM person WHERE ssn = '%s'", ssn);
        executeQuery(query);

        showBrokers();
    }

    public void insertBroker() {
        hideAllErrors();

        // Get info from input form
        String ssn = SQLUtil.formatStringForQuery(txtSSN.getText(), true);
        String name = SQLUtil.formatStringForQuery(txtName.getText(), true);
        String address = SQLUtil.formatStringForQuery(txtAddress.getText(), true);
        String birthDate = SQLUtil.formatDateForQuery(txtDate.getValue());
        String bankName = SQLUtil.formatStringForQuery(txtBank.getText(), true);

        String department = SQLUtil.formatStringForQuery(txtDepartment.getText(), true);
        String position = SQLUtil.formatStringForQuery(txtPosition.getText(), true);
        String hireDate = SQLUtil.formatDateForQuery(txtHireDate.getValue());
        String salary = SQLUtil.formatStringForQuery(txtSalary.getText(), false);
        String commission = SQLUtil.formatStringForQuery(txtCommission.getText(), false);
        String share = SQLUtil.formatStringForQuery(txtShare.getText(), false);

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if any of the required fields is empty
        if (bankName.equals("null") || share.equals("null") || (companyBroker && (department.equals("null")
                || position.equals("null") || salary.equals("null")))
                || (!companyBroker && commission.equals("null"))) {
            showError("Empty required Fields (*)");
            return;
        }

        // Check if ssn is already added
        if (Person.searchPerson(ssn)) {
            AlertUtil.showAlert(AlertType.INFORMATION, "Couldn't add Broker", "SSN already used.");
            return;
        }

        // Add person to person table
        String addPersonQuery = String.format("INSERT INTO person VALUES(%s, %s, %s, %s, %s)", ssn, name,
                address, birthDate, bankName);
        executeQuery(addPersonQuery);
        // Add broker to broker table
        String addBrokerQuery = String.format("INSERT INTO broker VALUES(%s, %s)", share, ssn);
        executeQuery(addBrokerQuery);

        if (!companyBroker) {
            // Add broker to IndependentBroker table
            String addIBQuery = String.format("INSERT INTO independentBroker VALUES(%s, %s)", commission, ssn);
            executeQuery(addIBQuery);
        } else {
            // Add broker's employee info to employee table
            String addEmployeeQuery = String.format("INSERT INTO employee VALUES(%s, %s, %s, %s, %s)", salary,
                    hireDate, position, department, ssn);
            executeQuery(addEmployeeQuery);
        }

        showBrokers();
    }

    public void updateBroker() {
        hideAllErrors();

        // Get selected broker from TableView
        Broker broker = (Broker) tvBroker.getSelectionModel().getSelectedItem();
        if (broker == null) {
            showError("Empty required Fields (*)");
            return;
        }

        // Get info from input form
        String ssn = broker.getSsn();
        String name = SQLUtil.formatStringForQuery(txtName.getText(), true);
        String address = SQLUtil.formatStringForQuery(txtAddress.getText(), true);
        String birthDate = SQLUtil.formatDateForQuery(txtDate.getValue());
        String bankName = SQLUtil.formatStringForQuery(txtBank.getText(), true);

        String department = SQLUtil.formatStringForQuery(txtDepartment.getText(), true);
        String position = SQLUtil.formatStringForQuery(txtPosition.getText(), true);
        String hireDate = SQLUtil.formatDateForQuery(txtHireDate.getValue());
        String salary = SQLUtil.formatStringForQuery(txtSalary.getText(), false);
        String commission = SQLUtil.formatStringForQuery(txtCommission.getText(), false);
        String share = SQLUtil.formatStringForQuery(txtShare.getText(), false);

        if (ssn.length() < 9) {
            showError("SSN Invalid");
            return;
        }
        // Check if any of the required fields is empty
        if (bankName.equals("null") || share.equals("null") || (companyBroker && (department.equals("null")
                || position.equals("null") || salary.equals("null")))
                || (!companyBroker && commission.equals("null"))) {
            showError("Empty Fields");
            return;
        }

        // Update info that is in the person table
        String updatePersonQuery = String.format(
                "UPDATE person SET pName = %s, Address = %s, dateOfBirth = %s, bankInfo = %s WHERE ssn = %s",
                name, address, birthDate, bankName, ssn);
        executeQuery(updatePersonQuery);

        // Update info that is in the broker table
        String updateBrokerQuery = String.format("UPDATE broker SET bShare = %s WHERE ssn = %s", share, ssn);
        executeQuery(updateBrokerQuery);

        if (companyBroker) {
            // Update info that is in the employee table
            String updateEmployeeQuery = String.format(
                    "UPDATE employee SET salary = %s, department = %s, ePosition = %s, hireDate = %s WHERE ssn = %s",
                    salary, department, position, hireDate, ssn);
            executeQuery(updateEmployeeQuery);
        } else {
            // Update info that is the independentBroker table
            String updateIBQuery = String.format("UPDATE independentBroker SET commission = %s WHERE ssn = %s",
                    commission, ssn);
            executeQuery(updateIBQuery);
        }

        showBrokers();
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

        showBrokers();
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

        showBrokers();
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

        showBrokers();
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

        showBrokers();
    }

    private void executeQuery(String query) {
        try {
            SQLUtil.executeQuery(query);
        } catch (SQLIntegrityConstraintViolationException sql_icve) {
            sql_icve.printStackTrace();
            // This means we have a duplicate primary key
            // Duplicate primary key can mean a duplicate SSN or the broker already has
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

    /**
     * Changes the value of companyBroker var and hides or shows independent
     * broker's inputs and employee's (company broker's) inputs.
     */
    public void changeCompanyBroker() {
        companyBroker = !companyBroker;

        // Remove not needed input fields and out fields
        if (companyBroker) {
            mainVBox.getChildren().remove(pCommission);
            mainVBox.getChildren().add(vbEmployeeInfo);
            tvBroker.getColumns().add(tcPosition);
            tvBroker.getColumns().add(tcHireDate);
            tvBroker.getColumns().add(tcSalary);
            tvBroker.getColumns().add(tcDepartment);
        } else {
            mainVBox.getChildren().remove(vbEmployeeInfo);
            tvBroker.getColumns().remove(tcPosition);
            tvBroker.getColumns().remove(tcHireDate);
            tvBroker.getColumns().remove(tcSalary);
            tvBroker.getColumns().remove(tcDepartment);
            mainVBox.getChildren().add(pCommission);
        }

        // Update TableView to only include wanted brokers
        showBrokers();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hide all employee info input and output fields
        mainVBox.getChildren().remove(vbEmployeeInfo);
        mainVBox.getChildren().remove(vbEmployeeInfo);
        tvBroker.getColumns().remove(tcPosition);
        tvBroker.getColumns().remove(tcHireDate);
        tvBroker.getColumns().remove(tcSalary);
        tvBroker.getColumns().remove(tcDepartment);

        setUpTableColumns();
        showBrokers();
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
        tcPosition.setCellValueFactory(new PropertyValueFactory<>("ePosition"));
        tcHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        tcSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tcCommission.setCellValueFactory(new PropertyValueFactory<>("commission"));
        tcShare.setCellValueFactory(new PropertyValueFactory<>("share"));
    }

    private void restrictTextFields() {
        txtAddress.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(0));
        txtBank.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtName.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
        txtSSN.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
        txtSalary.setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        txtDepartment.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(16));
        txtPosition.setTextFormatter(TextFormatterTypes.getMaxLengthFormatter(16));
        txtShare.setTextFormatter(TextFormatterTypes.getDoubleTextFormatter(0));
        txtCommission.setTextFormatter(TextFormatterTypes.getIntFormatter(10));

        cbPhone.getEditor().setTextFormatter(TextFormatterTypes.getIntFormatter(10));
        cbEmail.getEditor().setTextFormatter(TextFormatterTypes.getEmailTextFormatter(64));
    }

    private void showBrokers() {
        tvBroker.setItems(getBrokers());
    }

    /**
     * Gets all information of all Brokers.
     * 
     * @return An ObservableList of all brokers.
     */
    private ObservableList<Person> getBrokers() {
        ObservableList<Person> result = FXCollections.observableArrayList();

        String query;
        if (companyBroker) {
            query = "SELECT b.*, p.pName, p.dateOfBirth, p.address, p.bankInfo, e.ePosition, e.salary, e.department, e.hireDate, "
                    + "GROUP_CONCAT(DISTINCT ph.phoneNumber ORDER BY ph.phoneNumber SEPARATOR '\n') AS phones, "
                    + "GROUP_CONCAT(DISTINCT em.address ORDER BY em.address SEPARATOR '\n') AS emails "
                    + "FROM broker b "
                    + "JOIN employee e ON b.ssn = e.ssn "
                    + "JOIN person p ON b.ssn = p.ssn "
                    + "LEFT JOIN phone ph ON p.ssn = ph.ssn "
                    + "LEFT JOIN email em ON p.ssn = em.ssn "
                    + "GROUP BY b.ssn, p.ssn, p.pName, p.dateOfBirth, p.address, p.bankInfo";
        } else {
            query = "SELECT ib.*, b.bShare, p.pName, p.dateOfBirth, p.address, p.bankInfo, "
                    + "GROUP_CONCAT(DISTINCT ph.phoneNumber ORDER BY ph.phoneNumber SEPARATOR '\n') AS phones, "
                    + "GROUP_CONCAT(DISTINCT em.address ORDER BY em.address SEPARATOR '\n') AS emails "
                    + "FROM independentBroker ib "
                    + "JOIN broker b ON ib.ssn = b.ssn "
                    + "JOIN person p ON b.ssn = p.ssn "
                    + "LEFT JOIN phone ph ON p.ssn = ph.ssn "
                    + "LEFT JOIN email em ON p.ssn = em.ssn "
                    + "GROUP BY ib.ssn , p.ssn , p.pName , p.dateOfBirth , p.address , p.bankInfo";
        }
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery(query)) {

            while (queryResult.next()) {
                if (companyBroker) {
                    result.add(new CompanyBroker(queryResult.getString("ssn"), queryResult.getString("pName"),
                            queryResult.getString("Address"), queryResult.getDate("dateOfBirth"),
                            queryResult.getString("bankInfo"), queryResult.getString("phones"),
                            queryResult.getString("emails"), queryResult.getDouble("bShare"),
                            queryResult.getString("ePosition"), queryResult.getString("department"),
                            queryResult.getInt("salary"), queryResult.getDate("hireDate")));
                } else {
                    result.add(new IndependentBroker(queryResult.getString("ssn"), queryResult.getString("pName"),
                            queryResult.getString("Address"), queryResult.getDate("dateOfBirth"),
                            queryResult.getString("bankInfo"), queryResult.getString("phones"),
                            queryResult.getString("emails"), queryResult.getDouble("bShare"),
                            queryResult.getInt("commission")));
                }
            }
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
        }

        return result;
    }
}
