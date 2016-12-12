package assets;

public class Settings { //in this class we set the settings for the algorithm

    public static final int gridSize = 5;
    public static final int numberOfDataCenters = 10;
    public static final int numberOfPhysicalMachinesPerDC = 10;
    public static final int memoryPerPM = 10;
    public static final int pMcpu = 3000;
    public static final int pMmemory = 5000;
    public static final int pMbandwidth = 5000;
    public static final double basicEnergyUtilization = 5;

    public static final int failureRate = 30; // Total out of 100
    public static int AdditionalFailureThreshold = 30;

    public static long RestartDuration = 10000;
}
