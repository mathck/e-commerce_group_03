package model.calculations;

import model.implementations.PhysicalMachine;

public class PMCalculation {

    private static final double basicEnergyUtilization = 5;

    /**
     * Formula 1
     */
    public static double totalEnergyUtilization() {
        return  basicEnergyUtilization;
        // wait on james to finish his part
    }

    /**
     * Formula 2
     */
    public static double networkConnection(PhysicalMachine physicalMachineSource, PhysicalMachine physicalMachineTarget) {
        return  basicEnergyUtilization +
                physicalMachineSource.getEnergyUtilization() +
                physicalMachineTarget.getEnergyUtilization();
    }

    /**
     * Formula Manhattan distance
     */
    public static int manhattanDistance(int sourceX, int sourceY, int targetX, int targetY) {
        return  Math.abs(sourceX - targetX) +
                Math.abs(sourceY - targetY);
    }
}
