package com.ecohub.dao;

import com.ecohub.models.Record;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecordDAO {

  private static final String INSERT_QUERY =
    "INSERT INTO ECOHUB.RECORD (R_CATEGORY, R_TITLE, R_VALUE, U_ID) VALUES (?,?,?,?)";
  private static final String DELETE_QUERY =
    "DELETE FROM ECOHUB.RECORD WHERE R_ID = ?";
  private static final String SELECT_ALL_QUERY =
    "SELECT * FROM ECOHUB.RECORD WHERE U_ID = ?";
  private static final String UPDATE_QUERY =
    "UPDATE ECOHUB.RECORD SET R_CATEGORY = ?, R_TITLE = ?, R_VALUE = ? WHERE R_ID = ?";


  // function for adding record based on this query private static final String INSERT_QUERY =
  // "INSERT INTO ECOHUB.RECORD (R.CATEGORY, R.TITLE, R.VALUE) VALUES (?,?,?)";
  public void addRecord(String category, String title, String value, int uid)
    throws SQLException {
    // Convert the value string to a BigDecimal
    BigDecimal bdValue = new BigDecimal(value);

    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        INSERT_QUERY
      )
    ) {
      preparedStatement.setString(1, category);
      preparedStatement.setString(2, title);
      preparedStatement.setBigDecimal(3, bdValue);
      preparedStatement.setInt(4, uid);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // function for deleting record based on this query private static final String DELETE_QUERY =
  // "DELETE FROM ECOHUB.RECORD WHERE R_ID = ?";
  public void deleteRecord(int id) throws SQLException {
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        DELETE_QUERY
      )
    ) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // function for selecting all record based on this query private static final String SELECT_QUERY =
  // "SELECT * FROM ECOHUB.RECORD WHERE U_ID = ?";
public List<Record> getAllRecords(int id) throws SQLException {
    List<Record> recordList = new ArrayList<>();
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        SELECT_ALL_QUERY
      )
    ) {
      preparedStatement.setInt(1, id);
      
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        // add each record to the list

        Record record = new Record(
          resultSet.getString("R_CATEGORY"),
          resultSet.getString("R_TITLE"),
          resultSet.getBigDecimal("R_VALUE")
        );
        recordList.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recordList;
  }

  // function for updating record based on this query private static final String UPDATE_QUERY =
  // "UPDATE ECOHUB.RECORD SET R.CATEGORY = ?, R.TITLE = ?, R.VALUE = ? WHERE R_ID = ?";
  public void updateRecord(String category, String title, String value, int id)
    throws SQLException {
    // Convert the value string to a BigDecimal
    BigDecimal bdValue = new BigDecimal(value);

    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        UPDATE_QUERY
      )
    ) {
      preparedStatement.setString(1, category);
      preparedStatement.setString(2, title);
      preparedStatement.setBigDecimal(3, bdValue);
      preparedStatement.setInt(4, id);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
