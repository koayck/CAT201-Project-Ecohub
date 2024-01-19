package com.ecohub;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.Record;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class RecordController implements Initializable {

  @FXML
  private VBox pnItems;

  @FXML
  private Pane pnlCustomer;

  @FXML
  private Pane pnlOrders;

  @FXML
  private Pane pnlOverview;

  @FXML
  private Pane pnlMenus;

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
  private Button refreshBtn;

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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Task<List<Record>> task = new Task<List<Record>>() {
      @Override
      protected List<Record> call() throws Exception {
        RecordDAO recordDao = new RecordDAO();
        return recordDao.getAllRecords(1);
      }
    };

    task.setOnSucceeded(e -> {
      List<Record> records = task.getValue();

      System.out.println(records);
      for (Record record : records) {
        try {
          FXMLLoader loader = new FXMLLoader(
            getClass().getResource("RecordItem.fxml")
          );
          Node node = loader.load();
          RecordItemController controller = loader.getController();

          System.out.print(record);
          controller.category.setText(record.getCategory());
          controller.title.setText(record.getActivity());
          // controller.date.setText(record.getDate());
          controller.value.setText(record.getInput().toString());
          // controller.footprint.setText(record.getFootprint());

          pnItems.getChildren().add(node);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });

    new Thread(task).start();
  }

  // // @FXML
  // void refresh(ActionEvent event) throws SQLException {
  //   RecordDAO recordDao = new RecordDAO();
  //   // pass user id to get all records
  //   List<Record> records = recordDao.getAllRecords(1);

  //   // clear all record before adding new ones
  //   // table_result.getItems().clear();
  //   System.out.println(records);

  //   BigDecimal total = getTotal();

  //   total = total.setScale(2, RoundingMode.HALF_UP);
  //   // Update the label
  //   labelTotal.setText(total.toString());

  //   // // Add all records to the table
  //   // for (Record record : records) {
  //   //   table_result.getItems().add(record);
  //   // }
  // }

  private BigDecimal getTotal() throws SQLException {
    RecordDAO recordDao = new RecordDAO();

    List<Record> records = recordDao.getAllRecords(1);

    BigDecimal total = BigDecimal.ZERO;
    for (Record record : records) {
      BigDecimal calculation = record
        .getInput()
        .multiply(BigDecimal.valueOf(0.66));
      total = total.add(calculation);
    }
    return total;
  }
  //   public void handleClicks(ActionEvent actionEvent) {
  //     if (actionEvent.getSource() == btnCustomers) {
  //       pnlCustomer.setStyle("-fx-background-color : #1620A1");
  //       pnlCustomer.toFront();
  //     }
  //     if (actionEvent.getSource() == btnMenus) {
  //       pnlMenus.setStyle("-fx-background-color : #53639F");
  //       pnlMenus.toFront();
  //     }
  //     if (actionEvent.getSource() == btnOverview) {
  //       pnlOverview.setStyle("-fx-background-color : #02030A");
  //       pnlOverview.toFront();
  //     }
  //     if (actionEvent.getSource() == btnOrders) {
  //       pnlOrders.setStyle("-fx-background-color : #464F67");
  //       pnlOrders.toFront();
  //     }
  //   }
}
