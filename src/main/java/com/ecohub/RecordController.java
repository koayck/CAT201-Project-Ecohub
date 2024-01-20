package com.ecohub;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.Record;
import com.ecohub.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecordController implements Initializable {

  private User user;

  public void initUser(User user) {
    this.user = user;
  }

  @FXML
  private VBox pnItems;

  @FXML
  private JFXComboBox<String> activityBox;

  @FXML
  private JFXComboBox<String> categoryBox;

  @FXML
  private JFXButton clearFilter;

  @FXML
  void clearFilter(ActionEvent event) {
    activityBox.getSelectionModel().clearSelection();
    activityBox.setPromptText("Filter by Categories");
    categoryBox.getSelectionModel().clearSelection();
    categoryBox.setPromptText("Filter by Categories");
  }

  @FXML
  private Button addPopupBtn;

  @FXML
  private Label labelTotal;

  @FXML
  public void showAddPopup(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddRecord.fxml"));
    Parent root = loader.load();

    // Access the controller of the popup
    AddRecordController controller = loader.getController();

    controller.getRecordController(this);

    // Pass the user object to the popup controller
    controller.initUser(user);

    clearFilter.fire();
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadDataToTable();
    ObservableList<String> categories = FXCollections.observableArrayList("All Categories", "Travel", "Electricity",
        "Waste", "Water");
    categoryBox.setItems(categories);

    ObservableList<String> activities = FXCollections.observableArrayList("All Activities", "Walking", "Car", "AC",
        "Refrigerator", "Plastic", "Electronic", "Drinking", "Bathing", "Washing");
    activityBox.setItems(activities);

    ChangeListener<String> categoryListener = (options, oldValue, newValue) -> {
      activityBox.getItems().clear();
      if (newValue == null || newValue.equals("All Categories")) {
        // If no category is selected or "All Categories" is selected, show all
        // activities
        activityBox.getItems().addAll("All Activities", "Walking", "Car", "AC", "Refrigerator", "Plastic", "Electronic",
            "Drinking", "Bathing", "Washing");
        loadDataToTable();
      } else if (newValue.equals("Travel")) {
        activityBox.getItems().addAll("Walking", "Car");
        loadFilterDataToTable(newValue, 1);
      } else if (newValue.equals("Electricity")) {
        activityBox.getItems().addAll("AC", "Refrigerator");
        loadFilterDataToTable(newValue, 1);
      } else if (newValue.equals("Waste")) {
        activityBox.getItems().addAll("Plastic", "Electronic");
        loadFilterDataToTable(newValue, 1);
      } else if (newValue.equals("Water")) {
        activityBox.getItems().addAll("Drinking", "Bathing", "Washing");
        loadFilterDataToTable(newValue, 1);
      }
    };

    categoryBox.getSelectionModel().selectedItemProperty().addListener(categoryListener);

    activityBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      if (newValue != null) {
        // If an activity is selected, select the related category
        categoryBox.getSelectionModel().selectedItemProperty().removeListener(categoryListener);
        if (newValue.equals("All Activities")) {
          loadDataToTable();
          categoryBox.getSelectionModel().select("All Categories");
        } else if (Arrays.asList("Walking", "Car").contains(newValue)) {
          categoryBox.getSelectionModel().select("Travel");
          loadFilterDataToTable(newValue, 2);
        } else if (Arrays.asList("AC", "Refrigerator").contains(newValue)) {
          loadFilterDataToTable(newValue, 2);
          categoryBox.getSelectionModel().select("Electricity");
        } else if (Arrays.asList("Plastic", "Electronic").contains(newValue)) {
          loadFilterDataToTable(newValue, 2);
          categoryBox.getSelectionModel().select("Waste");
        } else if (Arrays.asList("Drinking", "Bathing", "Washing").contains(newValue)) {
          categoryBox.getSelectionModel().select("Water");
          loadFilterDataToTable(newValue, 2);
        }
        categoryBox.getSelectionModel().selectedItemProperty().addListener(categoryListener);
      }
    });
  }

  public void loadDataToTable() {
    Task<List<Record>> task = new Task<List<Record>>() {
      @Override
      protected List<Record> call() throws Exception {
        RecordDAO recordDao = new RecordDAO();
        return recordDao.getAllRecords(1);
      }
    };

    System.out.println("Loading data to table...");
    System.out.println(pnItems);

    task.setOnSucceeded(e -> {
      System.out.println("In succedd...");
      List<Record> records = task.getValue();

      pnItems.getChildren().clear(); // Clear existing items

      for (Record record : records) {
        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordItem.fxml"));
          Node node = loader.load();
          RecordItemController controller = loader.getController();
          controller.getRecordController(this);

          controller.subcategory.setText(record.getSubcategory());
          controller.title.setText(record.getActivity());
          controller.date.setText(record.getDate().toString());
          controller.value.setText(record.getInput().toString());
          controller.footprint.setText(record.getFootprint().toString());
          controller.recordId.setText(String.valueOf(record.getRecord_id()));

          pnItems.getChildren().add(node);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    new Thread(task).start();
  }

  public void loadFilterDataToTable(String filter, int flag) {
    Task<List<Record>> task = new Task<List<Record>>() {
      @Override
      protected List<Record> call() throws Exception {
        RecordDAO recordDao = new RecordDAO();
        return recordDao.getFilteredRecords(1, filter, flag);
      }
    };

    System.out.println("Loading data to table...");
    System.out.println(pnItems);

    task.setOnSucceeded(e -> {
      System.out.println("In succedd...");
      List<Record> records = task.getValue();

      pnItems.getChildren().clear(); // Clear existing items

      for (Record record : records) {
        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordItem.fxml"));
          Node node = loader.load();
          RecordItemController controller = loader.getController();
          controller.getRecordController(this);

          controller.subcategory.setText(record.getSubcategory());
          controller.title.setText(record.getActivity());
          controller.date.setText(record.getDate().toString());
          controller.value.setText(record.getInput().toString());
          controller.footprint.setText(record.getFootprint().toString());
          controller.recordId.setText(String.valueOf(record.getRecord_id()));

          pnItems.getChildren().add(node);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    new Thread(task).start();
  }
}
