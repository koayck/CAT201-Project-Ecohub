package com.ecohub;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label errorLabel;

  @FXML
  void login() throws IOException {
    String username = usernameField.getText();
    String password = passwordField.getText();

    if (username.equals(password)) {
      Stage primaryStage = new Stage();
      Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
    } else {
      errorLabel.setText("Invalid username or password");
    }
  }
}
