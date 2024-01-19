package com.ecohub;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.Record;
import com.jfoenix.controls.JFXCheckBox;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public void initialize() {

        refreshBtn.fire();
        RecordDAO recordDAO = new RecordDAO();

        List<CheckBox> otherCheckboxes = Arrays.asList(excludeElectricity, excludeTravel, excludeWater, excludeWaste);

        // Add a listener to the "Select All" checkbox
        selectAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // If "Select All" is selected, uncheck all other checkboxes
                otherCheckboxes.forEach(checkbox -> checkbox.setSelected(false));
            }
        });

        // Add listeners to the other checkboxes
        for (CheckBox checkbox : otherCheckboxes) {
            checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // If any other checkbox is selected, uncheck the "Select All" checkbox
                    selectAll.setSelected(false);
                }
            });
        }

        // set up the category column
        col_category.setCellValueFactory(new PropertyValueFactory<Record, String>("category"));

        // Set up the 'activity' column
        col_activity.setCellValueFactory(new PropertyValueFactory<Record, String>("activity"));

        // Set up the 'input' column
        col_input.setCellValueFactory(new PropertyValueFactory<Record, BigDecimal>("input"));

        col_calculation.setCellValueFactory(cellData -> {
            String activity = cellData.getValue().getActivity();
            BigDecimal input = cellData.getValue().getInput();

            // Use the calculateValue function to calculate the value based on the activity
            BigDecimal value = recordDAO.calculateValue(activity, input);

            value = value.setScale(8, RoundingMode.HALF_UP);
            return new SimpleObjectProperty<BigDecimal>(value);
        });

        // set up the function to calculate percentage of each activity to total

        col_percentage.setCellValueFactory(cellData -> {
            String activity = cellData.getValue().getActivity();
            BigDecimal total;

            try {
                total = getTotal();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }

            BigDecimal percentage = recordDAO.calculatePercentage(activity, cellData.getValue().getInput(), total);
            return new SimpleObjectProperty<BigDecimal>(percentage);
        });
        ;

    }

    @FXML
    void refresh(ActionEvent event) throws SQLException {
        RecordDAO recordDao = new RecordDAO();
        // pass user id to get all records
        List<Record> records = recordDao.getAllRecords(1);

        // clear all record before adding new ones
        table_result.getItems().clear();

        BigDecimal total = getTotal();

        total = total.setScale(2, RoundingMode.HALF_UP);
        // Update the label
        labelTotal.setText(total.toString());

        // Add all records to the table
        for (Record record : records) {
            table_result.getItems().add(record);
        }

    }

    private BigDecimal getTotal() throws SQLException {
        RecordDAO recordDao = new RecordDAO();

        List<Record> records = recordDao.getAllRecords(1);

        BigDecimal total = BigDecimal.ZERO;
        for (Record record : records) {
            String activity = record.getActivity();
            BigDecimal input = record.getInput();

            // Use the calculateValue function to calculate the value based on the activity
            BigDecimal calculation = recordDao.calculateValue(activity, input);

            total = total.add(calculation);
        }
        return total;
    }

}
