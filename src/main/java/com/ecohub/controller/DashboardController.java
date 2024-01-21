package com.ecohub.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import com.ecohub.dao.RecordDAO;
import com.ecohub.session.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardController {
    
    @FXML
    private LineChart<String, Number> carbonChart;
    
    @FXML
    private PieChart breakChart;
    
    @FXML
    private HBox parent;
    
    @FXML
    private VBox linePane, piePane;
    
    @FXML
    private StackPane rightPane;
    
    @FXML
    private Label carbonData, electricData, distanceData;
    
    @FXML
    private ComboBox<String> lineBox;

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
    void updateChart() {
        showCarbon();
        showBreak();
    }
    
    @FXML
    void updateLabel() {
        RecordDAO recordDAO = new RecordDAO();
        try {
            BigDecimal total = recordDAO.getTotal(UserSession.getInstance().getUserId());
            if (total == null) {
                carbonData.setText("0");
            }
            else {
                carbonData.setText(String.valueOf(total));
            }
            total = recordDAO.getCategory(UserSession.getInstance().getUserId(), 2);
            if (total == null) {
                electricData.setText("0");
            }
            else {
                electricData.setText(String.valueOf(total));
            }
            total = recordDAO.getCategory(UserSession.getInstance().getUserId(), 1);
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
            String[][] data = recordDAO.getRecent(UserSession.getInstance().getUserId()); 

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName("Total Carbon Footprint");
            
            if (data == null) {
                linePane.getChildren().removeIf(node -> node instanceof LineChart);

                boolean stackPaneExists = linePane.getChildren().stream()
                    .filter(StackPane.class::isInstance)
                    .anyMatch(stackPane -> {
                            Node node = ((StackPane) stackPane).getChildren().get(0);
                            return node instanceof Label && "You have no data yet".equals(((Label) node).getText());
                        }
                    );

                if (!stackPaneExists) {
                    Label errorLabel = new Label("You have no data yet");
                    errorLabel.getStyleClass().add("error-label");
                    StackPane stackPane = new StackPane(errorLabel); // Create a new StackPane for the label.
                    stackPane.setAlignment(Pos.BOTTOM_CENTER); // Set the alignment of the StackPane to CENTER.
                    linePane.getChildren().add(stackPane);
                }
            }
            else {
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
            }
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
            String[][] data = recordDAO.getRecentYear(UserSession.getInstance().getUserId()); 

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName("Total Carbon Footprint");
            
            if (data == null) {
                linePane.getChildren().removeIf(node -> node instanceof LineChart);

                boolean stackPaneExists = linePane.getChildren().stream()
                    .filter(StackPane.class::isInstance)
                    .anyMatch(stackPane -> {
                            Node node = ((StackPane) stackPane).getChildren().get(0);
                            return node instanceof Label && "You have no data yet".equals(((Label) node).getText());
                        }
                    );

                if (!stackPaneExists) {
                    Label errorLabel = new Label("You have no data yet");
                    errorLabel.getStyleClass().add("error-label");
                    StackPane stackPane = new StackPane(errorLabel); // Create a new StackPane for the label.
                    stackPane.setAlignment(Pos.BOTTOM_CENTER); // Set the alignment of the StackPane to CENTER.
                    linePane.getChildren().add(stackPane);
                }
            }
            else {// Sort the data array in ascending order
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void showBreak() {
        RecordDAO recordDAO = new RecordDAO();
        List<String[]> data = null;
        PieChart breakChart = new PieChart();

        try {
            data = recordDAO.getPercentage(UserSession.getInstance().getUserId());
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

            // Add the chart back to its parent container
            piePane.getChildren().add(breakChart);
        } 
        else {
            boolean stackPaneExists = piePane.getChildren().stream()
                .filter(StackPane.class::isInstance)
                .anyMatch(stackPane -> {
                        Node node = ((StackPane) stackPane).getChildren().get(0);
                        return node instanceof Label && "You have no data yet".equals(((Label) node).getText());
                    }
                );

            if (!stackPaneExists) {
                Label errorLabel = new Label("You have no data yet");
                errorLabel.getStyleClass().add("error-label");
                StackPane stackPane = new StackPane(errorLabel); // Create a new StackPane for the label.
                stackPane.setAlignment(Pos.BOTTOM_CENTER); // Set the alignment of the StackPane to BOTTOM_CENTER.
                piePane.getChildren().add(stackPane);
            }
        }
    }
    
    @FXML
    void initialize() {
        initBox();
        if (UserSession.getInstance().getUsername() != null) {
            updateLabel();
            updateChart();
        }
    }
}
