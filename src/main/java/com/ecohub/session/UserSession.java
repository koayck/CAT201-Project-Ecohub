package com.ecohub.session;

public class UserSession {
  private static UserSession instance;

  private int userId;
  private String username;

  private UserSession(int userId, String username) {
    this.userId = userId;
    this.username = username;
  }

  public static UserSession getInstance(int userId, String username) {
    if(instance == null) {
      instance = new UserSession(userId, username);
    }
    return instance;
  }

  public static UserSession getInstance() {
    if(instance == null) {
      throw new RuntimeException("Call getInstance(int userId) before calling getInstance()");
    }
    return instance;
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public void cleanUserSession() {
    userId = 0; // or null
    instance = null;
  }
}