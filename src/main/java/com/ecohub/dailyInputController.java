package com.ecohub;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.ecohub.dao.RecordDAO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class dailyInputController {

    @FXML
    private TextField inputVal;

    @FXML
    private JFXComboBox<String> categoryBox;

    @FXML
    private TextField recordText;

    @FXML
    private JFXButton submitButton;

    @FXML
    private Label submitLabel;


    @FXML
    private void handleSubmit(ActionEvent event) throws SQLException {

        //Insert into database
        if (!inputVal.getText().isEmpty()) {
            String category = "Travel";
            String activity = categoryBox.getValue();
            String input = inputVal.getText();
            RecordDAO recordDao = new RecordDAO();
            recordDao.addRecord(category, activity, input);
        }
        submitLabel.setText("Submitted Sucessfully!");
        
        //Clear the inputs
        categoryBox.valueProperty().set(null);
        inputVal.clear();
        recordText.clear();
    }

    @FXML
    void initialize() {
        categoryBox.getItems().add("Distance");
        categoryBox.getItems().add("Electricity");
        categoryBox.getItems().add("Waste");
        categoryBox.getItems().add("Water");
    }
}
