package model.implementations;

import assets.Settings;
import model.exceptions.Failure;
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

    private double additionalFailureRate = 0;

    private boolean isLockedForRestart = false;

    private VirtualMachine failedVM;

    PhysicalMachine() {

        isLockedForRestart = false;
        virtualMachines = new ArrayList<>();

        int numberOfVirtualMachines = (int) (memory / Settings.memoryPerPM);
        int consumedCPU = (int) (cpu / numberOfVirtualMachines);
        int consumedMemory = (int) (memory / numberOfVirtualMachines);
        int consumedBandwidth = (int) (bandwidth / numberOfVirtualMachines);

        for(int i = 0; i < numberOfVirtualMachines; i++)
            virtualMachines.add(new VirtualMachine(consumedCPU, consumedMemory, consumedBandwidth));
    }

    public void setLockedForRestart() {
        this.isLockedForRestart = true;
    }

    boolean setJob(Job job) throws JobEvent, InterruptedException {

        try {

            for (VirtualMachine vm : virtualMachines)
            {
                if(!vm.hasJob()) {

                    job.setAdditionalFailureRate(++additionalFailureRate);

                    vm.setJob(job);
                    return true;
                }
            }

        }
        catch (JobEvent | InterruptedException ex) {

            if(ex instanceof Failure)
                ((Failure) ex).responsiblePM = this;

            if(isLockedForRestart && getCurrentNumberOfJobs() == 0)
                this.restartPM();

            throw ex;
        }

        return false;
    }

    boolean hasFreeVM() {
        if(isLockedForRestart)
            return false;

        for (VirtualMachine vm : virtualMachines)
            if(!vm.hasJob())
                return true;
        return false;
    }

    public void restartPM() throws InterruptedException {
        Thread.sleep(Settings.RestartDuration);
        this.isLockedForRestart = false;
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
