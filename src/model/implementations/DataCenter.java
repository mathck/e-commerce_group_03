package model.implementations;

import model.exceptions.JobEvent;
import model.interfaces.INode;
import model.utility.RandomNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataCenter implements INode {

    private List<PhysicalMachine> physicalMachines;
    private int locationX;
    private int locationY;

    DataCenter(int numberOfPhysicalMachines) {
        physicalMachines = new ArrayList<>(numberOfPhysicalMachines);

        for(int i = 0; i < numberOfPhysicalMachines; i++)
            physicalMachines.add(new PhysicalMachine());
    }

    /**
     * assign a job to this data center
     * @param job will be passed to the next free physical machine
     * @throws JobEvent job failed or
     */
    public void setJob(Job job) throws JobEvent, InterruptedException {
        for (PhysicalMachine pm : physicalMachines)
            if(pm.hasFreeVM()) {
                pm.setJob(job);
                return;
            }
    }

    /**
     * Does the dataCenter have a physical machine that has room for 1 more job?
     */
    public boolean hasFreePM() {
        for (PhysicalMachine pm : physicalMachines)
            if(pm.hasFreeVM())
                return true;

        return false;
    }

    /**
     * Does the dataCenter have a physical machine that has room for more jobs?
     * @param amountOfFreePMs amount of required free physical machines
     */
    public boolean hasFreePM(int amountOfFreePMs) {
        int freePMCounter = 0;

        for (PhysicalMachine pm : physicalMachines) {
            if (pm.hasFreeVM())
                freePMCounter++;

            if (freePMCounter == amountOfFreePMs)
                return true;
        }

        return false;
    }

    /**
     * @return total utilization of this data center
     */
    int getUtilTotal() {
        int total = 0;

        for (PhysicalMachine pm : physicalMachines)
            total += pm.getUtilTotal();

        return total;
    }

    @Override
    public String toString() {
        return "1";
    }

    public List<PhysicalMachine> getPhysicalMachines() {
        return physicalMachines;
    }

    public void setPhysicalMachines(List<PhysicalMachine> physicalMachines) {
        this.physicalMachines = physicalMachines;
    }

    public int getLocationX() {
        return locationX;
    }

    void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    void setLocationY(int locationY) {
        this.locationY = locationY;
    }
}
