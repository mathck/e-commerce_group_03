package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private final XYChart.Series baselineSeries = new XYChart.Series();
    private final XYChart.Series extensionSeries = new XYChart.Series();

    @FXML
    private void handleStartButtonBaselineAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success, 60%", 60),
                        new PieChart.Data("Fail, 40%", 40));

        baselineSeries.setName("Baseline");
        baselineSeries.getData().add(new XYChart.Data(0, 0));
        baselineSeries.getData().add(new XYChart.Data(1, 14));
        baselineSeries.getData().add(new XYChart.Data(2, 15));

        lineChart.getData().add(baselineSeries);
        baselinePieChart.setData(pieChartData);
    }

    @FXML
    private void handleStartButtonExtensionAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Success, 80%", 80),
                        new PieChart.Data("Fail, 20%", 20));

        extensionSeries.setName("Extension");
        extensionSeries.getData().add(new XYChart.Data(0, 0));
        extensionSeries.getData().add(new XYChart.Data(1, 25));
        extensionSeries.getData().add(new XYChart.Data(2, 45));

        lineChart.getData().add(extensionSeries);
        extensionPieChart.setData(pieChartData);
    }

    @FXML
    private void handleResetButtonBaselineAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        lineChart.getData().removeAll(baselineSeries);
        baselinePieChart.setData(pieChartData);
    }

    @FXML
    private void handleResetButtonExtensionAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        lineChart.getData().removeAll(extensionSeries);
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
