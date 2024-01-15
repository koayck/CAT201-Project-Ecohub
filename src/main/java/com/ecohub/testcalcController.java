package com.ecohub;

import com.jfoenix.controls.JFXCheckBox;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class testcalcController {

    @FXML
    private TableView<result> table_result;

    @FXML
    private TableColumn<result, String> col_activity;

    @FXML
    private TableColumn<result, BigDecimal> col_input;

    @FXML
    private TableColumn<result, BigDecimal> col_calculation;

    @FXML
    private TableColumn<result, BigDecimal> col_percentage;

    @FXML
    private JFXCheckBox excludeElectricity;

    @FXML
    private JFXCheckBox excludeTravel1;

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
        // refresh table
        refreshBtn.fire();
        // Set up the 'activity' column
        col_activity.setCellValueFactory(new PropertyValueFactory<result, String>("activity"));

        // Set up the 'input' column
        col_input.setCellValueFactory(new PropertyValueFactory<result, BigDecimal>("input"));

        col_calculation.setCellValueFactory(cellData -> {
            BigDecimal value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(0.66));
            value = value.setScale(2, RoundingMode.HALF_UP);
            return new SimpleObjectProperty<BigDecimal>(value);
        });

        // Set up the 'percentage' column so that it calculates the percentage of the
        // input
        col_percentage.setCellValueFactory(cellData -> {
            try {
                BigDecimal value = cellData.getValue().getInput().multiply(BigDecimal.valueOf(100)).divide(getTotal(),
                        2, RoundingMode.HALF_UP);
                return new SimpleObjectProperty<BigDecimal>(value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

    }

    @FXML
    void refresh(ActionEvent event) throws SQLException {
        testdb jdbcDao = new testdb();
        List<result> records = jdbcDao.getAllRecords();

        // clear all record before adding new ones
        table_result.getItems().clear();

        BigDecimal total = getTotal();

        total = total.setScale(2, RoundingMode.HALF_UP);
        // Update the label
        labelTotal.setText(total.toString());

        // Add all records to the table
        for (result record : records) {
            table_result.getItems().add(record);
        }

    }

    private BigDecimal getTotal() throws SQLException {
        testdb jdbcDao = new testdb();

        List<result> records = jdbcDao.getAllRecords();

        BigDecimal total = BigDecimal.ZERO;
        for (result record : records) {
            BigDecimal calculation = record.getInput().multiply(BigDecimal.valueOf(0.66));
            total = total.add(calculation);
        }
        return total;
    }

}
