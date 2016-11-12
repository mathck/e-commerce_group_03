package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class UIController implements Initializable {

    @FXML
    private PieChart baselinePieChart;
    @FXML
    private PieChart extensionPieChart;
    @FXML
    private LineChart lineChart;
    @FXML
    private BubbleChart bubbleChart;

    private XYChart.Series baselineLineChart = new XYChart.Series();
    private XYChart.Series extensionLineChart = new XYChart.Series();
    private XYChart.Series bubbleChartGrid = new XYChart.Series();

    @FXML
    private void handleStartButtonBaselineAction(ActionEvent event) throws InterruptedException {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success, 60%", 60),
                        new PieChart.Data("Fail, 40%", 40));

        baselineLineChart.setName("Baseline");
        baselineLineChart.getData().add(new XYChart.Data(0, 0));
        baselineLineChart.getData().add(new XYChart.Data(1, 14));
        baselineLineChart.getData().add(new XYChart.Data(2, 15));

        bubbleChartGrid.getData().add(new XYChart.Data(1, 2, 0.1));
        bubbleChartGrid.getData().add(new XYChart.Data(2, 1, 0.15));

        bubbleChart.getData().add(bubbleChartGrid);

        lineChart.getData().add(baselineLineChart);
        baselinePieChart.setData(pieChartData);
    }

    @FXML
    private void handleStartButtonExtensionAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success, 80%", 80),
                        new PieChart.Data("Fail, 20%", 20));

        extensionLineChart.setName("Extension");
        extensionLineChart.getData().add(new XYChart.Data(0, 0));
        extensionLineChart.getData().add(new XYChart.Data(1, 25));
        extensionLineChart.getData().add(new XYChart.Data(2, 45));

        bubbleChartGrid.getData().add(new XYChart.Data(1, 1, 1));
        bubbleChartGrid.getData().add(new XYChart.Data(1, 2, 2));
        bubbleChartGrid.getData().add(new XYChart.Data(2, 1, 2));
        bubbleChartGrid.getData().add(new XYChart.Data(5, 5, 5));

        bubbleChart.getData().add(bubbleChartGrid);
        lineChart.getData().add(extensionLineChart);
        extensionPieChart.setData(pieChartData);
    }

    @FXML
    private void handleResetButtonBaselineAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        lineChart.getData().removeAll(baselineLineChart);
        baselinePieChart.setData(pieChartData);
    }

    @FXML
    private void handleResetButtonExtensionAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        lineChart.getData().removeAll(extensionLineChart);
        extensionPieChart.setData(pieChartData);
    }

    @FXML
    private void handleStopButtonBaselineAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        baselinePieChart.setData(pieChartData);
    }

    @FXML
    private void handleStopButtonExtensionAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        extensionPieChart.setData(pieChartData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // not implemented
    }
}
