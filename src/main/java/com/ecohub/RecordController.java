package com.ecohub;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.Record;
import com.ecohub.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
  private JFXCheckBox selectAll;

  @FXML
  private JFXCheckBox excludeElectricity;

  @FXML
  private JFXCheckBox excludeWaste;

  @FXML
  private JFXCheckBox excludeWater;

  @FXML
  private JFXCheckBox excludeTravel;

  @FXML
  private JFXButton refreshBtn;

  @FXML
  private Button addPopupBtn;

  @FXML
  private Label labelTotal;

  List<String> excludedCategories = new ArrayList<>();

  @FXML
  void selectAll(ActionEvent event) {
    excludedCategories.clear();
  }

  // Maintain flags for each category
  boolean isElectricityExcluded = false;
  boolean isTravelExcluded = false;
  boolean isWasteExcluded = false;
  boolean isWaterExcluded = false;

  @FXML
  void exclElectricity(ActionEvent event) {
    isElectricityExcluded = !isElectricityExcluded;
    updateExcludedCategories("Electricity", isElectricityExcluded);
  }

  @FXML
  void exclTravel(ActionEvent event) {
    isTravelExcluded = !isTravelExcluded;
    updateExcludedCategories("Travel", isTravelExcluded);
  }

  @FXML
  void exclWaste(ActionEvent event) {
    isWasteExcluded = !isWasteExcluded;
    updateExcludedCategories("Waste", isWasteExcluded);
  }

  @FXML
  void exclWater(ActionEvent event) {
    isWaterExcluded = !isWaterExcluded;
    updateExcludedCategories("Water", isWaterExcluded);
  }

  private void updateExcludedCategories(String category, boolean isExcluded) {
    if (isExcluded && !excludedCategories.contains(category)) {
      excludedCategories.add(category);
    } else if (!isExcluded) {
      excludedCategories.remove(category);
    }
  }

  @FXML
  public void showAddPopup(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("AddRecord.fxml")
    );
    Parent root = loader.load();

    // Access the controller of the popup
    AddRecordController controller = loader.getController();

    controller.getRecordController(this);

    // Pass the user object to the popup controller
    controller.initUser(user);

    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadDataToTable();
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
          FXMLLoader loader = new FXMLLoader(
            getClass().getResource("RecordItem.fxml")
          );
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
