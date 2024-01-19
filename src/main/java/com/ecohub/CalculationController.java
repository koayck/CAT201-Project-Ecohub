package com.ecohub;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.Record;
import com.jfoenix.controls.JFXCheckBox;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CalculationController {

  @FXML
  private TableView<Record> table_result;

  @FXML
  private TableColumn<Record, String> col_category;

  @FXML
  private TableColumn<Record, String> col_activity;

  @FXML
  private TableColumn<Record, BigDecimal> col_input;

  @FXML
  private TableColumn<Record, BigDecimal> col_calculation;

  @FXML
  private TableColumn<Record, BigDecimal> col_percentage;

  @FXML
  private JFXCheckBox excludeElectricity;

  @FXML
  private JFXCheckBox excludeTravel;

  @FXML
  private JFXCheckBox excludeWaste;

  @FXML
  private JFXCheckBox excludeWater;

  @FXML
  private Label labelTotal;

  @FXML
  private Button refreshBtn;

  @FXML
  private JFXCheckBox selectAll;

  public void initialize() {
    refreshBtn.fire();

    List<CheckBox> otherCheckboxes = Arrays.asList(
      excludeElectricity,
      excludeTravel,
      excludeWater,
      excludeWaste
    );

    // Add a listener to the "Select All" checkbox
    selectAll
      .selectedProperty()
      .addListener((observable, oldValue, newValue) -> {
        if (newValue) {
          // If "Select All" is selected, uncheck all other checkboxes
          otherCheckboxes.forEach(checkbox -> checkbox.setSelected(false));
        }
      });

    // Add listeners to the other checkboxes
    for (CheckBox checkbox : otherCheckboxes) {
      checkbox
        .selectedProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue) {
            // If any other checkbox is selected, uncheck the "Select All" checkbox
            selectAll.setSelected(false);
          }
        });
    }

    // set up the category column
    col_category.setCellValueFactory(
      new PropertyValueFactory<Record, String>("category")
    );

    // Set up the 'activity' column
    col_activity.setCellValueFactory(
      new PropertyValueFactory<Record, String>("activity")
    );

    // Set up the 'input' column
    col_input.setCellValueFactory(
      new PropertyValueFactory<Record, BigDecimal>("input")
    );

    col_calculation.setCellValueFactory(cellData -> {
      BigDecimal value;
      String activity = cellData.getValue().getActivity();

      if ("Car".equals(activity)) {
        value =
          cellData
            .getValue()
            .getInput()
            .multiply(BigDecimal.valueOf(0.07))
            .multiply(BigDecimal.valueOf(2.349));
      } else if ("Walking".equals(activity)) {
        value =
          cellData.getValue().getInput().multiply(BigDecimal.valueOf(0.039));
      } else if (("AC".equals(activity) || "Refrigerator".equals(activity))) {
        value =
          cellData.getValue().getInput().multiply(BigDecimal.valueOf(0.39));
      } else if ("Plastic".equals(activity)) {
        value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(6));
      } else if ("Electronic".equals(activity)) {
        value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(2));
      } else if (
        (
          "Drinking".equals(activity) ||
          "Bathing".equals(activity) ||
          "Washing".equals(activity)
        )
      ) {
        value =
          cellData.getValue().getInput().multiply(BigDecimal.valueOf(0.298));
      } else {
        value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(0));
      }
      value = value.setScale(8, RoundingMode.HALF_UP);
      return new SimpleObjectProperty<BigDecimal>(value);
    });

    // set up the function to calculate percentage of each activity to total

    col_percentage.setCellValueFactory(cellData -> {
      BigDecimal value;
      String activity = cellData.getValue().getActivity();
      BigDecimal total;

      try {
        total = getTotal();
      } catch (SQLException e) {
        e.printStackTrace();
        return null;
      }

      if ("Car".equals(activity)) {
        value =
          cellData
            .getValue()
            .getInput()
            .multiply(BigDecimal.valueOf(0.07))
            .multiply(BigDecimal.valueOf(2.349));
      } else if ("Walking".equals(activity)) {
        value =
          cellData.getValue().getInput().multiply(BigDecimal.valueOf(0.039));
      } else if (("AC".equals(activity) || "Refrigerator".equals(activity))) {
        value =
          cellData.getValue().getInput().multiply(BigDecimal.valueOf(0.39));
      } else if ("Plastic".equals(activity)) {
        value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(6));
      } else if ("Electronic".equals(activity)) {
        value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(2));
      } else if (
        (
          "Drinking".equals(activity) ||
          "Bathing".equals(activity) ||
          "Washing".equals(activity)
        )
      ) {
        value =
          cellData.getValue().getInput().multiply(BigDecimal.valueOf(0.298));
      } else {
        value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(0));
      }

      BigDecimal percentage = value
        .divide(total, 2, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));
      return new SimpleObjectProperty<BigDecimal>(percentage);
    });
  }

  @FXML
  void refresh(ActionEvent event) throws SQLException {
    // RecordDAO recordDao = new RecordDAO();
    // // pass user id to get all records
    // List<Record> records = recordDao.getAllRecords(1);

    // // clear all record before adding new ones
    // table_result.getItems().clear();

    // BigDecimal total = getTotal();

    // total = total.setScale(2, RoundingMode.HALF_UP);
    // // Update the label
    // labelTotal.setText(total.toString());

    // // Add all records to the table
    // for (Record record : records) {
    //   table_result.getItems().add(record);
    // }
  }

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
  // public void handleClicks(ActionEvent actionEvent) {
  //   if (actionEvent.getSource() == btnCustomers) {
  //     pnlCustomer.setStyle("-fx-background-color : #1620A1");
  //     pnlCustomer.toFront();
  //   }
  //   if (actionEvent.getSource() == btnMenus) {
  //     pnlMenus.setStyle("-fx-background-color : #53639F");
  //     pnlMenus.toFront();
  //   }
  //   if (actionEvent.getSource() == btnOverview) {
  //     pnlOverview.setStyle("-fx-background-color : #02030A");
  //     pnlOverview.toFront();
  //   }
  //   if (actionEvent.getSource() == btnOrders) {
  //     pnlOrders.setStyle("-fx-background-color : #464F67");
  //     pnlOrders.toFront();
  //   }
  // }
}
