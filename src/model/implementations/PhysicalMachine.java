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

    private double utilTotal;
    private double utilIdle;
    private double utilCPU;
    private double utilMemory;
    private double utilBandwidth;
    private double workloadCPU;
    private double workloadMemory;
    private double workloadBandwidth;

    public PhysicalMachine() {
        virtualMachines = new ArrayList<>();

        int numberOfVirtualMachines = (int) (memory / Settings.memoryPerPM);
        int consumedCPU = (int) (getCpu() / numberOfVirtualMachines);
        int consumedMemory = (int) (getMemory() / numberOfVirtualMachines);
        int consumedBandwidth = (int) (getBandwidth() / numberOfVirtualMachines);

        for(int i = 0; i < numberOfVirtualMachines; i++)
            virtualMachines.add(new VirtualMachine(consumedCPU, consumedMemory, consumedBandwidth));
    }

    public boolean setJob(Job job) throws JobEvent, InterruptedException {
        for (VirtualMachine vm : virtualMachines)
        {
            if(!vm.hasJob()) {
                vm.setJob(job);
                return true;
            }
        }

        return false;
    }

    public boolean hasFreeVM() {
        for (VirtualMachine vm : virtualMachines)
            if(!vm.hasJob())
                return true;
        return false;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public double getUtilTotal() {
        utilTotal = utilIdle +workloadCPU*utilCPU + workloadMemory * utilMemory + workloadBandwidth*utilBandwidth;
        return utilTotal;
    }

    private void setUtilTotal(double utilTotal) {
        this.utilTotal = utilTotal;
    }

    public double getUtilIdle() {
        return utilIdle;
    }

    public void setUtilIdle(double utilIdle) {
        this.utilIdle = utilIdle;
    }

    public double getUtilCPU() {
        return utilCPU;
    }

    public void setUtilCPU(double utilCPU) {
        this.utilCPU = utilCPU;
    }

    public double getUtilMemory() {
        return utilMemory;
    }

    public void setUtilMemory(double utilMemory) {
        this.utilMemory = utilMemory;
    }

    public double getUtilBandwidth() {
        return utilBandwidth;
    }

    public void setUtilBandwidth(double utilBandwidth) {
        this.utilBandwidth = utilBandwidth;
    }

    public double getWorkloadCPU() {
        return workloadCPU;
    }

    public void setWorkloadCPU(double workloadCPU) {
        this.workloadCPU = workloadCPU;
    }

    public double getWorkloadMemory() {
        return workloadMemory;
    }

    public void setWorkloadMemory(double workloadMemory) {
        this.workloadMemory = workloadMemory;
    }

    public double getWorkloadBandwidth() {
        return workloadBandwidth;
    }

    public void setWorkloadBandwidth(double workloadBandwidth) {
        this.workloadBandwidth = workloadBandwidth;
    }
}
