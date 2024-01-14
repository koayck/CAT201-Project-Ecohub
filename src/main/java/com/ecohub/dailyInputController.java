package com.ecohub;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class dailyInputController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private Label submitLabel;

    @FXML
    private URL location;

    @FXML
    private Label distanceLabel;

    @FXML
    private Label electricLabel;

    @FXML
    private Label wasteLabel;

    @FXML
    private Label waterLabel;

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
    void AC(ActionEvent event) {
        electricLabel.setText("AC"); 
    }

    @FXML
    void Bathing(ActionEvent event) {
        waterLabel.setText("Bathing");   
    }

    @FXML
    void Car(ActionEvent event) {
        distanceLabel.setText("Car");
    }

    @FXML
    void Drinking(ActionEvent event) {
        waterLabel.setText("Drinking");
    }

    @FXML
    void Electronic(ActionEvent event) {
        wasteLabel.setText("Electronic");
    }

    @FXML
    void Plastic(ActionEvent event) {
        wasteLabel.setText("Plastic"); 
    }

    @FXML
    void Refrigerator(ActionEvent event) {
        electricLabel.setText("Refrigerator"); 
    }

    @FXML
    void Walking(ActionEvent event) {
        distanceLabel.setText("Walking");
    }


    @FXML
    void Washing(ActionEvent event) {
        wasteLabel.setText("Washing");
    }
    
    @FXML
    private void handleSubmit(ActionEvent event) {
        modeBox.valueProperty().set(null);
        elecBox.valueProperty().set(null);
        wasteBox.valueProperty().set(null);
        waterBox.valueProperty().set(null);

        distanceText.clear();
        elecText.clear();
        wasteText.clear();
        waterText.clear();

        
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
