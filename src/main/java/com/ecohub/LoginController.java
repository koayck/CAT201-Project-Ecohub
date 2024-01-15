package com.ecohub;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label errorLabel;

  @FXML
  private Button submitButton;
  
  @FXML
  void login() throws SQLException {
    Window owner = submitButton.getScene().getWindow();

    String username = usernameField.getText();
    String password = passwordField.getText();

    DatabaseConnection jdbcDao = new DatabaseConnection();
    
    // checkLogin return true or false
    // check if true, proceed to next page, if false, show error
    if (!jdbcDao.checkLogin(username, password)) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Username or password is incorrect"
      );
      return;
    }

    showAlert(
      Alert.AlertType.CONFIRMATION,
      owner,
      "Login Successful!",
      "Welcome " + usernameField.getText()
    );

    try {
      Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
      Scene scene = usernameField.getScene();
      scene.setRoot(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void showAlert(
    Alert.AlertType alertType,
    Window owner,
    String title,
    String message
  ) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initOwner(owner);
    alert.show();
  }

  // transition to signup page instead of new window
  @FXML
  void switchToSignUp() throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
    Scene scene = usernameField.getScene();
    scene.setRoot(root);
  }
}