package model.calculations;

import assets.Settings;
import model.implementations.DataCenter;
import model.implementations.PhysicalMachine;

public class PMCalculation {

    /**
     * Formula 1
     */
    public static double totalEnergyUtilization() {
        return  Settings.basicEnergyUtilization;
    }

    /**
     * Formula 2
     */
    public static double networkConnection(PhysicalMachine physicalMachineSource, PhysicalMachine physicalMachineTarget) {
        return  Settings.basicEnergyUtilization +
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

    /**
     * Formula Bandwidth
     */
    public static int getBandwidth(DataCenter dataCenter1, DataCenter dataCenter2) {
        return Math.abs((dataCenter1.hashCode() + dataCenter2.hashCode()) / 10000000);
    }
}
