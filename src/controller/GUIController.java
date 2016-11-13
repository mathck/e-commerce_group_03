package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import model.exceptions.JobEvent;
import model.implementations.DataCenter;
import model.implementations.Grid;
import model.interfaces.INode;
import model.utility.MatrixCalculator;
import model.utility.RandomNumber;

public class GUIController implements Initializable {

    @FXML
    private PieChart baselinePieChart;
    @FXML
    private PieChart extensionPieChart;
    @FXML
    private LineChart lineChart;
    @FXML
    private BubbleChart bubbleChart;
    @FXML
    private Button startButtonBaseline;
    @FXML
    private Button stopButtonBaseline;
    @FXML
    private Button resetButtonBaseline;
    @FXML
    private Button startButtonExtension;
    @FXML
    private Button stopButtonExtension;
    @FXML
    private Button resetButtonExtension;

    private XYChart.Series baselineLineChart = new XYChart.Series();
    private XYChart.Series extensionLineChart = new XYChart.Series();
    private XYChart.Series bubbleChartGrid = new XYChart.Series();

    private int successes = 0;
    private int failures = 0;
    private int counter = 0;

    public void DrawGrid(Grid grid) {

        INode[][] nodes = grid.getNodes().clone();
        MatrixCalculator.rotateInPlace90DegreesClockwise(nodes);

        int dimension = nodes[0].length;

        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if(nodes[row][col] instanceof DataCenter)
                    bubbleChartGrid.getData().add(new XYChart.Data(row, col, 0.25));

        bubbleChart.getData().add(bubbleChartGrid);

        NumberAxis xAxis = (NumberAxis) bubbleChartGrid.getChart().getXAxis();
        NumberAxis yAxis = (NumberAxis) bubbleChartGrid.getChart().getYAxis();

        xAxis.setTickLabelsVisible(false);
        yAxis.setTickLabelsVisible(false);
    }



    public void addDataPoint() {
        try {
            baselineLineChart.getData().add(new XYChart.Data(counter, RandomNumber.nextInt(0, 10)));
            extensionLineChart.getData().add(new XYChart.Data(counter, RandomNumber.nextInt(0, 10)));
            counter++;
        }
        catch (Exception e) {
            // THIS HACK "to demonstrate live data addition"
            // sometimes runs into concurrency failures

            // IGNORE this bug, as this wont be our solution
        }
    }

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
    }

    @FXML
    private void handleStopButtonExtensionAction(ActionEvent event) {
        startButtonExtension.setDisable(false);
        stopButtonExtension.setDisable(true);
        resetButtonExtension.setDisable(false);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        extensionPieChart.setData(pieChartData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // not implemented
    }

    public void addFinished(JobEvent event) {
        successes++;
        System.out.println("SUCCESS");
    }

    public void addException(JobEvent failure) {
        failures++;
        System.out.println("FAILED");
    }

    public void plotData() {
        if(baseLineEnabled()) {
            Platform.runLater(() -> baselineLineChart.getData().add(new XYChart.Data(counter++, successes)));
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    for(PieChart.Data d : baselinePieChart.getData())
                    {
                        if(d.getName().startsWith("Success"))
                        {
                            d.setPieValue(successes);
                            d.setName("Success, " + successes);
                        }
                        else{
                            d.setPieValue(failures);
                            d.setName("Failure, " + failures);
                        }
                    }
                }
            });
        }
        if(extensionEnabled()) {
            Platform.runLater(() -> extensionLineChart.getData().add(new XYChart.Data(counter++, successes)));
        }
        System.out.println("SUCCESS");
    }

    public boolean baseLineEnabled(){
        return startButtonBaseline.isDisabled();
    }

    public boolean extensionEnabled(){
        return startButtonExtension.isDisabled();
    }
}
