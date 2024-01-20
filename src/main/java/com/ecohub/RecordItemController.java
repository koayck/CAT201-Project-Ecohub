package com.ecohub;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RecordItemController {

  private RecordController recordController;

  public void getRecordController(RecordController recordController) {
    this.recordController = recordController;
  }

  @FXML
  public SVGPath categoryIcon;

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
  public void onEdit() {
    showEditDialog();
  }

  private void showEditDialog() {
    
    try {
      // Load the AlertInfo.fxml content
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("AddRecord.fxml")); // Make sure to replace with your actual path

      VBox alertInfoRoot = loader.load();

      // Get the controller
      AddRecordController controller = loader.getController();

      controller.initDialogTitle("Update record");

      controller.getRecordController(recordController);

      // Call the method to initialize the recordId
      controller.initRecordId(Integer.parseInt(recordId.getText()));
      controller.initSubmitBtn("Update");

      // Create a new stage for the alert
      Stage alertStage = new Stage();
      alertStage.setTitle("Edit Record");
      alertStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
      alertStage.setResizable(false);

      // Set the loaded content as the scene
      Scene alertScene = new Scene(alertInfoRoot);
      alertStage.setScene(alertScene);

      // Show the alert stage
      alertStage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void showDeleteDialog() {
    try {
      // Load the AlertInfo.fxml content
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("DeleteRecord.fxml")); // Make sure to replace with your actual path
      VBox alertInfoRoot = loader.load();

      // Create a new stage for the alert
      Stage alertStage = new Stage();
      alertStage.setTitle("Delete Record");
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
