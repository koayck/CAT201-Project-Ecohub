package com.ecohub.controller;

import com.ecohub.models.User;
import com.ecohub.session.UserSession;
import com.ecohub.dao.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Button submitButton;

  @FXML
  void login() throws SQLException {
    Window owner = submitButton.getScene().getWindow();

    String username = usernameField.getText();
    String password = passwordField.getText();

    UserDAO userDao = new UserDAO();

    User user = userDao.checkLogin(username, password);
        
    if (user == null) {
      showAlert(
        Alert.AlertType.ERROR,
        owner,
        "Form Error!",
        "Username or password is incorrect"
      );
      return;
    }

    // set user session
    UserSession.getInstance(user.getUser_id(), user.getUser_name());

    showAlert(
      Alert.AlertType.CONFIRMATION,
      owner,
      "Login Successful!",
      "Welcome " + usernameField.getText()
    );

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ecohub/fxml/Home.fxml"));
      Parent root = loader.load();

      HomeController homeController = loader.getController();
      homeController.title.setText(
        "Welcome Back, " + UserSession.getInstance().getUsername()
      );
      
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
    Parent root = FXMLLoader.load(getClass().getResource("/com/ecohub/fxml/SignUp.fxml"));
    Scene scene = usernameField.getScene();
    scene.setRoot(root);
  }
}
