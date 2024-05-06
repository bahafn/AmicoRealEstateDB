package com.github.lamico;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DB_NAME = "g5_lamicodb";
    private static final String USERNAME = "Lamico";
    private static final String PASSWORD = "123456";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DB_NAME),
                    USERNAME, PASSWORD);
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return connection;
    }
}
