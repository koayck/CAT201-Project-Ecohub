package com.ecohub.models;

// generate data model for this user based ont he following sql table statement
 //   CREATE TABLE USER (
  //   user_id int NOT NULL AUTO_INCREMENT,
  //   user_name varchar(45) NOT NULL,
  //   email varchar(45) NOT NULL,
  //   password varchar(45) NOT NULL,
  //   PRIMARY KEY (id),
  //   UNIQUE KEY username_UNIQUE (username)
  // ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

public class User {
  private int user_id;
  private String user_name;
  private String email;

  public User(int user_id, String user_name, String email) {
    this.user_id = user_id;
    this.user_name = user_name;
    this.email = email;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
