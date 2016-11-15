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
import logic.BaselineJobEngine;
import logic.ExtendedJobEngine;

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

    XYChart.Series baselineLineChart = new XYChart.Series();
    XYChart.Series extensionLineChart = new XYChart.Series();
    XYChart.Series bubbleChartGrid = new XYChart.Series();

    void drawPieCharts() {
        ObservableList<PieChart.Data> pieChartDataBaseline =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success, 50%", 1),
                        new PieChart.Data("Failure, 50%", 1));

        ObservableList<PieChart.Data> pieChartDataExtension =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success, 50%", 1),
                        new PieChart.Data("Failure, 50%", 1));

        baselinePieChart.setData(pieChartDataBaseline);
        extensionPieChart.setData(pieChartDataExtension);
    }


    @FXML
    private void handleStartButtonBaselineAction(ActionEvent event) throws InterruptedException {
        startButtonBaseline.setDisable(true);
        stopButtonBaseline.setDisable(false);
        resetButtonBaseline.setDisable(true);
        startButtonExtension.setDisable(true);

        baselineLineChart.setName("Baseline");
        lineChart.getData().add(baselineLineChart);

        BaselineJobEngine.getInstance().run((GUIController) this);
    }

    @FXML
    private void handleStartButtonExtensionAction(ActionEvent event) {
        startButtonExtension.setDisable(true);
        stopButtonExtension.setDisable(false);
        resetButtonExtension.setDisable(true);
        startButtonBaseline.setDisable(true);

        extensionLineChart.setName("Extension");
        lineChart.getData().add(extensionLineChart);

        ExtendedJobEngine.getInstance().run((GUIController) this);
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
        startButtonExtension.setDisable(false);

        BaselineJobEngine.getInstance().stop((GUIController) this);
    }

    @FXML
    private void handleStopButtonExtensionAction(ActionEvent event) {
        startButtonExtension.setDisable(false);
        stopButtonExtension.setDisable(true);
        resetButtonExtension.setDisable(false);
        startButtonBaseline.setDisable(false);

        ExtendedJobEngine.getInstance().stop((GUIController) this);
    }
}
