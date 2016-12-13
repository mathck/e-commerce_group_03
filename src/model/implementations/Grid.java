package model.implementations;

import assets.Settings;
import javafx.scene.chart.PieChart;
import javafx.util.Pair;
import model.calculations.PMCalculation;
import model.exceptions.NoAvailableDataCenter;
import model.interfaces.INode;
import model.utility.RandomNumber;

import java.util.ArrayList;

public class Grid {
    private INode[][] Nodes;
    private int dimension;

    /*
    Creates a grid and places the DCs on random positions in the grid-matrix.
     */
    public Grid(int numberDataCenters, int dimension) {
        this.dimension = dimension;
        Nodes = new INode[dimension][dimension];

        ArrayList<Pair<Integer, Integer>> randomDataCenterPositions =
                RandomNumber.nextIntPairUnique(numberDataCenters, 0, dimension - 1);

        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if(randomDataCenterPositions.contains(new Pair<>(row, col))) {
                    DataCenter dc = new DataCenter(RandomNumber.nextGaussian(Settings.numberOfPhysicalMachinesPerDC));
                    dc.setLocationX(row);
                    dc.setLocationY(col);
                    Nodes[row][col] = dc;
                }
                else
                    Nodes[row][col] = new EmptyNode();
    }

    public INode[][] getNodes() {
        return Nodes;
    }

    /*
    Returns all DCs to an ArrayList.
     */
    public ArrayList<DataCenter> getDataCenters() {
        ArrayList<DataCenter> dataCenters = new ArrayList<>();

        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if(Nodes[row][col] instanceof DataCenter)
                    dataCenters.add((DataCenter) Nodes[row][col]);

        return dataCenters;
    }

    /*
    Returns the average utilization of all DCs by summing up the total utilization of all DCs and dividing by the number of DCs.
     */
    public double getUtilAverage() {
        double sum = 0;
        int dataCenterCounter = 0;

        for(DataCenter dataCenter : getDataCenters()) {
            sum += dataCenter.getUtilTotal();
            dataCenterCounter++;
        }

        return sum / dataCenterCounter;
    }

    /*
    Returns the next best DC by applying the following algorithm:
    Iterate through all DCs. If a DC has a free PM, calculate the total utilization.
    If the new utilization is better than the current best utilization, the new DC is the next best DC.
    If no DC is available, the Job can not be executed.
     */
    public DataCenter getNextBest() throws NoAvailableDataCenter {
        DataCenter currentBest = null;

        for(DataCenter dataCenter : getDataCenters()) {
            if (dataCenter.hasFreePM()) {
                if (currentBest == null)
                    currentBest = dataCenter;

                if (dataCenter.getUtilTotal() < currentBest.getUtilTotal())
                    currentBest = dataCenter;
            }
        }

        if(currentBest == null)
            throw new NoAvailableDataCenter();
        else
            return currentBest;
    }

    /*

     */
    @Deprecated
    void printBandwidthMatrix() {
        ArrayList<DataCenter> dataCenters1 = getDataCenters();
        for (int i1 = 0; i1 < dataCenters1.size(); i1++) {
            DataCenter dataCenter = dataCenters1.get(i1);
            ArrayList<DataCenter> dataCenters = getDataCenters();
            for (int i = 0; i < dataCenters.size(); i++) {
                DataCenter innerDataCenter = dataCenters.get(i);
                System.out.println("from " + i + " to " + i1 + ": " + PMCalculation.getBandwidth(dataCenter, innerDataCenter) + "ms");
            }

        }
    }
}
