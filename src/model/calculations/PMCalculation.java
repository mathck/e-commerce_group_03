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
                physicalMachineSource.getUtilTotal() +
                physicalMachineTarget.getUtilTotal();
    }

    /**
     * Formula Manhattan distance
     */
    public static int manhattanDistance(int sourceX, int sourceY, int targetX, int targetY) {
        return  Math.abs(sourceX - targetX) +
                Math.abs(sourceY - targetY);
    }

    /**
     * Formula Manhattan distance * 30
     */
    public static int getLatency(int sourceX, int sourceY, int targetX, int targetY) {
        return  manhattanDistance(sourceX, sourceY, targetX, targetY) * 30;
    }
}
