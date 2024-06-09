package com.github.lamico.gui.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.github.lamico.db.DBConnection;
import com.github.lamico.db.managers.PropertyRegistrationManager;
import com.github.lamico.entities.Person;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.github.lamico.gui.utils.TimedError;
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;

/**
 * Controller for the property registration GUI.
 */
public class PropertyController {
    private TimedError timedError = new TimedError();
    private Map<String, Background> backgroundMap = new HashMap<>();

    @FXML
    private Button btRegister;

    @FXML
    private BorderPane root;

    @FXML
    private RadioButton rbShowOwners;

    @FXML
    private Label lbError;

    @FXML
    private TableView<Person> tbvTable;

    @FXML
    private TableColumn<?, String> tvEmail;

    @FXML
    private TableColumn<?, String> tvName;

    @FXML
    private TableColumn<?, String> tvSSN;

    @FXML
    private TextField txtArea;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtCondition;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtOwner;

    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtValuation;

    private String from;

    /**
     * Handles row selection in the table.
     * 
     * @param event the mouse event
     */
    @FXML
    void handleRowSelection(MouseEvent event) {
        Person person = tbvTable.getSelectionModel().getSelectedItem();
        if (person == null)
            return;

        txtOwner.setText(person.getSsn());
    }

    /**
     * Displays or hides the owner table based on the radio button selection.
     * 
     * @param event the action event
     */
    @FXML
    void displayOwnerTable(ActionEvent event) {
        if (rbShowOwners.isSelected()) {
            showOwners();
        } else {
            hideOwners();
        }
    }

    /**
     * Hides the owner table.
     */
    public void hideOwners() {
        rbShowOwners.selectedProperty().set(false);
        tbvTable.setVisible(false);
        tbvTable.setDisable(true);
        tbvTable.getSelectionModel().clearSelection();
    }

    /**
     * Registers a new property.
     * 
     * @param event the action event
     */
    @FXML
    void register(ActionEvent event) {
        String description = txtArea.getText().strip();
        String city = txtCity.getText().strip();
        String condition = txtCondition.getText().strip();
        String street = txtStreet.getText().strip();
        double area = Double.parseDouble(txtArea.getText().strip());
        double valuation = Double.parseDouble(txtValuation.getText().strip());
        String owner = txtOwner.getText().strip();

        if (description.isBlank() || city.isBlank() || condition.isBlank() || street.isBlank() || owner.isBlank()
                || area == 0 || valuation == 0) {
            timedError.displayErrorMessage(lbError, "Empty Fields", 2);
            return;
        }
        if (owner.length() < 9) {
            timedError.displayErrorMessage(lbError, "Invalid SSN", 2);
            return;
        }
        try {
            PropertyRegistrationManager.startTransaction();
            PropertyRegistrationManager.registerRealEstate(condition, city, street, valuation, description, area,
                    owner);
            goToNext();
        } catch (SQLException e) {
            timedError.displayErrorMessage(lbError, "Failed to Register", 2);
            e.printStackTrace();
        }
    }

    /**
     * Goes to the next screen based on the from parameter.
     */
    public void goToNext() {
        switch (from) {
        case TabManager.APARTMENTS:
            MainController.getTabManager().switchTo(TabManager.APARTMENT_REGISTER);
            break;
        case TabManager.LAND:
            MainController.getTabManager().switchTo(TabManager.LAND_REGISTER);
            break;
        case TabManager.BUILDINGS:
            MainController.getTabManager().switchTo(TabManager.BUILDING_REGISTER);
            break;
        default:
            break;
        }
    }

    /**
     * Initializes the controller.
     */
    @FXML
    void initialize() {
        backgroundMap.put(TabManager.BUILDINGS, ResourceManager.getBackground("buildings.jpg"));
        backgroundMap.put(TabManager.APARTMENTS, ResourceManager.getBackground("flat.jpg"));
        backgroundMap.put(TabManager.LAND, ResourceManager.getBackground("land.png"));

        restrictFields();
    }

    /**
     * Restricts the input fields to specific formats.
     */
    private void restrictFields() {
        txtArea.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));
        txtValuation.setTextFormatter(TextFormatterTypes.getDecimalTextFormatter(12, 2));

        txtCondition.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(255));
        txtDescription.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(255));

        txtOwner.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
        txtCity.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(40));
        txtStreet.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
    }

    /**
     * Shows the register screen based on the from parameter.
     * 
     * @param from the from parameter
     */
    public void showRegisterScreen(String from) {
        root.setBackground(backgroundMap.get(from));

        clearAllFields();
        hideOwners();

        this.from = from;

        MainController.getTabManager().switchTo(TabManager.PROPERTY);
    }

    /**
     * Clears all input fields.
     */
    public void clearAllFields() {
        txtArea.clear();
        txtCity.clear();
        txtCondition.clear();
        txtDescription.clear();
        txtOwner.clear();
        txtStreet.clear();
        txtValuation.clear();
    }

    /**
     * Shows the owner table.
     */
    public void showOwners() {
        tbvTable.setItems(getOwners());
        tvName.setCellValueFactory(new PropertyValueFactory<>("pName"));
        tvSSN.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        tvEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tbvTable.setVisible(true);
        tbvTable.setDisable(false);
    }

    /**
     * Gets the list of owners from the database.
     * 
     * @return the list of owners
     */
    private ObservableList<Person> getOwners() {
        ObservableList<Person> result = FXCollections.observableArrayList();
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery("SELECT p.ssn, p.pName, "
                        + "GROUP_CONCAT(DISTINCT e.address SEPARATOR '\n') AS emails " + "FROM Person p "
                        + "LEFT JOIN email e ON p.ssn = e.ssn " + "WHERE p.ssn not in (select ssn from employee) "
                        + "and p.ssn not in (select ssn from clientTbl) " + "and p.ssn not in (select ssn from broker) "
                        + "GROUP BY p.ssn, p.pName")) {

            while (queryResult.next()) {
                result.add(new Person(queryResult.getString("ssn"), queryResult.getString("pName"),
                        queryResult.getString("emails")));
            }
        } catch (SQLException e) {
            timedError.displayErrorMessage(lbError, "Failed to get owners", 2);
            e.printStackTrace();
        }
        return result;
    }
}