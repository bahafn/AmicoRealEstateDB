package com.github.lamico.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.entities.Person;
import com.github.lamico.gui.utils.TextFormatterTypes;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
	private TableColumn<?, ?> tvAddress;

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
	private Label lbAddressError;

	@FXML
	private Label lbBankError;

	@FXML
	private Label lbBirthDateError;

	@FXML
	private Label lbNameError;

	@FXML
	private Label lbSSNError;

	@FXML
	private Label lblDeleteError;

	@FXML
	private Label lblInsertError;

	@FXML
	private Label lblUpdateError;;

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
	public void deleteEmployee(ActionEvent event) {
		String ssn = txtSSN.getText().trim();
		if (ssn.length() < 9) {
			displayErrorMessage(lblDeleteError, "SSN Invalid", 2);
			return;
		}
		String query = "DELETE FROM person WHERE ssn = '" + ssn + "'";
		executeQuery(query);
		showEmployees();
	}

	@FXML
	public void insertEmployee(ActionEvent event) {
		String ssn = txtSSN.getText().strip();
		String name = txtName.getText().strip();
		String address = txtAddress.getText().strip();
		String birthDate = txtBirthDate.getText().strip();
		String bankName = txtBankName.getText().strip();

		if (ssn.length() < 9) {
			displayErrorMessage(lblInsertError, "SSN Invalid", 2);
			return;
		}
		if (name.isEmpty() || address.isEmpty() || birthDate.isEmpty() || bankName.isEmpty()) {
			displayErrorMessage(lblInsertError, "Empty Fields", 2);
			return;
		}

		String query = "INSERT INTO person (ssn, pName, Address, dateOfBirth, bankInfo) VALUES ('" + ssn + "', '" + name
				+ "', '" + address + "', '" + birthDate + "', '" + bankName + "')";
		executeQuery(query);

		showEmployees();
	}

	@FXML
	public void updateEmployee(ActionEvent event) {
		String ssn = txtSSN.getText().trim();
		String name = txtName.getText();
		String address = txtAddress.getText();
		String birthDate = txtBirthDate.getText();
		String bankName = txtBankName.getText();

		if (ssn.length() < 9) {
			displayErrorMessage(lblUpdateError, "SSN Invalid", 2);
			return;
		}
		if (name.isEmpty() || address.isEmpty() || birthDate.isEmpty() || bankName.isEmpty()) {
			displayErrorMessage(lblUpdateError, "Empty Fields", 2);
			return;
		}

		String query = "UPDATE person SET pName = '" + name + "', Address = '" + address + "', dateOfBirth = '"
				+ birthDate + "', bankInfo = '" + bankName + "' WHERE ssn = '" + ssn + "'";
		executeQuery(query);
		showEmployees();
	}

	private void executeQuery(String query) {
		Connection connection = DBConnection.getConnection();

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLIntegrityConstraintViolationException e) {
			// Duplicate primary key
			displayErrorMessage(lbSSNError, "Duplicate!", 2);
		} catch (MysqlDataTruncation e) {
			// Incorrect data format
			String errorMessage = e.getMessage();

			if (errorMessage.contains("Incorrect date value")) {
				displayErrorMessage(lbBirthDateError, "Wrong Format!", 2);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	private PauseTransition pauseTransition;

	public void displayErrorMessage(Label label, String errorMessage, int durationInSeconds) {
		Platform.runLater(() -> {
			if (pauseTransition != null) {
				pauseTransition.getOnFinished().handle(null);
				pauseTransition.stop();
			}
			label.setText(errorMessage);
			pauseTransition = new PauseTransition(Duration.seconds(durationInSeconds));
			pauseTransition.setOnFinished(event -> label.setText(""));
			pauseTransition.play();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showEmployees();
		restrictTexFields();
	}

	private void restrictTexFields() {
		txtAddress.setTextFormatter(TextFormatterTypes.getAlphanumericWordCharsAndCommasFormatter(0));
		txtBankName.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
		txtName.setTextFormatter(TextFormatterTypes.getAlphaWordCharsFormatter(0));
		txtSSN.setTextFormatter(TextFormatterTypes.getIntFormatter(9));
		txtBirthDate.setTextFormatter(TextFormatterTypes.getSQLDateFormatter());
	}

	private void showEmployees() {
		tvEmployee.setItems(getPersons("SELECT * FROM person"));
		// Set up cell factories
		tvName.setCellValueFactory(new PropertyValueFactory<>("pName"));
		tvSSN.setCellValueFactory(new PropertyValueFactory<>("ssn"));
		tvBirthDate.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
		tvBank.setCellValueFactory(new PropertyValueFactory<>("bankInfo"));
		tvAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
	}

	private ObservableList<Person> getPersons(String query) {
		ObservableList<Person> result = FXCollections.observableArrayList();
		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet queryResult = statement.executeQuery(query)) {

			while (queryResult.next()) {
				result.add(new Person(queryResult.getString("ssn"), queryResult.getString("pName"),
						queryResult.getString("Address"), queryResult.getDate("dateOfBirth"),
						queryResult.getString("bankInfo")));
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}

}
