package model.implementations;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMachine {

    private List<VirtualMachine> virtualMachines;

    private double cpu;
    private double memory;
    private double bandwidth;

    private double utilTotal;
    private double utilIdle;
    private double utilCPU;
    private double utilMemory;
    private double utilBandwidth;
    private double workloadCPU;
    private double workloadMemory;
    private double workloadBandwidth;

    public PhysicalMachine(int numberOfVirtualMachines) {
        virtualMachines = new ArrayList<>(numberOfVirtualMachines);
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
        utilTotal=utilIdle + workloadCPU*utilCPU + workloadMemory*utilMemory + workloadBandwidth*utilBandwidth;
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
