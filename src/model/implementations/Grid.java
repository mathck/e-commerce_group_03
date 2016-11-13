package model.implementations;

import javafx.util.Pair;
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
                    DataCenter dc = new DataCenter(RandomNumber.nextGaussian(40));
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
}
