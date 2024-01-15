package com.ecohub;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class DashboardController {

    @FXML
    private VBox chartView, show1, show2, show3, show4;

    @FXML
    private HBox parent;

    @FXML
    private StackPane rightPane;

    
    @FXML
    void showCarbon(MouseEvent event) {
        styleBox(0);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Carbon Footprint");
        
        LineChart<String, Number> carbonFoorprint = new LineChart<String, Number>(xAxis, yAxis);
        carbonFoorprint.setTitle("Daily Carbon Footprint");
        
        XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
        data.setName("Total Carbon Footprint");
        
        data.getData().add(new XYChart.Data<String, Number>("14/1", 200));
        data.getData().add(new XYChart.Data<String, Number>("15/1", 400));
        data.getData().add(new XYChart.Data<String, Number>("16/1", 100));
        
        carbonFoorprint.getData().add(data);
        carbonFoorprint.setLegendVisible(false);
        
        chartView.getChildren().clear();
        chartView.getChildren().add(carbonFoorprint);
    }

    @FXML
    void showBreak(MouseEvent event) {
        styleBox(1);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Fuel", 3000),
            new PieChart.Data("Electricity", 1500),
            new PieChart.Data("Water", 1020),
            new PieChart.Data("Waste", 2030)
        );

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Carbon Footprint Break Down");
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);

        chartView.getChildren().clear();
        chartView.getChildren().add(pieChart);
    }

    @FXML
    void showTravel(MouseEvent event) {
        styleBox(2);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Traveled Distance");
        
        LineChart<String, Number> distanceTravel = new LineChart<String, Number>(xAxis, yAxis);
        distanceTravel.setTitle("Daily Traveled Distance");
        
        XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
        data.setName("Total Traveled Distance");
        
        data.getData().add(new XYChart.Data<String, Number>("14/1", 1200));
        data.getData().add(new XYChart.Data<String, Number>("15/1", 403));
        data.getData().add(new XYChart.Data<String, Number>("16/1", 830));
        
        distanceTravel.getData().add(data);
        distanceTravel.setLegendVisible(false);
        
        chartView.getChildren().clear();
        chartView.getChildren().add(distanceTravel);
    }

    @FXML
    void showElectric(MouseEvent event) {
        styleBox(3);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Electric Usage");
        
        BarChart<String, Number> electricUsage = new BarChart<String, Number>(xAxis, yAxis);
        electricUsage.setTitle("Daily Electric Usage");
        
        XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
        data.setName("Total Carbon Footprint");
        
        data.getData().add(new XYChart.Data<String, Number>("14/1", 200));
        data.getData().add(new XYChart.Data<String, Number>("15/1", 400));
        data.getData().add(new XYChart.Data<String, Number>("16/1", 100));
        
        electricUsage.getData().add(data);
        electricUsage.setLegendVisible(false);
        
        chartView.getChildren().clear();
        chartView.getChildren().add(electricUsage);
    }


    private void styleBox(int index) {
        // This function change the style+color of the menu (Menu Item Selected)
        show1.setStyle("-fx-background-color: white;");
        for (javafx.scene.Node node : show1.getChildren()) {
            if (node instanceof Label) {
                // Modify the style of the label here
                Label label = (Label) node;
                label.setStyle("-fx-text-fill: black;");
            }
        }
        show2.setStyle("-fx-background-color: white;");
        for (javafx.scene.Node node : show2.getChildren()) {
            if (node instanceof Label) {
                // Modify the style of the label here
                Label label = (Label) node;
                label.setStyle("-fx-text-fill: black;");
            }
        }
        show3.setStyle("-fx-background-color: white;");
        for (javafx.scene.Node node : show3.getChildren()) {
            if (node instanceof Label) {
                // Modify the style of the label here
                Label label = (Label) node;
                label.setStyle("-fx-text-fill: black;");
            }
        }
        show4.setStyle("-fx-background-color: white;");
        for (javafx.scene.Node node : show4.getChildren()) {
            if (node instanceof Label) {
                // Modify the style of the label here
                Label label = (Label) node;
                label.setStyle("-fx-text-fill: black;");
            }
        }

        switch (index) {
            case 0:
                show1.setStyle("-fx-background-color: #10c474;");
                for (javafx.scene.Node node : show1.getChildren()) {
                    if (node instanceof Label) {
                        // Modify the style of the label here
                        Label label = (Label) node;
                        label.setStyle("-fx-text-fill: white;");
                    }
                }
                break;
            case 1:
                show2.setStyle("-fx-background-color: #10c474;");
                for (javafx.scene.Node node : show2.getChildren()) {
                    if (node instanceof Label) {
                        // Modify the style of the label here
                        Label label = (Label) node;
                        label.setStyle("-fx-text-fill: white;");
                    }
                }
                break;
            case 2:
                show3.setStyle("-fx-background-color: #10c474;");
                for (javafx.scene.Node node : show3.getChildren()) {
                    if (node instanceof Label) {
                        // Modify the style of the label here
                        Label label = (Label) node;
                        label.setStyle("-fx-text-fill: white;");
                    }
                }
                break;
            case 3:
                show4.setStyle("-fx-background-color: #10c474;");
                for (javafx.scene.Node node : show4.getChildren()) {
                    if (node instanceof Label) {
                        // Modify the style of the label here
                        Label label = (Label) node;
                        label.setStyle("-fx-text-fill: white;");
                    }
                }
                break;
        }
    }

}
