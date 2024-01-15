package com.ecohub;

import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class testdb {

    private static final String DATABASE_NAME = "ecohub";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "454545";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME
            + "?useSSL=false&serverTimezone=UTC";
    private static final String INSERT_QUERY = "INSERT INTO ecohub.result (activity, input) VALUES (?, ?)";

    // public static void main(String[] args) throws SQLException {
    public void insertRecord(String activity, String input) throws SQLException {
        // Convert the input string to a BigDecimal
        BigDecimal bdInput = new BigDecimal(input);

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, activity);
            // Set the BigDecimal value
            preparedStatement.setBigDecimal(2, bdInput);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public List<result> getAllRecords() throws SQLException {
        List<result> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ecohub.result")) {

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String activity = rs.getString("activity");
                // Get the input as a BigDecimal
                BigDecimal input = rs.getBigDecimal("input");
                result res = new result(activity, input);
                results.add(res);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return results;
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