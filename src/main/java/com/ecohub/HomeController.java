package com.ecohub;

import com.ecohub.models.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;


public class HomeController implements Initializable {

  @FXML // Menu (Left Box)
  private VBox sidebar;

  @FXML // Menu items
  private HBox navHome, navTitle1, navTitle2, navTitle3, navTitle4;

  @FXML
  private Label title;

  @FXML // Parent of holder pane (Right pane)
  private StackPane rightPane;

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
  private Label label1;

  @FXML
  private Label label2;

  @FXML
  private Label label3;

  @FXML
  private Label label4;

  @FXML
  private Label label5;

  // @Override
  public void initialize(URL url, ResourceBundle rb) {
    styleBox(0); // for changing the color of Home Icon
    Image image = new Image(
      getClass().getResource("img/home-banner.png").toExternalForm()
    );
    imageView.setImage(image);
  }

  private void setNode(Node node) {
    homePane.setVisible(false);
    holderPane.getChildren().clear();
    holderPane.getChildren().add((Node) node);
  }

  public void initUser(User user) {
    title.setText("Welcome Back, " + user.getUser_name());
  }

  @FXML
  private void expandSidebar() {
    sidebar.setPrefWidth((sidebar.getWidth() == 50) ? 250 : 50);
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
  private void onTitle1() {
    styleBox(1);
    try {
      ItemPane = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  @FXML
  private void onTitle2() {
    styleBox(2);
    try {
      ItemPane = FXMLLoader.load(getClass().getResource("Record.fxml"));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  @FXML
  private void onTitle3() {
    styleBox(3);
    try {
      ItemPane = FXMLLoader.load(getClass().getResource("dailyInput.fxml"));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  @FXML
  private void onTitle4() {
    styleBox(4);
    try {
      ItemPane = FXMLLoader.load(getClass().getResource("calculation.fxml"));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  @FXML
  private void onAbout() {
    System.out.println("About Clicked !");

    
  }

  private void styleBox(int index) {
    // This function change the style+color of the menu (Menu Item Selected)
    ((SVGPath) navHome.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((SVGPath) navTitle1.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((SVGPath) navTitle2.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((SVGPath) navTitle3.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((SVGPath) navTitle4.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );

    navHome.setStyle("-fx-border: 0");
    navTitle1.setStyle("-fx-border: 0");
    navTitle2.setStyle("-fx-border: 0");
    navTitle3.setStyle("-fx-border: 0");
    navTitle4.setStyle("-fx-border: 0");

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
        navTitle1.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #00daa0;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((SVGPath) navTitle1.getChildren().get(0)).setFill(
            Paint.valueOf("#00daa0")
          );
        break;
      case 2:
        navTitle2.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #00daa0;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((SVGPath) navTitle2.getChildren().get(0)).setFill(
            Paint.valueOf("#00daa0")
          );
        break;
      case 3:
        navTitle3.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #00daa0;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((SVGPath) navTitle3.getChildren().get(0)).setFill(
            Paint.valueOf("#00daa0")
          );
        break;
      case 4:
        navTitle4.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #00daa0;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((SVGPath) navTitle4.getChildren().get(0)).setFill(
            Paint.valueOf("#00daa0")
          );
        break;
    }
  }
}
