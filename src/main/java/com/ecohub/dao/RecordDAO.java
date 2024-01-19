package com.ecohub.dao;

import com.ecohub.models.Record;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
  private static final String GET_RECECNT = 
    "SELECT DATE(`R_DATE`) AS `Date`, SUM(`R_CARBON`) AS `Total` FROM `record` WHERE `U_ID` = ? AND `R_DATE` >= CURRENT_DATE - INTERVAL 6 DAY GROUP BY DATE(`R_DATE`)";



  // function for adding record based on this query private static final String
  // INSERT_QUERY =
  // "INSERT INTO ECOHUB.RECORD (R.CATEGORY, R.TITLE, R.VALUE) VALUES (?,?,?)";
  public void addRecord(String category, String title, String value, int uid) throws SQLException {
    // Convert the value string to a BigDecimal
    BigDecimal bdValue = new BigDecimal(value);

    try (Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
      preparedStatement.setString(1, category);
      preparedStatement.setString(2, title);
      preparedStatement.setBigDecimal(3, bdValue);
      preparedStatement.setInt(4, uid);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public BigDecimal calculateValue(String title, BigDecimal input) {
    BigDecimal value;

    if ("Car".equals(title)) {
      value = input.multiply(BigDecimal.valueOf(0.16443));
    } else if ("Walking".equals(title)) {
      value = input.multiply(BigDecimal.valueOf(0.039));
    } else if (("AC".equals(title) || "Refrigerator".equals(title))) {
      value = input.multiply(BigDecimal.valueOf(0.39));
    } else if ("Plastic".equals(title)) {
      value = input.multiply(BigDecimal.valueOf(6));
    } else if ("Electronic".equals(title)) {
      value = input.multiply(BigDecimal.valueOf(2));
    } else if (("Drinking".equals(title) || "Bathing".equals(title) || "Washing".equals(title))) {
      value = input.multiply(BigDecimal.valueOf(0.298));
    } else {
      value = input.multiply(BigDecimal.valueOf(0));
    }

    value = value.setScale(8, RoundingMode.HALF_UP);
    return value;
  }

  // calculate percentage for each activity
  public BigDecimal calculatePercentage(String activity, BigDecimal input, BigDecimal total) {
    BigDecimal value = calculateValue(activity, input);
    BigDecimal percentage = value.divide(total, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    return percentage;
  }

  // function for deleting record based on this query private static final String
  // DELETE_QUERY =
  // "DELETE FROM ECOHUB.RECORD WHERE R_ID = ?";
  public void deleteRecord(int id) throws SQLException {
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // function for selecting all record based on this query private static final
  // String SELECT_QUERY =
  // "SELECT * FROM ECOHUB.RECORD WHERE U_ID = ?";
  public List<Record> getAllRecords(int id) throws SQLException {
    List<Record> recordList = new ArrayList<>();
    try (Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY)) {
      preparedStatement.setInt(1, id);

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        // add each record to the list

        Record record = new Record(resultSet.getString("R_CATEGORY"), resultSet.getString("R_TITLE"),
            resultSet.getBigDecimal("R_VALUE"));
        recordList.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recordList;
  }

  // function for updating record based on this query private static final String
  // UPDATE_QUERY =
  // "UPDATE ECOHUB.RECORD SET R.CATEGORY = ?, R.TITLE = ?, R.VALUE = ? WHERE R_ID
  // = ?";
  public void updateRecord(String category, String title, String value, int id) throws SQLException {
    // Convert the value string to a BigDecimal
    BigDecimal bdValue = new BigDecimal(value);

    try (Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
      preparedStatement.setString(1, category);
      preparedStatement.setString(2, title);
      preparedStatement.setBigDecimal(3, bdValue);
      preparedStatement.setInt(4, id);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public String[][] getRecent(int id) throws SQLException {
    List<String[]> recordList = new ArrayList<>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        GET_RECECNT
      )
    ) {
      // assuming you're setting the id somewhere
      preparedStatement.setInt(1, id);
      
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Date date = resultSet.getDate("Date");
        String dateString = formatter.format(date);
        double total = resultSet.getDouble("Total");
        recordList.add(new String[]{dateString, Double.toString(total)});
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recordList.toArray(new String[0][]);
  }
}
