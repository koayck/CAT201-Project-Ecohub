package com.ecohub;

import java.io.IOException;
import java.sql.SQLException;

import com.ecohub.dao.UserDAO;

import javafx.event.ActionEvent;
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

public class SignUpController {

  @FXML
  private TextField usernameField;

  @FXML
  private TextField emailField;

  @FXML
  private PasswordField passwordField;
  
  @FXML
  private PasswordField confirmPasswordField;

  @FXML
  private Label errorLabel;

  @FXML
  private Button submitButton;

  @FXML
  public void register(ActionEvent event) throws SQLException {
    Window owner = submitButton.getScene().getWindow();

    System.out.println(usernameField.getText());
    System.out.println(emailField.getText());
    System.out.println(passwordField.getText());
    System.out.println(confirmPasswordField.getText());
    if (usernameField.getText().isEmpty()) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Please enter your name"
      );
      return;
    }

    if (emailField.getText().isEmpty()) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Please enter your email"
      );
      return;
    }
    if (passwordField.getText().isEmpty()) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Please enter a password"
      );
      return;
    }
    if (confirmPasswordField.getText().isEmpty()) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Please confirm your password"
      );
      return;
    }
    if (!passwordField.getText().equals(confirmPasswordField.getText())) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Passwords do not match"
      );
      return;
    }

    String username = usernameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();

    UserDAO userDao = new UserDAO();
    userDao.addRecord(username, email, password);

    showAlert(
      Alert.AlertType.CONFIRMATION,
      owner,
      "Registration Successful!",
      "Welcome " + usernameField.getText()
    );
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

  @FXML
  void switchToLogin() throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
    Scene scene = usernameField.getScene();
    scene.setRoot(root);
  }
}