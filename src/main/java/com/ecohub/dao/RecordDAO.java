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
    "INSERT INTO ECOHUB.RECORD (R_TITLE, R_VALUE, S_ID, U_ID, R_DATE) VALUES (?,?,?,?,NOW())";
  private static final String DELETE_QUERY =
    "DELETE FROM ECOHUB.RECORD WHERE R_ID = ?";
  private static final String SELECT_ALL_QUERY =
    "SELECT * FROM ECOHUB.RECORD NATURAL JOIN ECOHUB.SUBCATEGORY NATURAL JOIN ECOHUB.CATEGORY WHERE U_ID = ?";
  private static final String UPDATE_QUERY =
    "UPDATE ECOHUB.RECORD SET R_TITLE = ?, R_VALUE = ?, S_ID = ?  WHERE R_ID = ?";
  private static final String GET_RECENT =
    "SELECT DATE(`R_DATE`) AS `Date`, SUM(`R_CARBON`) AS `Total` FROM `record` WHERE `U_ID` = ? AND `R_DATE` >= CURRENT_DATE - INTERVAL 6 DAY GROUP BY DATE(`R_DATE`)";
  private static final String GET_RECENT_YEAR =
    "SELECT YEAR(`R_DATE`) AS `Year`, MONTH(`R_DATE`) AS `Month`, SUM(`R_CARBON`) AS `Total` FROM `record` WHERE `U_ID` = ? AND `R_DATE` >= DATE_SUB(CURRENT_DATE, INTERVAL 12 MONTH) GROUP BY YEAR(`R_DATE`), MONTH(`R_DATE`)";
  private static final String GET_TOTAL =
    "SELECT SUM(`R_CARBON`) AS `Total` FROM `record` WHERE `U_ID` = ?";
  private static final String GET_PERCENTAGE =
    "SELECT c.C_NAME AS Category, SUM(r.R_CARBON) AS TotalCarbon, (SUM(r.R_CARBON) / (SELECT SUM(R_CARBON) FROM record WHERE U_ID = ?)) * 100 AS Percentage FROM record r JOIN subcategory s ON r.S_ID = s.S_ID JOIN ecohub.category c ON s.C_ID = c.C_ID WHERE r.U_ID = ? GROUP BY c.C_NAME;";
  private static final String GET_CATEGORY =
    "SELECT c.C_NAME AS Category, SUM(r.R_VALUE) AS Total FROM record r JOIN subcategory s ON r.S_ID = s.S_ID JOIN ecohub.category c ON s.C_ID = c.C_ID WHERE r.U_ID = ? AND c.C_ID = ? GROUP BY c.C_NAME;";
  private static final String SELECT_QUERY =
    "SELECT * FROM ECOHUB.RECORD NATURAL JOIN ECOHUB.SUBCATEGORY NATURAL JOIN ECOHUB.CATEGORY WHERE R_ID = ?";
  private static final String SELECT_FILTER_ACT_QUERY =
    "SELECT * FROM ECOHUB.RECORD JOIN SUBCATEGORY ON ECOHUB.RECORD.S_ID = SUBCATEGORY.S_ID WHERE U_ID = ? AND S_NAME = ?";
  private static final String SELECT_FILTER_CAT_QUERY =
    "SELECT * FROM ECOHUB.RECORD JOIN SUBCATEGORY ON ECOHUB.RECORD.S_ID = SUBCATEGORY.S_ID  JOIN CATEGORY ON SUBCATEGORY.C_ID = CATEGORY.C_ID WHERE U_ID = ? AND C_NAME = ?";
    private static final String SELECT_KEYWORD = "SELECT * FROM ECOHUB.RECORD NATURAL JOIN ECOHUB.SUBCATEGORY NATURAL JOIN ECOHUB.CATEGORY WHERE U_ID = ? AND R_TITLE LIKE ?";

    // function for adding record based on this query private static final String
  // INSERT_QUERY =
  // "INSERT INTO ECOHUB.RECORD (R.CATEGORY, R.TITLE, R.VALUE) VALUES (?,?,?)";
  public void addRecord(String title, String value, int subCategoryId, int uid)
    throws SQLException {
    // Convert the value string to a BigDecimal
    BigDecimal bdValue = new BigDecimal(value);

    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        INSERT_QUERY
      )
    ) {
      preparedStatement.setString(1, title);
      preparedStatement.setBigDecimal(2, bdValue);
      preparedStatement.setInt(3, subCategoryId);
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
    } else if (
      (
        "Drinking".equals(title) ||
        "Bathing".equals(title) ||
        "Washing".equals(title)
      )
    ) {
      value = input.multiply(BigDecimal.valueOf(0.298));
    } else {
      value = input.multiply(BigDecimal.valueOf(0));
    }

    value = value.setScale(8, RoundingMode.HALF_UP);
    return value;
  }

  // calculate percentage for each activity
  public BigDecimal calculatePercentage(
    String activity,
    BigDecimal input,
    BigDecimal total
  ) {
    BigDecimal value = calculateValue(activity, input);
    BigDecimal percentage = value
      .divide(total, 2, RoundingMode.HALF_UP)
      .multiply(BigDecimal.valueOf(100));
    return percentage;
  }

  // function for deleting record based on this query private static final String
  // DELETE_QUERY =
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

  // function for selecting all record based on this query private static final
  // String SELECT_QUERY =
  // "SELECT * FROM ECOHUB.RECORD WHERE U_ID = ?";
  public List<Record> getAllRecords(int id, String keyword) throws SQLException {
    String query = (keyword == null || keyword.isEmpty()) ? SELECT_ALL_QUERY : SELECT_KEYWORD;

    List<Record> recordList = new ArrayList<>();
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        query
      )
    ) {
      preparedStatement.setInt(1, id);
      if (keyword != null && !keyword.isEmpty())
        preparedStatement.setString(2, keyword + "%");

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        // add each record to the list

        Record record = new Record(
          resultSet.getString("R_TITLE"),
          resultSet.getString("C_NAME"),
          resultSet.getString("S_NAME"),
          resultSet.getBigDecimal("R_VALUE"),
          resultSet.getDate("R_DATE"),
          resultSet.getBigDecimal("R_CARBON"),
          resultSet.getInt("R_ID")
        );
        
        recordList.add(record);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recordList;
  }

  public List<Record> getFilteredRecords(int id, String filter, int flag)
    throws SQLException {
    List<Record> recordList = new ArrayList<>();
    if (flag == 1) {
      try (
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
          SELECT_FILTER_CAT_QUERY
        )
      ) {
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, filter);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          // add each record to the list

          Record record = new Record(
            resultSet.getString("R_TITLE"),
            resultSet.getString("C_NAME"),
            resultSet.getString("S_NAME"),
            resultSet.getBigDecimal("R_VALUE"),
            resultSet.getDate("R_DATE"),
            resultSet.getBigDecimal("R_CARBON"),
            resultSet.getInt("R_ID")
          );
          
          recordList.add(record);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    if (flag == 2) {
      try (
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
          SELECT_FILTER_ACT_QUERY
        )
      ) {
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, filter);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
          // add each record to the list

          Record record = new Record(
            resultSet.getString("R_TITLE"),
            resultSet.getString("C_NAME"),
            resultSet.getString("S_NAME"),
            resultSet.getBigDecimal("R_VALUE"),
            resultSet.getDate("R_DATE"),
            resultSet.getBigDecimal("R_CARBON"),
            resultSet.getInt("R_ID")
          );
          
          recordList.add(record);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return recordList;
  }

  // function to get specific record based on the record id
  public Record getRecord(int id) throws SQLException {
    Record record = null;
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        SELECT_QUERY
      )
    ) {
      preparedStatement.setInt(1, id);

      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        record =
          new Record(
            resultSet.getString("R_TITLE"),
            resultSet.getString("C_NAME"),
            resultSet.getString("S_NAME"),
            resultSet.getBigDecimal("R_VALUE"),
            resultSet.getDate("R_DATE"),
            resultSet.getBigDecimal("R_CARBON"),
            resultSet.getInt("R_ID")
          );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return record;
  }

  // function for updating record based on this query private static final String
  // UPDATE_QUERY =
  // "UPDATE ECOHUB.RECORD SET R.CATEGORY = ?, R.TITLE = ?, R.VALUE = ? WHERE R_ID
  // = ?";
  public void updateRecord(String title, String value, int subCategoryId, int recordId)
    throws SQLException {
    // Convert the value string to a BigDecimal
    BigDecimal bdValue = new BigDecimal(value);

    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        UPDATE_QUERY
      )
    ) {
      preparedStatement.setString(1, title);
      preparedStatement.setBigDecimal(2, bdValue);
      preparedStatement.setInt(3, subCategoryId);
      preparedStatement.setInt(4, recordId);

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
        GET_RECENT
      )
    ) {
      // assuming you're setting the id somewhere
      preparedStatement.setInt(1, id);

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Date date = resultSet.getDate("Date");
        String dateString = formatter.format(date);
        double total = resultSet.getDouble("Total");
        recordList.add(new String[] { dateString, Double.toString(total) });
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recordList.toArray(new String[0][]);
  }

  public String[][] getRecentYear(int id) throws SQLException {
    List<String[]> recordList = new ArrayList<>();
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        GET_RECENT_YEAR
      )
    ) {
      preparedStatement.setInt(1, id);

      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int year = resultSet.getInt("Year");
        int month = resultSet.getInt("Month");
        double total = resultSet.getDouble("Total");
        recordList.add(
          new String[] { year + "-" + month, Double.toString(total) }
        );
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return recordList.toArray(new String[0][]);
  }

  public BigDecimal getTotal(int id) throws SQLException {
    BigDecimal total = null;
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        GET_TOTAL
      )
    ) {
      // assuming you're setting the id somewhere
      preparedStatement.setInt(1, id);

      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        BigDecimal bd = resultSet.getBigDecimal("Total");
        if (bd != null) {
          total = bd.setScale(2, RoundingMode.HALF_UP);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return total;
  }

  public BigDecimal getCategory(int userId, int categoryId)
    throws SQLException {
    BigDecimal total = null;
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        GET_CATEGORY
      )
    ) {
      // assuming you're setting the id somewhere
      preparedStatement.setInt(1, userId);
      preparedStatement.setInt(2, categoryId);

      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        BigDecimal bd = resultSet.getBigDecimal("Total");
        if (bd != null) {
          total = bd.setScale(2, RoundingMode.HALF_UP);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return total;
  }

  public List<String[]> getPercentage(int id) throws SQLException {
    List<String[]> percentages = new ArrayList<>();
    try (
      Connection connection = DBUtil.getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(
        GET_PERCENTAGE
      )
    ) {
      preparedStatement.setInt(1, id);
      preparedStatement.setInt(2, id);

      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        String[] result = new String[2];
        result[0] = resultSet.getString("Category");
        result[1] = resultSet.getString("Percentage");
        percentages.add(result);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return percentages.isEmpty() ? null : percentages;
  }
}
