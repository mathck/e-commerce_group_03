package model.implementations;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMachine {

    private List<VirtualMachine> virtualMachines;

    // TODO Attribute James

    public PhysicalMachine(int numberOfVirtualMachines) {
        virtualMachines = new ArrayList<>(numberOfVirtualMachines);
    }
}
