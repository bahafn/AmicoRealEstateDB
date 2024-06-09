package com.github.lamico.gui.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

import com.github.lamico.db.DBConnection;
import com.github.lamico.gui.utils.AlertUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DashboardController implements Initializable {
	@FXML
	public Label txtEmployeeNum, txtClientNum, txtBrokerNum, txtOwnerNum;

	@FXML
	public BarChart<String, Integer> bcOwnerAges;

	@FXML
	public TableView<DepartmentSalaryInfo> tvDepartmentSalaries;

	@FXML
	private BarChart<String, Number> bcPricePerArea;

	@FXML
	private LineChart<?, ?> lcBuildingPriceOverTheYears;

	@FXML
	private TableView<?> tbvRealEstateAreas;

	@FXML
	private TableColumn<?, ?> tvMostPopularAreas, tvAverageRealEstateSellingPrice, tvNumberOfSellers;

	@FXML
	private Label txtAvgPropertyPrice, txtTotalApartments, txtTotalBuildings, txtTotalLand, txtTotalProperties,
			txtTotalValue;

	@FXML
	public TableColumn<?, ?> tcDepartment, tcAvgSalary, tcMinSalary, tcMaxSalary;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set up the top text information
		setUpTextCounters();

		// Set information in graphs
		setUpGraph();

		// Set information in tables
		setUpTableColumns();
		setUpEmployeeTable();
		setUpPricePerAreaChart();
	}

	private void setUpPricePerAreaChart() {
		// Clear existing data
		bcPricePerArea.getData().clear();

		bcPricePerArea.getXAxis().setLabel("Property Type");
		bcPricePerArea.getYAxis().setLabel("Average Price per Sq. Meter");

		XYChart.Series<String, Number> series = new XYChart.Series<>();

		String query = "SELECT 'Real Estate' AS property_type, AVG(valuation / area) AS avg_price_per_sqm "
		        + "FROM RealEstate "
		        + "UNION ALL "
		        + "SELECT 'Land', AVG(re.valuation / re.area) "
		        + "FROM Land le INNER JOIN RealEstate re ON le.prNum = re.prNum "
		        + "UNION ALL "
		        + "SELECT 'Building', AVG(re.valuation / re.area) "
		        + "FROM Building be INNER JOIN RealEstate re ON be.prNum = re.prNum "
		        + "UNION ALL "
		        + "SELECT 'Apartment', AVG(re.valuation / re.area) "
		        + "FROM Apartment ap INNER JOIN RealEstate re ON ap.prNum = re.prNum "
		        + "INNER JOIN SaleApartment sa ON ap.prNum = sa.prNum";

		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				String propertyType = resultSet.getString("property_type");
				double avgPricePerSqm = resultSet.getDouble("avg_price_per_sqm");
				XYChart.Data<String, Number> data = new XYChart.Data<>(propertyType, avgPricePerSqm);
				series.getData().add(data);
			}

			// Add series to the bar chart
			bcPricePerArea.getData().add(series);

		} catch (SQLException e) {
			AlertUtil.showAlert(AlertType.ERROR, "Error", "Error fetching data: " + e.getMessage());
		}
	}

	private void setUpTableColumns() {
		tcDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
		tcAvgSalary.setCellValueFactory(new PropertyValueFactory<>("avgSalary"));
		tcMinSalary.setCellValueFactory(new PropertyValueFactory<>("minSalary"));
		tcMaxSalary.setCellValueFactory(new PropertyValueFactory<>("maxSalary"));
	}

	private void setUpTextCounters() {
		txtEmployeeNum.setText("Number of Employees:\n" + queryResultCount("SELECT COUNT(*) FROM employee"));
		txtClientNum.setText("Number of Clients:\n" + queryResultCount("SELECT COUNT(*) FROM clientTbl"));
		txtBrokerNum.setText("Number of Brokers:\n" + queryResultCount("SELECT COUNT(*) FROM broker"));
		txtOwnerNum.setText("Number of Owners:\n" + queryResultCount("SELECT COUNT(*) " + "FROM person p "
				+ "WHERE p.ssn not in (select ssn from employee) " + "and p.ssn not in (select ssn from clientTbl) "
				+ "and p.ssn not in (select ssn from broker)"));
	}

	private void setUpEmployeeTable() {
		String query = "SELECT department, AVG(salary) as avg_salary, MIN(salary) as min_salary, "
				+ "MAX(salary) as max_salary " + "FROM employee " + "GROUP BY department;";

		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet queryResult = statement.executeQuery(query)) {

			ObservableList<DepartmentSalaryInfo> info = FXCollections.observableArrayList();

			while (queryResult.next()) {
				info.add(new DepartmentSalaryInfo(queryResult.getString("department"),
						queryResult.getDouble("avg_salary"), queryResult.getInt("max_salary"),
						queryResult.getInt("min_salary")));
			}

			tvDepartmentSalaries.setItems(info);
		} catch (SQLException sql_e) {
			AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setUpGraph() {
		// Clear bar chart's data so we don't add any data twice
		bcOwnerAges.getData().removeAll(bcOwnerAges.getData());

		// Setup axises of bar graph
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("Count")));

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Number of Owners");

		ArrayList<String> ages = new ArrayList<>();
		ArrayList<Integer> counts = new ArrayList<>();

		// Prepare data for the bar graph
		String query = "SELECT YEAR(dateOfBirth), COUNT(*) " + "FROM person p "
				+ "WHERE p.ssn not in (select ssn from employee) " + "and p.ssn not in (select ssn from clientTbl) "
				+ "and p.ssn not in (select ssn from broker) " + "and dateOfBirth is not null "
				+ "GROUP BY YEAR(dateOfBirth)";
		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet queryResult = statement.executeQuery(query)) {

			while (queryResult.next()) {
				ages.add(calculateAge(queryResult.getInt(1)) + "");
				counts.add(queryResult.getInt(2));
			}
		} catch (SQLException sql_e) {
			AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
		}

		// Sort ages
		Collections.sort(ages);

		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		series.setName("Count");

		for (int i = 0; i < ages.size(); i++)
			series.getData().add(new XYChart.Data(ages.get(i), counts.get(i)));

		bcOwnerAges.getData().add(series);
	}

	/** Takes a date of birth and calculates the age of it. */
	private int calculateAge(int date) {
		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();
		return currentYear - date;
	}

	public void refresh() {
		setUpTextCounters();
		setUpGraph();
	}

	/** Takes a query containing COUNT SQL function and returns the number */
	public int queryResultCount(String query) {
		try (Connection connection = DBConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet queryResult = statement.executeQuery(query)) {

			queryResult.next();
			return queryResult.getInt("count(*)");
		} catch (SQLException sql_e) {
			AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
			return 0;
		}
	}

	/** This class is used to add into tvDepartmentSalaries. */
	public class DepartmentSalaryInfo {
		private String department;
		private double avgSalary;
		private int maxSalary;
		private int minSalary;

		public DepartmentSalaryInfo(String department, double avgSalary, int maxSalary, int minSalary) {
			setDepartment(department);
			setAvgSalary(avgSalary);
			setMaxSalary(maxSalary);
			setMinSalary(minSalary);
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getDepartment() {
			return this.department;
		}

		public void setAvgSalary(double avgSalary) {
			this.avgSalary = avgSalary;
		}

		public double getAvgSalary() {
			return this.avgSalary;
		}

		public void setMaxSalary(int maxSalary) {
			this.maxSalary = maxSalary;
		}

		public int getMaxSalary() {
			return this.maxSalary;
		}

		public void setMinSalary(int minSalary) {
			this.minSalary = minSalary;
		}

		public int getMinSalary() {
			return this.minSalary;
		}
	}
}
