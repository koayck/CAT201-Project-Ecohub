package com.ecohub;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddRecordController {

  private RecordController recordController;

  public void getRecordController(RecordController recordController) {
    this.recordController = recordController;
  }

  private User user;

  public void initUser(User user) {
    this.user = user;
  }

  // Declare subCategoryId as an instance variable
  private Integer subCategoryId;

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
  Button cancelBtn;

  @FXML
  private void handleSubmit(ActionEvent event) {
    try {
      // Check if fields are not empty
      if (!titleField.getText().isEmpty() && !inputField.getText().isEmpty()) {
        String title = titleField.getText();
        String value = inputField.getText();
        RecordDAO recordDao = new RecordDAO();
        recordDao.addRecord(title, value, subCategoryId, user.getUser_id());
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

      showSuccessDialog();
    } catch (SQLException e) {
      // Handle the SQLException here
      e.printStackTrace();
      // You can show an error message or perform any other error handling logic
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
    subCategoryIds.put("Drinking", 5);
    subCategoryIds.put("Bathing", 6);
    subCategoryIds.put("Washing", 7);
    subCategoryIds.put("Plastic", 8);
    subCategoryIds.put("Electronic", 9);

    // Add a listener to handle category selection
    categoryField
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((options, oldValue, newValue) -> {
        // Clear the subcategory field
        subCategoryField.getItems().clear();

        // Get the selected category
        String selectedCategory = categoryField
          .getSelectionModel()
          .getSelectedItem();

        // Get the subcategories for the selected category
        List<String> subcategories = categories.get(selectedCategory);

        // Update the subcategory field with the relevant subcategories
        if (subcategories != null) {
          subCategoryField.getItems().addAll(subcategories);
        }
      });

    // Add a listener to handle subcategory selection
    subCategoryField
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((options, oldValue, newValue) -> {
        // Get the selected subcategory
        String selectedSubCategory = subCategoryField
          .getSelectionModel()
          .getSelectedItem();

        // Get the ID for the selected subcategory
        subCategoryId = subCategoryIds.get(selectedSubCategory);
        // Now you can use subCategoryId in your SQL statement
      });
  }

  private void showSuccessDialog() {
    try {
      // Load the AlertInfo.fxml content
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(
        getClass().getResource("AlertInfo.fxml")
      ); // Make sure to replace with your actual path
      VBox alertInfoRoot = loader.load();

      // Create a new stage for the alert
      Stage alertStage = new Stage();
      alertStage.setTitle("Alert Information");
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
}
