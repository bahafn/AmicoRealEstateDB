package com.github.lamico.db.managers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.lamico.db.DBConnection;

/**
 * This class manages property registrations in the database.
 */
public class PropertyRegistrationManager {
    /**
     * The current property registration number.
     */
    private static volatile long prNum = -1;

    /**
     * The database connection.
     */
    private static Connection connection = null;

    /**
     * Private constructor to prevent instantiation.
     */
    private PropertyRegistrationManager() {
    }

    /**
     * Starts a new transaction.
     * 
     * @throws SQLException if a database error occurs
     */
    public static synchronized void startTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.close();
        }
        connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        prNum = -1;
    }

    /**
     * Registers a new real estate property.
     * 
     * @param prCondition    the property condition
     * @param city           the city
     * @param streetName     the street name
     * @param valuation     the valuation
     * @param areaDescription the area description
     * @param area           the area
     * @param ownerSSN      the owner's SSN
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Registers a new land property.
     * 
     * @param plotNum the plot number
     * @param blockNum the block number
     * @throws SQLException if a database error occurs
     */
    public static synchronized void registerLand(int plotNum, int blockNum) throws SQLException {
        String insertLandQuery = "INSERT INTO Land (prNum, plotNum, blockNum) VALUES (" + prNum + ", " + plotNum + ", "
                + blockNum + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertLandQuery);
        }
    }

    /**
     * Registers a new building.
     * 
     * @param landNum the land number
     * @param bName   the building name
     * @param yearBuilt the year built
     * @param floorNum the floor number
     * @throws SQLException if a database error occurs
     */
    public static synchronized void registerBuilding(int landNum, String bName, int yearBuilt, int floorNum)
            throws SQLException {
        String insertBuildingQuery = "INSERT INTO Building (prNum, landNum, bName, yearBuilt, floorNum) VALUES ("
                + prNum + ", " + landNum + ", '" + bName + "', " + yearBuilt + ", " + floorNum + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertBuildingQuery);
        }
    }

    /**
     * Registers a new apartment.
     * 
     * @param buildingNum the building number
     * @param roomNum    the room number
     * @param unitNum   the unit number
     * @param bedroomNum the bedroom number
     * @param bathroomNum the bathroom number
     * @param livingroomNum the living room number
     * @param hasBalcony whether the apartment has a balcony
     * @param kitchenType the kitchen type
     * @param hasGarden whether the apartment has a garden
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Registers a new rental apartment.
     * 
     * @param price the rent price
     * @throws SQLException if a database error occurs
     */
    public static synchronized void registerRentalApartment(double price) throws SQLException {
        String insertRentalApartmentQuery = "INSERT INTO RentalApartment (prNum, rent) VALUES (" + prNum + ", " + price
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertRentalApartmentQuery);
        }
    }

    /**
     * Registers a new sale apartment.
     * 
     * @param rent the sale price
     * @throws SQLException if a database error occurs
     */
    public static synchronized void registerSaleApartment(double rent) throws SQLException {
        String insertSaleApartmentQuery = "INSERT INTO SaleApartment (prNum, price) VALUES (" + prNum + ", " + rent
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(insertSaleApartmentQuery);
        }
    }

    /**
     * Commits the current transaction.
     * 
     * @throws SQLException if a database error occurs
     */
    public static synchronized void commitTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
            connection.close();
        }
        prNum = -1;
    }

    /**
     * Rolls back the current transaction.
     * 
     * @throws SQLException if a database error occurs
     */
    public static synchronized void rollbackTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.close();
        }
        prNum = -1;
    }

    /**
     * Gets the current property registration number.
     * 
     * @return the property registration number
     */
    public static long getPrNum() {
        return prNum;
    }
}