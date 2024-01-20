package com.ecohub;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RecordItemController {

  private RecordController recordController;
  
  public void getRecordController(RecordController recordController) {
    this.recordController = recordController;
  }

  @FXML
  public Label recordId;

  @FXML
  public Label title;

  @FXML
  public Label subcategory;

  @FXML
  public Label date;

  @FXML
  public Label value;

  @FXML
  public Label footprint;

  @FXML
  public Button editButton;

  @FXML
  public Button deleteButton;

  // function for handleDelete
  public void onDelete() {
    showDeleteDialog();
  }

  // function for handleEdit
  public void onEdit() {}

  // private void showEditDialog() {
  //   try {
  //     // Load the AlertInfo.fxml content
  //     FXMLLoader loader = new FXMLLoader();
  //     loader.setLocation(
  //       getClass().getResource("EditRecord.fxml")
  //     ); // Make sure to replace with your actual path
  //     VBox alertInfoRoot = loader.load();

  //     // Create a new stage for the alert
  //     Stage alertStage = new Stage();
  //     alertStage.setTitle("Alert Information");
  //     alertStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
  //     alertStage.setResizable(false);

  //     // Set the loaded content as the scene
  //     Scene alertScene = new Scene(alertInfoRoot);
  //     alertStage.setScene(alertScene);

  //     // Show the alert stage
  //     alertStage.showAndWait();
  //   } catch (IOException e) {
  //     e.printStackTrace();
  //   }
  // }

  private void showDeleteDialog() {
    try {
      // Load the AlertInfo.fxml content
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("DeleteRecord.fxml")); // Make sure to replace with your actual path
      VBox alertInfoRoot = loader.load();

      // Create a new stage for the alert
      Stage alertStage = new Stage();
      alertStage.setTitle("Alert Information");
      alertStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
      alertStage.setResizable(false);

      // Access the controller of the popup
      DeleteRecordController controller = loader.getController();

      controller.getRecordItemController(this);

      controller.getRecordController(recordController);

      // Set the loaded content as the scene
      Scene alertScene = new Scene(alertInfoRoot);
      alertStage.setScene(alertScene);

      // Show the alert stage
      alertStage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
