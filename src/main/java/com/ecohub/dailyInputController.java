package com.ecohub;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

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
        submitLabel.setText("Submitted Sucessfully!");
    }

    @FXML
    void initialize() {

    }

}
