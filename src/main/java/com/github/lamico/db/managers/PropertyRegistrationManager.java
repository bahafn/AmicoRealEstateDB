package com.github.lamico.db.managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;

public class PropertyRegistrationManager {
	private static volatile boolean finishedRegistering = false;
	private static volatile long prNum = -1;
	private static Connection connection = null;

	private PropertyRegistrationManager() {
	}

	public static synchronized void registerRealEstate(String prCondition, String city, String streetName,
			double valuation, String areaDescription, double area, String ownerSSN) throws SQLException {
		// Check if previous registration has finished, if not, delete the real estate
		if (!finishedRegistering && connection != null && !connection.isClosed()) {
			connection.rollback();
			connection.close();
		}

		// Updates to false if a property is successfully registered
		finishedRegistering = true;
		prNum = -1;

		String insertRealEstateQuery = "INSERT INTO RealEstate "
				+ "(prCondition, city, streetName, valuation, areaDescription, area, ownerSSN) " + "VALUES ('"
				+ prCondition + "', '" + city + "', '" + streetName + "', " + valuation + ", '" + areaDescription
				+ "', " + area + ", '" + ownerSSN + "')";

		connection = DBConnection.getConnection();
		connection.setAutoCommit(false);

		try (Statement statement = connection.createStatement()) {
			// Insert a real estate and allow it to return the new property id created
			int affectedRows = statement.executeUpdate(insertRealEstateQuery, Statement.RETURN_GENERATED_KEYS);
			if (affectedRows == 0) {
				throw new SQLException("Creating property failed, no rows affected.");
			}

			// Get the last inserted prNum
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				finishedRegistering = false;
				prNum = resultSet.getLong(1); // Return prNum to complete registration
			} else {
				throw new SQLException("Creating property failed, no ID obtained.");
			}
		}
		connection.rollback();

	}

	public static synchronized void registerLand(int plotNum, int blockNum) throws SQLException {
		String insertLandQuery = "INSERT INTO Land (prNum, plotNum, blockNum) VALUES (" + prNum + ", " + plotNum + ", "
				+ blockNum + ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(insertLandQuery);
			connection.commit();
			finishedRegistering = true;
		}
	}

	public static synchronized void registerBuilding(int landNum, String bName, int yearBuilt, int floorNum)
			throws SQLException {
		String insertBuildingQuery = "INSERT INTO Building (prNum, landNum, bName, yearBuilt, floorNum) VALUES ("
				+ prNum + ", " + landNum + ", '" + bName + "', " + yearBuilt + ", " + floorNum + ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(insertBuildingQuery);
			connection.commit();
			finishedRegistering = true;
		}

	}

	public static synchronized void registerApartment(int buildingNum, int roomNum, int unitNum, int bedroomNum,
			int bathroomNum, int livingroomNum, boolean hasBalcony, String kitchenType, boolean hasGarden)
			throws SQLException {
		String insertApartmentQuery = "INSERT INTO Apartment (prNum, buildingNum, roomNum, unitNum, bedroomNum, "
				+ "bathroomNum, livingroomNum, hasBalcony, kitchenType, hasGarden) VALUES (" + prNum + ", "
				+ buildingNum + ", " + roomNum + ", " + unitNum + ", " + bedroomNum + ", " + bathroomNum + ", "
				+ livingroomNum + ", " + (hasBalcony ? 1 : 0) + ", '" + kitchenType + "', " + (hasGarden ? 1 : 0) + ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(insertApartmentQuery);
		}
	}

	public static synchronized void registerRentalApartment(double price) throws SQLException {
		String insertRentalApartmentQuery = "INSERT INTO RentalApartment (prNum, price) VALUES (" + prNum + ", " + price
				+ ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(insertRentalApartmentQuery);
			connection.commit();
			finishedRegistering = true;
		}
	}

	public static synchronized void registerSaleApartment(double rent) throws SQLException {
		String insertSaleApartmentQuery = "INSERT INTO SaleApartment (prNum, rent) VALUES (" + prNum + ", " + rent
				+ ")";

		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(insertSaleApartmentQuery);
			connection.commit();
			finishedRegistering = true;
		}
	}

	public static long getPrNum() {
		return prNum;
	}
}
