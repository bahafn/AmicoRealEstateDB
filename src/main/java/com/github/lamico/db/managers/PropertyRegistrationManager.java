package com.github.lamico.db.managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;

public class PropertyRegistrationManager {
    private static volatile long prNum = -1;
    private static Connection connection = null;

    private PropertyRegistrationManager() {
    }

    public static synchronized void startTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.close();
        }
        connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        prNum = -1;
    }

    public static synchronized void registerRealEstate(String prCondition, String city, String streetName,
            double valuation, String areaDescription, double area, String ownerSSN) throws SQLException {
        String insertRealEstateQuery = "INSERT INTO RealEstate "
                + "(prCondition, city, streetName, valuation, areaDescription, area, ownerSSN) "
                + "VALUES ('" + prCondition + "', '" + city + "', '" + streetName + "', " + valuation + ", '"
                + areaDescription + "', " + area + ", '" + ownerSSN + "')";

        try (Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate(insertRealEstateQuery, Statement.RETURN_GENERATED_KEYS);
            if (affectedRows == 0) {
                throw new SQLException("Creating property failed, no rows affected.");
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                prNum = resultSet.getLong(1);
            } else {
                throw new SQLException("Creating property failed, no ID obtained.");
            }
        }
    }

    public static synchronized void registerLand(int plotNum, int blockNum) throws SQLException {
        String insertLandQuery = "INSERT INTO Land (prNum, plotNum, blockNum) VALUES (" + prNum + ", " + plotNum + ", "
                + blockNum + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertLandQuery);
        }
    }

    public static synchronized void registerBuilding(int landNum, String bName, int yearBuilt, int floorNum)
            throws SQLException {
        String insertBuildingQuery = "INSERT INTO Building (prNum, landNum, bName, yearBuilt, floorNum) VALUES ("
                + prNum + ", " + landNum + ", '" + bName + "', " + yearBuilt + ", " + floorNum + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertBuildingQuery);
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
        String insertRentalApartmentQuery = "INSERT INTO RentalApartment (prNum, rent) VALUES (" + prNum + ", " + price
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertRentalApartmentQuery);
        }
    }

    public static synchronized void registerSaleApartment(double rent) throws SQLException {
        String insertSaleApartmentQuery = "INSERT INTO SaleApartment (prNum, price) VALUES (" + prNum + ", " + rent
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertSaleApartmentQuery);
        }
    }

    public static synchronized void commitTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
            connection.close();
        }
        prNum = -1;
    }

    public static synchronized void rollbackTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.close();
        }
        prNum = -1;
    }

    public static long getPrNum() {
        return prNum;
    }
}
