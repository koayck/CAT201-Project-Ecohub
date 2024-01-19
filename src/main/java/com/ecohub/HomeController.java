package com.ecohub;

import com.ecohub.dao.UserDAO;
import com.ecohub.models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

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
  private ImageView imgSlider;

  // counter Number of image using in slider
  private final byte NUMBER_IMAGE_SLIDER = 3;
  private int counter = 1;

  // Example of right pane
  private Parent ItemPane;

  // @Override
  public void initialize(URL url, ResourceBundle rb) {
    styleBox(0); // for changing the color of Home Icon
    // Initialize the image of slider (to fill parent)
    // imgSlider.fitWidthProperty().bind(holderPane.widthProperty());
    // imgSlider.fitHeightProperty().bind(holderPane.heightProperty());
    // sliderAutoChangePictures(); // For auto change picture of slider
  }

  private void setNode(Node node) {
    homePane.setVisible(false);
    holderPane.getChildren().clear();
    holderPane.getChildren().add((Node) node);
    sidebar.setPrefWidth(50);
    FadeTransition ft = new FadeTransition(Duration.millis(1000));
    ft.setNode(node);
    ft.setFromValue(0.1);
    ft.setToValue(1);
    ft.setCycleCount(1);
    ft.setAutoReverse(false);
    ft.play();
  }

  private User user;

  public void initUser(User user) {
    title.setText("Welcome Back, " + user.getUser_name());
    this.user = user;
    // You can now use this user object in your HomeController
  }

  

  // private void sliderAutoChangePictures() {
  // // Make auto change the slider in duration

  // Timeline sliderTimer = new Timeline(new KeyFrame(Duration.ZERO, e -> {
  // FadeTransition ft = new FadeTransition();
  // ft.setNode(imgSlider);
  // ft.setDuration(new Duration(4000));
  // ft.setFromValue(1.0);
  // ft.setToValue(0.3);
  // ft.setCycleCount(0);
  // ft.setAutoReverse(true);
  // ft.play();
  // imgSlider.setImage(new
  // Image("com/houarizegai/fxtools/dashboard/model1/img/slider/" + counter +
  // ".png"));
  // if (++counter > NUMBER_IMAGE_SLIDER) {
  // counter = 1;
  // }
  // }),
  // new KeyFrame(Duration.seconds(4))
  // );
  // sliderTimer.setCycleCount(Animation.INDEFINITE);
  // sliderTimer.play();

  // }

  @FXML
  private void expandSidebar() {
    sidebar.setPrefWidth((sidebar.getWidth() == 50) ? 250 : 50);
  }

  @FXML
  private void onHome() {
    styleBox(0);
    homePane.setVisible(true);
    sidebar.setPrefWidth(50);
  }

  @FXML
  private void onTitle1() {
    styleBox(1);
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("dashboard.fxml")
      );
      Parent root = loader.load();

      DashboardController DashboardController = loader.getController();
      DashboardController.initUser(user);

      ItemPane = root;
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  @FXML
  private void onTitle2() {
    styleBox(2);
    try {
      ItemPane = FXMLLoader.load(getClass().getResource("record.fxml"));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    setNode(ItemPane);
  }

  @FXML
  private void onTitle3() {
    styleBox(3);
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("dailyInput.fxml")
      );
      Parent root = loader.load();

      dailyInputController dailyInputController = loader.getController();
      dailyInputController.initUser(user);

      ItemPane = root;
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
    ((FontAwesomeIconView) navHome.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((FontAwesomeIconView) navTitle1.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((FontAwesomeIconView) navTitle2.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((FontAwesomeIconView) navTitle3.getChildren().get(0)).setFill(
        Paint.valueOf("#4a4949")
      );
    ((FontAwesomeIconView) navTitle4.getChildren().get(0)).setFill(
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
          "-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((FontAwesomeIconView) navHome.getChildren().get(0)).setFill(
            Paint.valueOf("#2196f3")
          );
        break;
      case 1:
        navTitle1.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((FontAwesomeIconView) navTitle1.getChildren().get(0)).setFill(
            Paint.valueOf("#2196f3")
          );
        break;
      case 2:
        navTitle2.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((FontAwesomeIconView) navTitle2.getChildren().get(0)).setFill(
            Paint.valueOf("#2196f3")
          );
        break;
      case 3:
        navTitle3.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((FontAwesomeIconView) navTitle3.getChildren().get(0)).setFill(
            Paint.valueOf("#2196f3")
          );
        break;
      case 4:
        navTitle4.setStyle(
          "-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;"
        );
        ((FontAwesomeIconView) navTitle4.getChildren().get(0)).setFill(
            Paint.valueOf("#2196f3")
          );
        break;
    }
  }
}
