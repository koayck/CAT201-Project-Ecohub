package com.ecohub.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

  private static final String DATABASE_NAME = "ecohub";
  private static final String DATABASE_USERNAME = "<database username here>";
  private static final String DATABASE_PASSWORD = "<database password here>";
  private static final String DATABASE_URL = "<database url here>" + DATABASE_NAME
      + "?useSSL=false&serverTimezone=UTC";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
  }
}
