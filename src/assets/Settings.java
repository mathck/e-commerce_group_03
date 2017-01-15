package assets;

/**
 * settings for the algorithms
 */

public class Settings {

    public static final int gridSize = 15;
    public static final int numberOfDataCenters = 100;
    public static final int numberOfPhysicalMachinesPerDC = 100;
    public static final int memoryPerPM = 10;
    public static final int pMcpu = 3000;
    public static final int pMmemory = 5000;
    public static final int pMbandwidth = 5000;
    public static final double basicEnergyUtilization = 5;

    public static final int failureRate = 30; // Total out of 100
    public static int AdditionalFailureThreshold = 30;

    public static long RestartDuration = 10000;
}
