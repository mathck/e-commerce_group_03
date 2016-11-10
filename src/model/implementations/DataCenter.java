package model.implementations;

import model.interfaces.INode;

import java.util.ArrayList;
import java.util.List;

class DataCenter implements INode {

    private List<PhysicalMachine> physicalMachines;

    // TODO Attribute James

    DataCenter(int numberOfPhysicalMachines) {
        physicalMachines = new ArrayList<>(numberOfPhysicalMachines);
    }

    @Override
    public String toString() {
        return "1";
    }
}
