  package com.ecohub.dao;

import com.ecohub.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
  private static final String INSERT_QUERY =
    "INSERT INTO ECOHUB.USER (U_NAME, U_EMAIL, U_PASSWORD) VALUES (?,?,?)";

  // function for adding record based on this query private static final String INSERT_QUERY =
  // "INSERT INTO ECOHUB.USER (U_NAME, U_EMAIL, U_PASSWORD) VALUES (?,?,?)";
  public void addRecord(String username, String email, String password)
    throws SQLException {
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        INSERT_QUERY
      )
    ) {
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, email);
      preparedStatement.setString(3, password);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // generate code to check if username and password match for login page, return user object if match
  public User checkLogin(String username, String password)
    throws SQLException {
    User user = null;
    // Step 1: Establishing a Connection and
    // try-with-resource statement will auto close the connection.
    try (
      Connection connection = DBUtil.getConnection();
      // Step 2:Create a statement using connection object
      PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT * FROM USER WHERE BINARY U_NAME = ? AND BINARY U_PASSWORD = ?"
      )
    ) {
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);

      
      // Step 3: Execute the query or update query
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        user =
          new User(
            resultSet.getInt("U_ID"),
            resultSet.getString("U_NAME"),
            resultSet.getString("U_EMAIL")
          );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  //  check if username already exists in database, return true if exists
  public boolean checkUsername(String username) throws SQLException {
    boolean userExists = false;
    // Step 1: Establishing a Connection and
    // try-with-resource statement will auto close the connection.
    try (
      Connection connection = DBUtil.getConnection();
      // Step 2:Create a statement using connection object
      PreparedStatement preparedStatement = connection.prepareStatement(
        "SELECT * FROM USER WHERE BINARY U_NAME = ?"
      )
    ) {
      preparedStatement.setString(1, username);

      
      // Step 3: Execute the query or update query
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        userExists = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return userExists;
  }
}
