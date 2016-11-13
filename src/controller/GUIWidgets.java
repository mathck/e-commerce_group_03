package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import logic.JobEngine;

class GUIWidgets {

    @FXML
    protected PieChart baselinePieChart;
    @FXML
    protected PieChart extensionPieChart;
    @FXML
    protected LineChart lineChart;
    @FXML
    protected BubbleChart bubbleChart;
    @FXML
    protected Button startButtonBaseline;
    @FXML
    protected Button stopButtonBaseline;
    @FXML
    protected Button resetButtonBaseline;
    @FXML
    protected Button startButtonExtension;
    @FXML
    protected Button stopButtonExtension;
    @FXML
    protected Button resetButtonExtension;

    protected XYChart.Series baselineLineChart = new XYChart.Series();
    protected XYChart.Series extensionLineChart = new XYChart.Series();
    protected XYChart.Series bubbleChartGrid = new XYChart.Series();

    @FXML
    private void handleStartButtonBaselineAction(ActionEvent event) throws InterruptedException {
        startButtonBaseline.setDisable(true);
        stopButtonBaseline.setDisable(false);
        resetButtonBaseline.setDisable(true);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success, 50%", 1),
                        new PieChart.Data("Failure, 50%", 1));

        baselinePieChart.setData(pieChartData);

        baselineLineChart.setName("Baseline");
        lineChart.getData().add(baselineLineChart);

        JobEngine.getInstance().run();
    }

    @FXML
    private void handleStartButtonExtensionAction(ActionEvent event) {
        startButtonExtension.setDisable(true);
        stopButtonExtension.setDisable(false);
        resetButtonExtension.setDisable(true);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success", 0),
                        new PieChart.Data("Failure", 0));

        extensionPieChart.setData(pieChartData);

        extensionLineChart.setName("Extension");
        lineChart.getData().add(extensionLineChart);

        JobEngine.getInstance().run();
    }

    @FXML
    private void handleResetButtonBaselineAction(ActionEvent event) {
        startButtonBaseline.setDisable(false);
        stopButtonBaseline.setDisable(true);
        resetButtonBaseline.setDisable(true);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        lineChart.getData().removeAll(baselineLineChart);
        baselinePieChart.setData(pieChartData);
    }

    @FXML
    private void handleResetButtonExtensionAction(ActionEvent event) {
        startButtonExtension.setDisable(false);
        stopButtonExtension.setDisable(true);
        resetButtonExtension.setDisable(true);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        lineChart.getData().removeAll(extensionLineChart);
        extensionPieChart.setData(pieChartData);
    }

    @FXML
    private void handleStopButtonBaselineAction(ActionEvent event) {
        startButtonBaseline.setDisable(false);
        stopButtonBaseline.setDisable(true);
        resetButtonBaseline.setDisable(false);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        baselinePieChart.setData(pieChartData);

        JobEngine.getInstance().stop();
    }

    @FXML
    private void handleStopButtonExtensionAction(ActionEvent event) {
        startButtonExtension.setDisable(false);
        stopButtonExtension.setDisable(true);
        resetButtonExtension.setDisable(false);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        extensionPieChart.setData(pieChartData);

        JobEngine.getInstance().stop();
    }
}