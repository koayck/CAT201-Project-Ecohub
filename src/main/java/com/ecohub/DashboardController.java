package com.ecohub;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ecohub.dao.RecordDAO;
import com.ecohub.models.User;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class DashboardController {

    private User user;

    private boolean isUserInitialized = false;

    public void initUser(User user) {
        this.user = user;
        this.isUserInitialized = true;
        updateLabel();
        updateChart();
    }

    public void initBox() {
        lineBox.getItems().addAll("Daily Carbon Footprint (Recent 7 days)", "Monthly Carbon Footprint (Recent 12 months)");
        lineBox.getSelectionModel().selectFirst();
        lineBox.setOnAction(event -> {
            String selectedChoice = lineBox.getValue();
            switch (selectedChoice) {
                case "Daily Carbon Footprint (Recent 7 days)":
                    showCarbon();
                    break;
                case "Monthly Carbon Footprint (Recent 12 months)":
                    showCarbonYear();
            }
        });
    }
    
    @FXML
    private LineChart<String, Number> carbonChart;
    
    @FXML
    private PieChart breakChart;
    
    @FXML
    private HBox parent;

    @FXML
    private VBox linePane;
    
    @FXML
    private StackPane rightPane;
    
    @FXML
    private Label carbonData, electricData, distanceData;

    @FXML
    private ComboBox<String> lineBox;
    
    @FXML
    void updateChart() {
        showCarbon();
        showBreak();
    }
    
    @FXML
    void updateLabel() {
        RecordDAO recordDAO = new RecordDAO();
        try {
            BigDecimal total = recordDAO.getTotal(user.getUser_id());
            if (total == null) {
                carbonData.setText("0");
            }
            else {
                carbonData.setText(String.valueOf(total));
            }
            total = recordDAO.getCategory(user.getUser_id(), 2);
            if (total == null) {
                electricData.setText("0");
            }
            else {
                electricData.setText(String.valueOf(total));
            }
            total = recordDAO.getCategory(user.getUser_id(), 1);
            if (total == null) {
                distanceData.setText("0");
            }
            else {
                distanceData.setText(String.valueOf(total));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void showCarbon() {
        RecordDAO recordDAO = new RecordDAO();

        // Create a new CategoryAxis each time the data changes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Carbon Footprint");

        // Create a new LineChart each time the data changes
        LineChart<String, Number> carbonChart = new LineChart<>(xAxis, yAxis);

        try {
            String[][] data = recordDAO.getRecent(user.getUser_id()); 

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName("Total Carbon Footprint");
            
            for (String[] point : data) {
                String dateAsString = point[0];
                double total = Double.parseDouble(point[1]);

                series.getData().add(new XYChart.Data<String, Number>(dateAsString, total));
            }
            
            carbonChart.getData().add(series);
            carbonChart.setLegendVisible(false);

            // Remove only LineChart instances from linePane
            linePane.getChildren().removeIf(node -> node instanceof LineChart);

            // Add the chart back to its parent container
            linePane.getChildren().add(carbonChart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showCarbonYear() {
        RecordDAO recordDAO = new RecordDAO();

        // Create a new CategoryAxis each time the data changes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Carbon Footprint");

        // Create a new LineChart each time the data changes
        LineChart<String, Number> carbonChart = new LineChart<>(xAxis, yAxis);

        try {
            String[][] data = recordDAO.getRecentYear(user.getUser_id()); 

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName("Total Carbon Footprint");
            
            // Sort the data array in ascending order
            Arrays.sort(data, (a, b) -> a[0].compareTo(b[0]));

            for (String[] point : data) {
                String monthAsString = point[0];
                double total = Double.parseDouble(point[1]);

                series.getData().add(new XYChart.Data<String, Number>(monthAsString, total));
            }
            
            carbonChart.getData().add(series);
            carbonChart.setLegendVisible(false);

            // Remove only LineChart instances from linePane
            linePane.getChildren().removeIf(node -> node instanceof LineChart);

            // Add the chart back to its parent container
            linePane.getChildren().add(carbonChart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void showBreak() {
        RecordDAO recordDAO = new RecordDAO();
        List<String[]> data = null;

        try {
            data = recordDAO.getPercentage(user.getUser_id());
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
            breakChart.setStartAngle(180);
        }
    }
    
    @FXML
    void initialize() {
        initBox();
        if (isUserInitialized) {
            updateLabel();
            updateChart();
        }
    }
}
