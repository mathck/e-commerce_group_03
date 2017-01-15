package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import model.exceptions.JobEvent;
import model.implementations.DataCenter;
import model.implementations.EnergyUtil;
import model.implementations.Grid;
import model.implementations.LatencyPerformance;
import model.interfaces.INode;
import model.utility.MatrixCalculator;

public class GUIController extends GUIWidgets implements Initializable {

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private boolean baseLineEnabled;
    private boolean extensionEnabled;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lineChart.setCreateSymbols(false);

        TimerTask addNewTimelinePoint = new TimerTask() {

            @Override
            public void run() {

                if(baseLineEnabled || extensionEnabled) {
                    plotData();
                }
            }
        };

        scheduler.scheduleAtFixedRate(addNewTimelinePoint, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private int baselineSuccesses = 0;
    private int baselineFailures = 0;
    private int extensionSuccesses = 0;
    private int extensionFailures = 0;
    private int baselineLineChartXCounter = 0;
    private int extensionLineChartXCounter = 0;

    private EnergyUtil baselineUtil = new EnergyUtil();
    private EnergyUtil extensionUtil = new EnergyUtil();

    private LatencyPerformance baselineLatency = new LatencyPerformance();
    private LatencyPerformance extensionLatency = new LatencyPerformance();

    void drawGrid(Grid grid) {

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

    public void addFinished(JobEvent success) {
        if(baseLineEnabled)
            baselineSuccesses++;

        if(extensionEnabled)
            extensionSuccesses++;

        System.out.println("\u001B[32m" + "SUCCESS: " + success.hashCode() + "\u001B[0m");
        int sum = baselineSuccesses+baselineFailures;
        System.out.println("SUM BL: " + sum);
    }

    public void addException(JobEvent failure) {
        if(baseLineEnabled)
            baselineFailures++;

        if(extensionEnabled)
            extensionFailures++;

        System.out.println("\u001B[31m" + "FAILED: " + failure.hashCode() + "\u001B[0m");

        int sum = extensionSuccesses+extensionFailures;
        System.out.println("SUM EX: " + sum);
    }

    public void addLatency(int latencyms) {
        if (baseLineEnabled) {

            baselineLatency.average =
                    ((baselineLatency.average * baselineLatency.counter)
                            + latencyms) / (++baselineLatency.counter);

        }
        else if (extensionEnabled) {
            extensionLatency.average =
                    ((extensionLatency.average * extensionLatency.counter)
                            + latencyms) / (++extensionLatency.counter);
        }
    }

    public void addEnergyUtil(double newValue) {

        if (baseLineEnabled) {

            baselineUtil.average =
                    ((baselineUtil.average * baselineUtil.counter)
                            + newValue) / (++baselineUtil.counter);

        }
        else if (extensionEnabled) {
            extensionUtil.average =
                    ((extensionUtil.average * extensionUtil.counter)
                            + newValue) / (++extensionUtil.counter);
        }
    }

    public void plotData() {
        if(baseLineEnabled) {
            Platform.runLater(() -> baselineLineChart.getData().add(new XYChart.Data(baselineLineChartXCounter++, baselineSuccesses)));
            Platform.runLater(() -> {
                for(PieChart.Data data : baselinePieChart.getData())
                {
                    if(data.getName().startsWith("Success"))
                    {
                        data.setPieValue(baselineSuccesses);
                        data.setName("Success, " + baselineSuccesses);
                    }
                    else{
                        data.setPieValue(baselineFailures);
                        data.setName("Failure, " + baselineFailures);
                    }
                }
            });
            Platform.runLater(() -> {
                baselineUtilTextField.setText(Double.toString(Math.round(baselineUtil.average *100)/100.00));
                baselineLatencyTextField.setText(Double.toString(Math.round(baselineLatency.average *100)/100.00));
            });
        }
        if(extensionEnabled) {
            Platform.runLater(() -> extensionLineChart.getData().add(new XYChart.Data(extensionLineChartXCounter++, extensionSuccesses)));
            Platform.runLater(() -> {
                for(PieChart.Data data : extensionPieChart.getData())
                {
                    if(data.getName().startsWith("Success"))
                    {
                        data.setPieValue(extensionSuccesses);
                        data.setName("Success, " + extensionSuccesses);
                    }
                    else{
                        data.setPieValue(extensionFailures);
                        data.setName("Failure, " + extensionFailures);
                    }
                }
            });
            Platform.runLater(() -> {
                extensionUtilTextField.setText(Double.toString(Math.round(extensionUtil.average *100)/100.00));
                extensionLatencyTextField.setText(Double.toString(Math.round(extensionLatency.average *100)/100.00));
            });
        }
    }

    public void enableBaseline(boolean enable) {
        this.baseLineEnabled = enable;
    }

    public void enableExtension(boolean enable) {
        this.extensionEnabled = enable;
    }
}
