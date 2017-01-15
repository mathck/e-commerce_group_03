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

        int numberOfVirtualMachines = 3000;// (int) (memory / Settings.memoryPerPM);
        int consumedCPU = (int) (cpu / numberOfVirtualMachines);
        int consumedMemory = (int) (memory / numberOfVirtualMachines);
        int consumedBandwidth = (int) (bandwidth / numberOfVirtualMachines);

        for(int i = 0; i < numberOfVirtualMachines; i++)
            virtualMachines.add(new VirtualMachine(consumedCPU, consumedMemory, consumedBandwidth));
    }

    public void setLockedForRestart() {
        if (getCurrentNumberOfJobs() != 0) {
            this.isLockedForRestart = true;
            System.out.println("this.isLockedForRestart = true");
        }
    }

    /*
    Iterate over all VMs on the PM and check, if a VM is free (has no job) and sets the job. The additional failure rate is increased by every new job.
    When the machine is restarted, the additional failure rate is set to 0.
    In case of a failure exception, the exception is either rethrown and nothing happens, or the exception is rethrown and the PM is locked for restart.
    After all jobs on that specific machine are finished, the machine is restarted, if it is locked for restart.
     */
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

    /*
    Checks, if there is a VM that is currently not operating a job.
     */
    boolean hasFreeVM() {
        if(this.isLockedForRestart)
            return false;

        for (VirtualMachine vm : virtualMachines)
            if(!vm.hasJob())
                return true;
        return false;
    }

    /*
    Restarts a PM by using a fixed restart duration.
     */
    public void restartPM() throws InterruptedException {

        System.out.println("\u001B[30;47m" + "RESTARTING PHYSICAL MACHINE: " + this.hashCode() + "\u001B[0m");

        Thread.sleep(Settings.RestartDuration);
        this.isLockedForRestart = false;
        System.out.println("\u001B[30;42m" + "RESTARTED: " + this.hashCode() + "\u001B[0m");
    }

    /*
    Counts the jobs operated by the VMs of a PM.
     */
    private int getCurrentNumberOfJobs() {
        int jobCounter = 0;
        for (VirtualMachine vm : virtualMachines)
            if(vm.hasJob())
                jobCounter++;
        return jobCounter;
    }

    /*
    Calculates the total utilization of the PM.
     */
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
