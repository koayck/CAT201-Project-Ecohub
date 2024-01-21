package com.ecohub.controller;

import com.ecohub.session.UserSession;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.Window;

public class HomeController implements Initializable {

  @FXML // Menu items
  private HBox navHome, navDashboard, navRecord;

  @FXML
  public Label title;

  @FXML // Right Pane
  private AnchorPane holderPane;

  @FXML
  private VBox homePane;

  @FXML // Image of Slider
  private ImageView imageView;

  // Example of right pane
  @FXML
  private Parent ItemPane;

  @FXML
  private Label navLogOut;

  // @Override
  public void initialize(URL url, ResourceBundle rb) {
    styleBox(0); // for changing the color of Home Icon
    Image image = new Image(
      getClass().getResource("/com/ecohub/img/home-banner.png").toExternalForm()
    );
    imageView.setImage(image);
  }

  private void setNode(Node node) {
    homePane.setVisible(false);
    holderPane.getChildren().clear();
    holderPane.getChildren().add((Node) node);
  }

  @FXML
  private void onHome() {
    homePane.setVisible(true);

    styleBox(0);

    // If ItemPane already exists, remove it from its parent
    if (ItemPane != null && ItemPane.getParent() != null) {
      ((Pane) ItemPane.getParent()).getChildren().remove(ItemPane);
    }
  }

  @FXML
  private void onDashboard() {
    styleBox(1);
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/com/ecohub/fxml/Dashboard.fxml")
      );
      Parent root = loader.load();

      ItemPane = root;
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  @FXML
  private void onRecord() {
    styleBox(2);
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ecohub/fxml/Record.fxml"));
      Parent root = loader.load();

      ItemPane = root;
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  // log out
  @FXML
  private void onLogOut() {
    Window owner = navLogOut.getScene().getWindow();
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ecohub/fxml/Login.fxml"));
      Parent root = loader.load();
      
      Scene scene = navLogOut.getScene();
      scene.setRoot(root);
      // once log out show a alert window
      showAlert(Alert.AlertType.CONFIRMATION, owner, "Logout Successful!", "Till we meet again, " + UserSession.getInstance().getUsername());
      UserSession.getInstance().cleanUserSession();
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

  private void styleBox(int index) {
    // This function change the style+color of the menu (Menu Item Selected)
    ((SVGPath) navHome.getChildren().get(0)).setFill(Paint.valueOf("#4a4949"));
    ((SVGPath) navDashboard.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((SVGPath) navRecord.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );

    navHome.setStyle("-fx-border: 0");
    navDashboard.setStyle("-fx-border: 0");
    navRecord.setStyle("-fx-border: 0");

    switch (index) {
      case 0:
        navHome.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #00daa0;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((SVGPath) navHome.getChildren().get(0)).setFill(
            Paint.valueOf("#00daa0")
          );
        break;
      case 1:
        navDashboard.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #00daa0;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((SVGPath) navDashboard.getChildren().get(0)).setFill(
            Paint.valueOf("#00daa0")
          );
        break;
      case 2:
        navRecord.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #00daa0;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((SVGPath) navRecord.getChildren().get(0)).setFill(
            Paint.valueOf("#00daa0")
          );
        break;
    }
  }
}
