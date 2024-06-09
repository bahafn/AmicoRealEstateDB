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
import com.github.lamico.managers.ResourceManager;
import com.github.lamico.managers.TabManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;

public class PropertyController {
	Map<String, Background> backgroundMap = new HashMap<>();

	@FXML
	private Button btRegister;

	@FXML
	private BorderPane root;

	@FXML
	private RadioButton rbShowOwners;

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

	private String backTo;

	@FXML
	void handleRowSelection(MouseEvent event) {
		Person person = tbvTable.getSelectionModel().getSelectedItem();
		if (person == null)
			return;

		txtOwner.setText(person.getSsn() + "");
	}

	@FXML
	void displayOwnerTable(ActionEvent event) {
		if (rbShowOwners.isSelected()) {
			showOwners();
		} else {
			hideOwners();
		}
	}

	public void hideOwners() {
		rbShowOwners.selectedProperty().set(false);
		tbvTable.setVisible(false);
		tbvTable.setDisable(true);
		tbvTable.getSelectionModel().clearSelection();
	}

	@FXML
	void register(ActionEvent event) {
		MainController.getTabManager().switchTo(backTo);
	}

	@FXML
	void initialize() {
		backgroundMap.put(TabManager.BUILDINGS, ResourceManager.getBackground("buildings.jpg"));
		backgroundMap.put(TabManager.APARTMENTS, ResourceManager.getBackground("flat.jpg"));
		backgroundMap.put(TabManager.LAND, ResourceManager.getBackground("land.png"));
		
		restrictFields();
	}

	private void restrictFields() {
		txtArea.setTextFormatter(TextFormatterTypes.getDoubleTextFormatter(0));
		txtValuation.setTextFormatter(TextFormatterTypes.getDoubleTextFormatter(0));
		
		txtCondition.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(255));
		txtDescription.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(255));
		
		txtOwner.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
		txtCity.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(40));
		txtStreet.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(40));
		
	}

	public void showRegisterScreen(String from) {
		root.setBackground(backgroundMap.get(from));

		clearAllFields();
		hideOwners();

		this.backTo = from;

		MainController.getTabManager().switchTo(TabManager.PROPERTY);
	}

	public void clearAllFields() {
		txtArea.clear();
		txtCity.clear();
		txtCondition.clear();
		txtDescription.clear();
		txtOwner.clear();
		txtStreet.clear();
		txtValuation.clear();
	}

	public void registerProperty() {
		String description = txtArea.getText().strip();
		String city = txtCity.getText().strip();
		String condition = txtCondition.getText().strip();
		String street = txtStreet.getText().strip();
		double area = Double.parseDouble(txtArea.getText().strip());
		double valuation = Double.parseDouble(txtValuation.getText().strip());
		String owner = txtOwner.getText().strip();
		try {
			PropertyRegistrationManager.registerRealEstate(condition, city, street, valuation, description, area,
					owner);
		} catch (SQLException e) {
		}
	}

	public void showOwners() {
		tbvTable.setItems(getOwners());
		tvName.setCellValueFactory(new PropertyValueFactory<>("pName"));
		tvSSN.setCellValueFactory(new PropertyValueFactory<>("ssn"));
		tvEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

		tbvTable.setVisible(true);
		tbvTable.setDisable(false);
	}

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
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

}
