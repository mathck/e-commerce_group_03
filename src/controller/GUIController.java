package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import model.exceptions.JobEvent;
import model.implementations.DataCenter;
import model.implementations.Grid;
import model.interfaces.INode;
import model.utility.MatrixCalculator;

public class GUIController extends GUIWidgets implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    private int successes = 0;
    private int failures = 0;
    private int lineChartXCounter = 0;

    private ArrayList<Integer> latencys = new ArrayList<>();

    void DrawGrid(Grid grid) {

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

    public void addFinished(JobEvent event) {
        successes++;
        System.out.println("SUCCESS: " + event.hashCode());
    }

    public void addException(JobEvent failure) {
        failures++;
        System.out.println("FAILED: " + failure.hashCode());
    }

    public void addLatency(int latencyms) {
        latencys.add(latencyms);
    }

    public void plotData() {
        if(baseLineEnabled()) {
            Platform.runLater(() -> baselineLineChart.getData().add(new XYChart.Data(lineChartXCounter++, successes)));
            Platform.runLater(() -> {
                for(PieChart.Data data : baselinePieChart.getData())
                {
                    if(data.getName().startsWith("Success"))
                    {
                        data.setPieValue(successes);
                        data.setName("Success, " + successes);
                    }
                    else{
                        data.setPieValue(failures);
                        data.setName("Failure, " + failures);
                    }
                }
            });
        }
        if(extensionEnabled()) {
            Platform.runLater(() -> extensionLineChart.getData().add(new XYChart.Data(lineChartXCounter++, successes)));
        }
    }

    private boolean baseLineEnabled(){
        return startButtonBaseline.isDisabled();
    }

    private boolean extensionEnabled(){
        return startButtonExtension.isDisabled();
    }
}
