package com.ecohub;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    void showBreak(MouseEvent event) {

    }

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
    void showElectric(MouseEvent event) {

    }

    @FXML
    void showTravel(MouseEvent event) {

    }

    private void styleBox(int index) {
        // This function change the style+color of the menu (Menu Item Selected)

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
                show2.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                break;
            case 2:
                show3.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                break;
            case 3:
                show4.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                break;
        }
    }

}
