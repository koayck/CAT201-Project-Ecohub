package com.ecohub;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CalculationController {

    @FXML
    private TableView<result> table_result;

    @FXML
    private TableColumn<result, String> col_activity;

    @FXML
    private TableColumn<result, Double> col_input;

    @FXML
    private TableColumn<result, String> col_category;

    @FXML
    private TableColumn<result, Double> col_emission;

    @FXML
    private TableColumn<result, Double> col_percentage;

    @FXML
    private JFXCheckBox excludeElectricity;

    @FXML
    private JFXCheckBox excludeTravel1;

    @FXML
    private JFXCheckBox excludeWaste;

    @FXML
    private JFXCheckBox excludeWater;

    @FXML
    private Button refreshBtn;

    @FXML
    private JFXCheckBox selectAll;

}
