package com.ecohub;

//Johnas's part
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class testController {

    @FXML
    private TextField acText;

    @FXML
    private TextField inText;

    @FXML
    private Button submitBtn;

    @FXML
    void save(ActionEvent event) throws SQLException {
        String activity = acText.getText();
        String input = inText.getText();

        testdb jdbcDao = new testdb();
        jdbcDao.insertRecord(activity, input);
    }

    @FXML
    public void initialize() {
        inText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                inText.setText(oldValue);
            }
        });

    }
}