package com.ecohub;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.ecohub.dao.RecordDAO;

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
    private LineChart<String, Number> carbonChart;
    
    @FXML
    private PieChart breakChart;
    
    @FXML
    private HBox parent;
    
    @FXML
    private StackPane rightPane;
    
    @FXML
    private Label carbonData, electricData, distanceData;
    
    @FXML
    void updateChart() {
        carbonChart.getData().clear(); // clear the old data
        showCarbon(); // add the new data
        showBreak();
    }
    
    @FXML
    void updateLabel() {
        RecordDAO recordDAO = new RecordDAO();
        try {
            BigDecimal total = recordDAO.getTotal(1);
            carbonData.setText(String.valueOf(total));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void showCarbon() {
        RecordDAO recordDAO = new RecordDAO();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Carbon Footprint");

        try {
            String[][] data = recordDAO.getRecent(1); 

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName("Total Carbon Footprint");
            
            for (String[] point : data) {
                String dateAsString = point[0];
                double total = Double.parseDouble(point[1]);

                series.getData().add(new XYChart.Data<String, Number>(dateAsString, total));
            }
            
            carbonChart.getData().add(series);
            carbonChart.setLegendVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void showBreak() {
        RecordDAO recordDAO = new RecordDAO();
        List<String[]> data = null;

        try {
            data = recordDAO.getPercentage();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately for your application
        }

        if (data != null) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (String[] item : data) {
                String category = item[0];
                double percentage = Double.parseDouble(item[1]);
                pieChartData.add(new PieChart.Data(category, percentage));
            }

            breakChart.setData(pieChartData);
            breakChart.setClockwise(true);
            breakChart.setLabelLineLength(50);
            breakChart.setLabelsVisible(true);
            breakChart.setStartAngle(180);
        }
    }
    

    @FXML
    void initialize() {
        updateLabel();
        updateChart();
    }
}
