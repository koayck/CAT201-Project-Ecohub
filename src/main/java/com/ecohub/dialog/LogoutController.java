package com.ecohub.dialog;

import com.ecohub.session.UserSession;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LogoutController {

  private Stage homepageStage;

  public void setHomepageStage(Stage homepageStage) {
    this.homepageStage = homepageStage;
  }

  @FXML
  private Label Msg_Label1;

  @FXML
  private void handleLogout(ActionEvent event) {
    try {
      // Load the Login.fxml content
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/com/ecohub/fxml/Login.fxml")); // Make sure to replace with your actual path
      VBox loginRoot = loader.load();

      // Create a new stage for the login
      Stage loginStage = new Stage();
      loginStage.setTitle("EcoHub");
      loginStage.setResizable(false);

      // Set the loaded content as the scene
      Scene loginScene = new Scene(loginRoot, 1280, 720);
      loginStage.setScene(loginScene);

      // Show the login stage
      loginStage.show();

      // Close the current (logout) stage
      ((Stage) (((Node) (event.getSource())).getScene().getWindow())).close();

      // Close the homepage stage
      homepageStage.close();

      // Show the alert
      showAlert(
        Alert.AlertType.CONFIRMATION,
        loginStage,
        "Logout Successful!",
        "Till we meet again, " + UserSession.getInstance().getUsername()
      );
      UserSession.getInstance().cleanUserSession();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void showAlert(
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
  private void handleCancel(ActionEvent event) {
    // Get the current stage
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    // Close the current stage
    stage.close();
  }
}
