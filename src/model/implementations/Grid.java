package model.implementations;

import assets.Settings;
import javafx.scene.chart.PieChart;
import javafx.util.Pair;
import model.calculations.PMCalculation;
import model.interfaces.INode;
import model.utility.RandomNumber;

import java.util.ArrayList;

public class Grid {
    private INode[][] Nodes;
    private int dimension;

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

    public ArrayList<DataCenter> getDataCenters() {
        ArrayList<DataCenter> dataCenters = new ArrayList<>();

        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if(Nodes[row][col] instanceof DataCenter)
                    dataCenters.add((DataCenter) Nodes[row][col]);

        return dataCenters;
    }

    public DataCenter getNextBest() {
        DataCenter currentBest = null;

        for(DataCenter dataCenter : getDataCenters()) {
            if (dataCenter.hasFreePM()) {
                if (currentBest == null)
                    currentBest = dataCenter;

                if (dataCenter.getUtilTotal() < currentBest.getUtilTotal())
                    currentBest = dataCenter;
            }
        }

        return currentBest;
    }

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
