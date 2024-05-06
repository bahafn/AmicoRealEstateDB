package com.github.lamico.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.github.lamico.managers.ConfigManager;

public class DBConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DB_NAME = "g5_lamicodb";
    private static final String USERNAME = ConfigManager.getProperty("Username");
    private static final String PASSWORD = ConfigManager.getProperty("Password");

    private static Connection connection;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DB_NAME),
                    USERNAME, PASSWORD);
        } catch (SQLException se) {
        	System.out.println("Check if the Username and Password in config.properties are correct.");
            se.printStackTrace();
        }

        return connection;
    }
}
