package com.ecohub;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.User;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class dailyInputController {

    private User user;

    public void initUser(User user) {
        this.user = user;
        // You can now use this user object in your DailyInputController
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private Label submitLabel;

    @FXML
    private URL location;

    @FXML
    private MenuItem AC;

    @FXML
    private MenuItem Bathing;

    @FXML
    private MenuItem Car;

    @FXML
    private MenuItem Drinking;

    @FXML
    private MenuItem Electronic;

    @FXML
    private MenuItem Plastic;

    @FXML
    private MenuItem Refrigerator;

    @FXML
    private MenuItem Walking;

    @FXML
    private MenuItem Washing;

    @FXML
    JFXComboBox<String> modeBox;

    @FXML
    JFXComboBox<String> elecBox;

    @FXML
    JFXComboBox<String> wasteBox;

    @FXML
    JFXComboBox<String> waterBox;

    @FXML
    private TextField distanceText;

    @FXML
    private TextField elecText;

    @FXML
    private TextField wasteText;

    @FXML
    private TextField waterText;

    @FXML
    private TextField recordText;

    @FXML
    private void handleSubmit(ActionEvent event) throws SQLException {

        // For distanceText and distanceLabel
        if (!distanceText.getText().isEmpty()) {
            String category = "Travel";
            String activity = recordText.getText();
            String input = distanceText.getText();
            RecordDAO recordDao = new RecordDAO();
            recordDao.addRecord(category, activity, input, user.getUser_id());
        }

        // For elecText and electricLabel
        if (!elecText.getText().isEmpty()) {
            String category = "Electricity";
            String activity = recordText.getText();
            String input = elecText.getText();
            RecordDAO recordDao = new RecordDAO();
            recordDao.addRecord(category, activity, input, user.getUser_id());
        }

        // For wasteText and wasteLabel
        if (!wasteText.getText().isEmpty()) {
            String category = "Waste";
            String activity = recordText.getText();
            String input = wasteText.getText();
            RecordDAO recordDao = new RecordDAO();
            recordDao.addRecord(category, activity, input, user.getUser_id());
        }

        // For waterText and waterLabel
        if (!waterText.getText().isEmpty()) {
            String category = "Water";
            String activity = recordText.getText();
            String input = waterText.getText();
            RecordDAO recordDao = new RecordDAO();
            recordDao.addRecord(category, activity, input, user.getUser_id());
        }

        modeBox.valueProperty().set(null);
        elecBox.valueProperty().set(null);
        wasteBox.valueProperty().set(null);
        waterBox.valueProperty().set(null);

        distanceText.clear();
        elecText.clear();
        wasteText.clear();
        waterText.clear();
        recordText.clear();

        submitLabel.setText("Submitted Sucessfully!");
    }

    @FXML
    void initialize() {
        modeBox.getItems().add("Walking");
        modeBox.getItems().add("Car");

        elecBox.getItems().add("AC");
        elecBox.getItems().add("Refrigerator");

        wasteBox.getItems().add("Plastic");
        wasteBox.getItems().add("Electronic");

        waterBox.getItems().add("Drinking");
        waterBox.getItems().add("Bathing");
        waterBox.getItems().add("Washing");

    }
}
