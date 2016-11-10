package model.implementations;

import model.exceptions.Failure;
import model.exceptions.PMFailure;
import model.exceptions.VMFailure;
import model.interfaces.INode;

import java.util.ArrayList;
import java.util.List;

public class DataCenter implements INode {

    private List<PhysicalMachine> physicalMachines;

    // TODO Attribute James

    public DataCenter(int numberOfPhysicalMachines) {
        physicalMachines = new ArrayList<>(numberOfPhysicalMachines);
    }
}
