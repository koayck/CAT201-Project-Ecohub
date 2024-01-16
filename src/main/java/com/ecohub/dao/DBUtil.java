package com.ecohub.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

  private static final String DATABASE_NAME = "ecohub";
  private static final String DATABASE_USERNAME = "root";
  private static final String DATABASE_PASSWORD = "73812";
  private static final String DATABASE_URL =
    "jdbc:mysql://localhost:3306/" +
    DATABASE_NAME +
    "?useSSL=false&serverTimezone=UTC";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
      DATABASE_URL,
      DATABASE_USERNAME,
      DATABASE_PASSWORD
    );
  }
}
