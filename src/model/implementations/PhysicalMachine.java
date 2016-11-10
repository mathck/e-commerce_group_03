package model.implementations;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMachine {

    private List<VirtualMachine> virtualMachines;

    private double energyUtilization;

    // TODO Attribute James

    public PhysicalMachine(int numberOfVirtualMachines) {
        virtualMachines = new ArrayList<>(numberOfVirtualMachines);
    }

    public double getEnergyUtilization() {
        return energyUtilization;
    }

    private void setEnergyUtilization(double energyUtilization) {
        this.energyUtilization = energyUtilization;
    }
}
