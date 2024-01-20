package com.ecohub.session;

public class UserSession {
  private static UserSession instance;

  private int userId;

  private UserSession(int userId) {
    this.userId = userId;
  }

  public static UserSession getInstance(int userId) {
    if(instance == null) {
      instance = new UserSession(userId);
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

  public void cleanUserSession() {
    userId = 0; // or null
    instance = null;
  }
}