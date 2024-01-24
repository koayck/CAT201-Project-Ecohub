package com.ecohub.controller;

import com.ecohub.dao.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
  private Button submitButton;

  @FXML
  public void register(ActionEvent event) throws SQLException {
    Window owner = submitButton.getScene().getWindow();

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
    // check if email is valid
    // explain what this function do
    if (
      !emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    ) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Please enter a valid email"
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
    if (
      !passwordField
        .getText()
        .matches(
          "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        )
    ) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Password must be at least 8 characters long, contain at least 1 special character, 1 number, 1 uppercase letter, 1 lowercase letter"
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

    // check if password consists of at least 8 characters, 1 special character, 1 number, 1 uppercase letter, 1 lowercase letter

    String username = usernameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();

    UserDAO userDao = new UserDAO();

    // check if username already exists, if so, show error
    if (userDao.checkUsername(username)) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Username already exists"
      );

      // clear field
      usernameField.setText("");
      emailField.setText("");
      passwordField.setText("");
      confirmPasswordField.setText("");
      return;
    }

    userDao.addRecord(username, email, password);

    showAlert(
      Alert.AlertType.CONFIRMATION,
      owner,
      "Sign Up Successful!",
      "Hello " + usernameField.getText() + ", you have successfully signed up."
    );

    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/com/ecohub/fxml/Login.fxml")
      );
      Parent root = loader.load();

      Scene scene = submitButton.getScene();
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

  @FXML
  void switchToLogin() throws IOException {
    Parent root = FXMLLoader.load(
      getClass().getResource("/com/ecohub/fxml/Login.fxml")
    );
    Scene scene = usernameField.getScene();
    scene.setRoot(root);
  }
}
