package com.ecohub;

import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class resultdb {

    private static final String DATABASE_NAME = "ecohub";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "454545";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME
            + "?useSSL=false&serverTimezone=UTC";
    private static final String INSERT_QUERY = "INSERT INTO ecohub.result (category,activity, input) VALUES (?,?,?)";

    // public static void main(String[] args) throws SQLException {
    public void insertRecord(String category, String activity, String input) throws SQLException {
        // Convert the input string to a BigDecimal
        BigDecimal bdInput = new BigDecimal(input);

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, category);
            preparedStatement.setString(2, activity);
            // Set the BigDecimal value
            preparedStatement.setBigDecimal(3, bdInput);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public List<result> getAllRecords(List<String> excludedCategories) throws SQLException {
        List<result> results = new ArrayList<>();

        String query = "SELECT * FROM ecohub.result";
        if (!excludedCategories.isEmpty()) {
            query += " WHERE category NOT IN (" + String.join(", ", Collections.nCopies(excludedCategories.size(), "?"))
                    + ")";
        }

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < excludedCategories.size(); i++) {
                preparedStatement.setString(i + 1, excludedCategories.get(i));
            }

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String category = rs.getString("category");
                String activity = rs.getString("activity");
                // Get the input as a BigDecimal
                BigDecimal input = rs.getBigDecimal("input");
                result res = new result(category, activity, input);
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