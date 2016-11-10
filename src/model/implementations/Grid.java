package model.implementations;

import javafx.util.Pair;
import model.interfaces.INode;
import model.utility.RandomNumber;

import java.util.ArrayList;

public class Grid {
    private INode[][] Nodes;

    public Grid(int numberDataCenters, int dimension) {
        Nodes = new INode[dimension][dimension];

        ArrayList<Pair<Integer, Integer>> randomDataCenterPositions =
                RandomNumber.nextIntPairUnique(numberDataCenters, 0, dimension - 1);

        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if(randomDataCenterPositions.contains(new Pair<>(row, col)))
                    Nodes[row][col] = new DataCenter(RandomNumber.nextGaussian(40));
                else
                    Nodes[row][col] = new EmptyNode();
    }

    public INode[][] getNodes() {
        return Nodes;
    }
}
