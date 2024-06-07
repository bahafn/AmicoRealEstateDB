package com.github.lamico.gui.utils;

import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.github.lamico.db.DBConnection;

public class SQLUtils {
    private SQLUtils() {
    }

    /** Executes an SQL statement without returning anything. */
    public static void executeQuery(String query) throws SQLIntegrityConstraintViolationException, SQLException {
        Connection connection = DBConnection.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    /**
     * Takes a any String and returns a String ready to be used in an SQL query.
     * <p>
     * For example, adds quotations around the string if needed, or returns null
     * when the string is empty or null.
     * 
     * @param text   The text we want to format.
     * @param string whether to add quotations around the String or not.
     */
    public static String formatStringForQuery(String text, boolean string) {
        if (text == null || text.equals(""))
            return "null";

        return !string ? text : String.format("'%s'", text);
    }

    /** Takes a LocalDate and returns a String ready to be used in an SQL query. */
    public static String formatDateForQuery(LocalDate date) {
        return date == null ? null : String.format("'%s'", date.toString());
    }
}
