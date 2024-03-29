package com.ecohub.controller;

import com.ecohub.dao.RecordDAO;
import com.ecohub.dialog.AlertInfoController;
import com.ecohub.models.Record;
import com.ecohub.session.UserSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddRecordController {

  private RecordController recordController;

  public void getRecordController(RecordController recordController) {
    this.recordController = recordController;
  }

  private int recordId;

  public void initRecordId(int recordId) {
    this.recordId = recordId;
    initFields();
  }

  // Declare subCategoryId as an instance variable
  private Integer subCategoryId;

  @FXML
  private Label dialogTitle;

  public void initDialogTitle(String title) {
    dialogTitle.setText(title);
  }

  @FXML
  private TextField titleField;

  @FXML
  private ComboBox<String> categoryField;

  @FXML
  private ComboBox<String> subCategoryField;

  @FXML
  private TextField inputField;

  @FXML
  private Button submitBtn;

  @FXML
  private Label inputFieldLabel;

  @FXML
  public void initSubmitBtn(String title) {
    submitBtn.setText(title);
  }

  @FXML
  Button cancelBtn;

  @FXML
  private void handleSubmit(ActionEvent event) {
    try {
      // Check if fields are not empty
      if (!titleField.getText().isEmpty() && !inputField.getText().isEmpty()) {
        String title = titleField.getText();
        String value = inputField.getText();
        RecordDAO recordDao = new RecordDAO();
        if (recordId != 0) {
          recordDao.updateRecord(title, value, subCategoryId, recordId);
        } else {
          recordDao.addRecord(
            title,
            value,
            subCategoryId,
            UserSession.getInstance().getUserId()
          );
        }
      }

      // Clear the fields
      titleField.clear();
      categoryField.valueProperty().set(null);
      subCategoryField.valueProperty().set(null);
      inputField.clear();

      // Get the current stage
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      recordController.loadDataToTable();

      // Close the current stage
      stage.close();

      if (recordId != 0) {
        showSuccessDialog("Record updated successfully!");
      } else {
        showSuccessDialog("Record added successfully!");
      }
    } catch (SQLException e) {
      // Handle the SQLException here
      e.printStackTrace();
      // You can show an error message or perform any other error handling logic
    }
  }

  public void initFields() {
    if (recordId != 0) {
      try {
        // Get the record
        RecordDAO recordDao = new RecordDAO();
        Record record = recordDao.getRecord(recordId);

        titleField.setText(record.getTitle());
        // how to restrict changes on the titlefield

        inputField.setText(record.getInput().toString());
        categoryField.setValue(record.getCategory());
        subCategoryField.setValue(record.getSubcategory());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void handleDiscard(ActionEvent event) {
    // Get the current stage
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    // Close the current stage
    stage.close();
  }

  public void initialize() {
    // Define your categories and subcategories
    Map<String, List<String>> categories = new HashMap<>();
    categories.put("Travel", Arrays.asList("Walking", "Car"));
    categories.put("Electricity", Arrays.asList("AC", "Refrigerator"));
    categories.put("Water", Arrays.asList("Drinking", "Bathing", "Washing"));
    categories.put("Waste", Arrays.asList("Plastic", "Electronic"));

    // Populate the categoryField with the category names
    categoryField.getItems().addAll(categories.keySet());

    // Define a map for subcategories and their associated IDs
    Map<String, Integer> subCategoryIds = new HashMap<>();
    subCategoryIds.put("Walking", 1);
    subCategoryIds.put("Car", 2);
    subCategoryIds.put("AC", 3);
    subCategoryIds.put("Refrigerator", 4);
    subCategoryIds.put("Plastic", 5);
    subCategoryIds.put("Electronic", 6);
    subCategoryIds.put("Drinking", 7);
    subCategoryIds.put("Bathing", 8);
    subCategoryIds.put("Washing", 9);

    // Add a listener to handle category selection
    categoryField.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      // Clear the subcategory field
      subCategoryField.getItems().clear();

      // Add unit to input field label according to the category picked
      if (newValue.equals("Travel")) {
        inputFieldLabel.setText("Value (km)");
      } else if (newValue.equals("Electricity")) {
        inputFieldLabel.setText("Value (kWh)");
      } else if (newValue.equals("Water")) {
        inputFieldLabel.setText("Value (litre)");
      } else if (newValue.equals("Waste")) {
        inputFieldLabel.setText("Value (kg)");
      }

      // Get the selected category
      String selectedCategory = categoryField.getSelectionModel().getSelectedItem();

      // Get the subcategories for the selected category
      List<String> subcategories = categories.get(selectedCategory);

      // Update the subcategory field with the relevant subcategories
      if (subcategories != null) {
        subCategoryField.getItems().addAll(subcategories);
      }
    });

    // Add a listener to handle subcategory selection
    subCategoryField.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      // Get the selected subcategory
      String selectedSubCategory = subCategoryField.getSelectionModel().getSelectedItem();

        // Get the ID for the selected subcategory
        subCategoryId = subCategoryIds.get(selectedSubCategory);
        // Now you can use subCategoryId in your SQL statement
      });
      initFields();
  }

  private void showSuccessDialog(String message) {
    try {
      // Load the AlertInfo.fxml content
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/com/ecohub/fxml/AlertInfo.fxml")); // Make sure to replace with your actual path
      VBox alertInfoRoot = loader.load();

      // Create a new stage for the alert
      Stage alertStage = new Stage();
      alertStage.setTitle("Alert Information");
      alertStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
      alertStage.setResizable(false);

      // Get the controller
      AlertInfoController controller = loader.getController();
      controller.Msg_Label.setText(message);

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
