package model.implementations;

import assets.Settings;
import model.exceptions.JobEvent;
import model.utility.RandomNumber;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMachine {

    private List<VirtualMachine> virtualMachines;

    private double cpu = RandomNumber.nextGaussian(Settings.pMcpu);
    private double memory = RandomNumber.nextGaussian(Settings.pMmemory);
    private double bandwidth = RandomNumber.nextGaussian(Settings.pMbandwidth);

    private double utilIdle = RandomNumber.nextGaussian(10);
    private double utilCPU = RandomNumber.nextGaussian(10);
    private double utilMemory = RandomNumber.nextGaussian(10);
    private double utilBandwidth = RandomNumber.nextGaussian(10);

    PhysicalMachine() {
        virtualMachines = new ArrayList<>();

        int numberOfVirtualMachines = (int) (memory / Settings.memoryPerPM);
        int consumedCPU = (int) (cpu / numberOfVirtualMachines);
        int consumedMemory = (int) (memory / numberOfVirtualMachines);
        int consumedBandwidth = (int) (bandwidth / numberOfVirtualMachines);

        for(int i = 0; i < numberOfVirtualMachines; i++)
            virtualMachines.add(new VirtualMachine(consumedCPU, consumedMemory, consumedBandwidth));
    }

    boolean setJob(Job job) throws JobEvent, InterruptedException {
        for (VirtualMachine vm : virtualMachines)
        {
            if(!vm.hasJob()) {
                vm.setJob(job);
                return true;
            }
        }

        return false;
    }

    boolean hasFreeVM() {
        for (VirtualMachine vm : virtualMachines)
            if(!vm.hasJob())
                return true;
        return false;
    }

    private int getCurrentNumberOfJobs() {
        int jobCounter = 0;
        for (VirtualMachine vm : virtualMachines)
            if(vm.hasJob())
                jobCounter++;
        return jobCounter;
    }

    public double getUtilTotal() {
        return  utilIdle +
                getWorkloadCPU() * utilCPU +
                getWorkloadMemory() * utilMemory +
                getWorkloadBandwidth() * utilBandwidth;
    }

    private double getWorkloadCPU() {
        return RandomNumber.nextGaussian(10, 5) * getCurrentNumberOfJobs();
    }

    private double getWorkloadMemory() {
        return RandomNumber.nextGaussian(20, 8) * getCurrentNumberOfJobs();
    }

    private double getWorkloadBandwidth() {
        return RandomNumber.nextGaussian(30, 10) * getCurrentNumberOfJobs();
    }
}
