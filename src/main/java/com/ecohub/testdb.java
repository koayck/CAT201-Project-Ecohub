package com.ecohub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class testdb {

    private static final String DATABASE_NAME = "ecohub";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "454545";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME
            + "?useSSL=false&serverTimezone=UTC";
    private static final String INSERT_QUERY = "INSERT INTO ecohub.result (activity, input) VALUES (?, ?)";

    // public static void main(String[] args) throws SQLException {
    public void insertRecord(String activity, String input) throws SQLException {
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, activity);
            preparedStatement.setString(2, input);

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}