package com.ecohub.controller;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.Record;
import com.ecohub.session.UserSession;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

public class RecordController implements Initializable {

  @FXML
  private Label recordCount;

  @FXML
  private Label totalCarbon;

  @FXML
  private TextField searchBar;

  @FXML
  private VBox pnItems;

  @FXML
  private JFXComboBox<String> subCategoryBox;

  @FXML
  private JFXComboBox<String> categoryBox;

  @FXML
  private JFXButton clearFilter;

  @FXML
  void clearFilter(ActionEvent event) {
    subCategoryBox.getSelectionModel().clearSelection();
    categoryBox.getSelectionModel().clearSelection();
    subCategoryBox.getSelectionModel().selectFirst();
    categoryBox.getSelectionModel().selectFirst();
    subCategoryBox.getSelectionModel().selectFirst();
  }

  @FXML
  private Button addPopupBtn;

  @FXML
  private Label labelTotal;

  @FXML
  public void showAddPopup(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/com/ecohub/fxml/AddRecord.fxml")
    );
    Parent root = loader.load();

    // Access the controller of the popup
    AddRecordController controller = loader.getController();

    // Pass the current controller to the popup controller
    controller.getRecordController(this);

    controller.initDialogTitle("Add a new record");

    clearFilter.fire();
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();
  }

  public void loadDataToTableWithSearch() {
    Task<List<Record>> task = new Task<List<Record>>() {
      @Override
      protected List<Record> call() throws Exception {
        String keyword = searchBar.getText(); // get the text from the search bar
        RecordDAO recordDao = new RecordDAO();
        return recordDao.getAllRecords(
          UserSession.getInstance().getUserId(),
          keyword
        ); // use the new function
      }
    };

    task.setOnSucceeded(e -> {
      List<Record> records = task.getValue();

      

      RecordDAO recordDao = new RecordDAO();
      try {
        BigDecimal total = recordDao.getTotalCarbon(
          UserSession.getInstance().getUserId(),
          null,
          null
        );
        if (total == null) {
          totalCarbon.setText("0");
        } else {
          totalCarbon.setText((total).toString());
        }
        recordCount.setText(String.valueOf(records.size()));
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      pnItems.getChildren().clear(); // Clear existing items

      DecimalFormat df = new DecimalFormat("#.##");

      for (Record record : records) {
        try {
          FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/ecohub/fxml/RecordItem.fxml")
          );
          Node node = loader.load();
          RecordItemController controller = loader.getController();
          controller.getRecordController(this);

          Pair<String, String> iconInfo = getIcon(record.getCategory());
          controller.categoryIcon.setContent(iconInfo.getKey());
          controller.categoryIcon.setFill(Color.web(iconInfo.getValue()));

          controller.subcategory.setText(record.getSubcategory());
          controller.title.setText(record.getTitle());
          controller.date.setText(record.getDate().toString());
          controller.value.setText(record.getInput().toString());

          BigDecimal footprint = record.getFootprint();

          String formattedFootprint = df.format(footprint);
          controller.footprint.setText(formattedFootprint);

          controller.recordId.setText(String.valueOf(record.getRecord_id()));

          pnItems.getChildren().add(node);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    new Thread(task).start();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadDataToTable();

    searchBar
      .textProperty()
      .addListener((observable, oldValue, newValue) -> {
        loadDataToTableWithSearch(); // load records that match the keyword when the text in the searchBar changes
      });

    ObservableList<String> categories = FXCollections.observableArrayList(
      "All Categories",
      "Travel",
      "Electricity",
      "Waste",
      "Water"
    );
    categoryBox.setItems(categories);
    categoryBox.getSelectionModel().selectFirst();

    ObservableList<String> activities = FXCollections.observableArrayList(
      "All Subcategories",
      "Walking",
      "Car",
      "AC",
      "Refrigerator",
      "Plastic",
      "Electronic",
      "Drinking",
      "Bathing",
      "Washing"
    );
    subCategoryBox.setItems(activities);
    subCategoryBox.getSelectionModel().selectFirst();

    ChangeListener<String> categoryListener = (options, oldValue, newValue) -> {
      subCategoryBox.getItems().clear();
      if (newValue == null || newValue.equals("All Categories")) {
        // If no category is selected or "All Categories" is selected, show all
        // activities
        subCategoryBox
          .getItems()
          .addAll(
            "All Subcategories",
            "Walking",
            "Car",
            "AC",
            "Refrigerator",
            "Plastic",
            "Electronic",
            "Drinking",
            "Bathing",
            "Washing"
          );
        subCategoryBox.getSelectionModel().selectFirst();
        loadDataToTable();
      } else if (newValue.equals("Travel")) {
        subCategoryBox.getItems().addAll("All Subcategories", "Walking", "Car");
        subCategoryBox.getSelectionModel().selectFirst();
        loadFilterDataToTable(newValue, null, 1);
      } else if (newValue.equals("Electricity")) {
        subCategoryBox
          .getItems()
          .addAll("All Subcategories", "AC", "Refrigerator");
        subCategoryBox.getSelectionModel().selectFirst();
        loadFilterDataToTable(newValue, null, 1);
      } else if (newValue.equals("Waste")) {
        subCategoryBox
          .getItems()
          .addAll("All Subcategories", "Plastic", "Electronic");
        subCategoryBox.getSelectionModel().selectFirst();
        loadFilterDataToTable(newValue, null, 1);
      } else if (newValue.equals("Water")) {
        subCategoryBox
          .getItems()
          .addAll("All Subcategories", "Drinking", "Bathing", "Washing");
        subCategoryBox.getSelectionModel().selectFirst();
        loadFilterDataToTable(newValue, null, 1);
      }
    };

    categoryBox
      .getSelectionModel()
      .selectedItemProperty()
      .addListener(categoryListener);

    subCategoryBox
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((options, oldValue, newValue) -> {
        if (newValue != null) {
          // If an subcategory is selected, select the related category
          categoryBox
            .getSelectionModel()
            .selectedItemProperty()
            .removeListener(categoryListener);
          if (newValue.equals("All Activities")) {
            loadDataToTable();
            categoryBox.getSelectionModel().select("All Categories");
          } else if (Arrays.asList("Walking", "Car").contains(newValue)) {
            categoryBox.getSelectionModel().select("Travel");
            loadFilterDataToTable(null, newValue, 2);
          } else if (Arrays.asList("AC", "Refrigerator").contains(newValue)) {
            loadFilterDataToTable(null, newValue, 2);
            categoryBox.getSelectionModel().select("Electricity");
          } else if (
            Arrays.asList("Plastic", "Electronic").contains(newValue)
          ) {
            loadFilterDataToTable(null, newValue, 2);
            categoryBox.getSelectionModel().select("Waste");
          } else if (
            Arrays.asList("Drinking", "Bathing", "Washing").contains(newValue)
          ) {
            categoryBox.getSelectionModel().select("Water");
            loadFilterDataToTable(null, newValue, 2);
          }

          categoryBox
            .getSelectionModel()
            .selectedItemProperty()
            .addListener(categoryListener);
        }
      });
  }

  public void loadDataToTable() {
    Task<List<Record>> task = new Task<List<Record>>() {
      @Override
      protected List<Record> call() throws Exception {
        RecordDAO recordDao = new RecordDAO();
        return recordDao.getAllRecords(
          UserSession.getInstance().getUserId(),
          null
        );
      }
    };

    task.setOnSucceeded(e -> {
      List<Record> records = task.getValue();

      RecordDAO recordDao = new RecordDAO();
      try {
        BigDecimal total = recordDao.getTotalCarbon(
          UserSession.getInstance().getUserId(),
          null,
          null
        );
        if (total == null) {
          totalCarbon.setText("0");
        } else {
          totalCarbon.setText((total).toString());
        }
        recordCount.setText(String.valueOf(records.size()));
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      pnItems.getChildren().clear(); // Clear existing items

      for (Record record : records) {
        try {
          FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/ecohub/fxml/RecordItem.fxml")
          );
          Node node = loader.load();
          RecordItemController controller = loader.getController();
          controller.getRecordController(this);

          Pair<String, String> iconInfo = getIcon(record.getCategory());
          controller.categoryIcon.setContent(iconInfo.getKey());
          controller.categoryIcon.setFill(Color.web(iconInfo.getValue()));

          controller.subcategory.setText(record.getSubcategory());
          controller.title.setText(record.getTitle());
          controller.date.setText(record.getDate().toString());
          controller.value.setText(record.getInput().toString());

          DecimalFormat df = new DecimalFormat("#.##");
          BigDecimal footprint = record.getFootprint();
          String formattedFootprint = df.format(footprint);

          controller.footprint.setText(formattedFootprint);

          controller.recordId.setText(String.valueOf(record.getRecord_id()));

          pnItems.getChildren().add(node);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    new Thread(task).start();
  }

  public void loadFilterDataToTable(
    String categoryFilter,
    String subCategoryFilter,
    int flag
  ) {
    Task<List<Record>> task = new Task<List<Record>>() {
      @Override
      protected List<Record> call() throws Exception {
        String filter = categoryFilter;
        if (categoryFilter == null) {
          filter = subCategoryFilter;
        }
        RecordDAO recordDao = new RecordDAO();
        return recordDao.getFilteredRecords(
          UserSession.getInstance().getUserId(),
          filter,
          flag
        );
      }
    };

    task.setOnSucceeded(e -> {
      List<Record> records = task.getValue();

      RecordDAO recordDao = new RecordDAO();

      try {
        BigDecimal total = recordDao.getTotalCarbon(
          UserSession.getInstance().getUserId(),
          categoryFilter,
          subCategoryFilter
        );
        if (total == null) {
          totalCarbon.setText("0");
        } else {
          totalCarbon.setText((total).toString());
        }
        recordCount.setText(String.valueOf(records.size()));
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      pnItems.getChildren().clear(); // Clear existing items

      for (Record record : records) {
        try {
          FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/ecohub/fxml/RecordItem.fxml")
          );
          Node node = loader.load();
          RecordItemController controller = loader.getController();
          controller.getRecordController(this);

          DecimalFormat df = new DecimalFormat("#.##");

          Pair<String, String> iconInfo = getIcon(record.getCategory());
          controller.categoryIcon.setContent(iconInfo.getKey());
          controller.categoryIcon.setFill(Color.web(iconInfo.getValue()));

          controller.subcategory.setText(record.getSubcategory());
          controller.title.setText(record.getTitle());
          controller.date.setText(record.getDate().toString());
          controller.value.setText(record.getInput().toString());

          BigDecimal footprint = record.getFootprint();
          String formattedFootprint = df.format(footprint);
          controller.footprint.setText(formattedFootprint);

          controller.recordId.setText(String.valueOf(record.getRecord_id()));

          pnItems.getChildren().add(node);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    new Thread(task).start();
  }

  public Pair<String, String> getIcon(String category) {
    String svgPath = "";
    String color = "#000000";
    switch (category.toLowerCase()) {
      case "water":
        svgPath =
          "M7.21.8C7.69.295 8 0 8 0q.164.544.371 1.038c.812 1.946 2.073 3.35 3.197 4.6C12.878 7.096 14 8.345 14 10a6 6 0 0 1-12 0C2 6.668 5.58 2.517 7.21.8m.413 1.021A31 31 0 0 0 5.794 3.99c-.726.95-1.436 2.008-1.96 3.07C3.304 8.133 3 9.138 3 10c0 0 2.5 1.5 5 .5s5-.5 5-.5c0-1.201-.796-2.157-2.181-3.7l-.03-.032C9.75 5.11 8.5 3.72 7.623 1.82z";
        color = "#3499f1";
        break;
      case "electricity":
        svgPath =
          "M5.52.359A.5.5 0 0 1 6 0h4a.5.5 0 0 1 .474.658L8.694 6H12.5a.5.5 0 0 1 .395.807l-7 9a.5.5 0 0 1-.873-.454L6.823 9.5H3.5a.5.5 0 0 1-.48-.641z";
        color = "#ffd714";
        break;
      case "waste":
        svgPath =
          "M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5M8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5m3 .5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 1 0";
        color = "#9b72b6";
        break;
      case "travel":
        svgPath =
          "M2.52 3.515A2.5 2.5 0 0 1 4.82 2h6.362c1 0 1.904.596 2.298 1.515l.792 1.848c.075.175.21.319.38.404.5.25.855.715.965 1.262l.335 1.679q.05.242.049.49v.413c0 .814-.39 1.543-1 1.997V13.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-1.338c-1.292.048-2.745.088-4 .088s-2.708-.04-4-.088V13.5a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1-.5-.5v-1.892c-.61-.454-1-1.183-1-1.997v-.413a2.5 2.5 0 0 1 .049-.49l.335-1.68c.11-.546.465-1.012.964-1.261a.8.8 0 0 0 .381-.404l.792-1.848ZM3 10a1 1 0 1 0 0-2 1 1 0 0 0 0 2m10 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2M6 8a1 1 0 0 0 0 2h4a1 1 0 1 0 0-2zM2.906 5.189a.51.51 0 0 0 .497.731c.91-.073 3.35-.17 4.597-.17s3.688.097 4.597.17a.51.51 0 0 0 .497-.731l-.956-1.913A.5.5 0 0 0 11.691 3H4.309a.5.5 0 0 0-.447.276L2.906 5.19Z";
        color = "#a43431";
        break;
      default:
        svgPath = ""; // default case when no match is found
        break;
    }
    return new Pair<>(svgPath, color);
  }
}
