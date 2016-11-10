package model.implementations;

import model.interfaces.INode;

public class Grid {
    private INode[][] Nodes;

    public Grid(int numberDataCenters, int dimension) {
        Nodes = new INode[dimension][dimension];

        // numberDataCenters dataCenters, rest ist EmptyNode TODO Michi
        // random verteilen: TODO Michi
    }
}
