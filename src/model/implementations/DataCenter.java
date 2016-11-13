package model.implementations;

import model.exceptions.JobEvent;
import model.interfaces.INode;

import java.util.ArrayList;
import java.util.List;

public class DataCenter implements INode {

    private List<PhysicalMachine> physicalMachines;
    private int[][] locationInGrid;
    private double utilIdle;
    private double utilTotal;

    DataCenter(int numberOfPhysicalMachines) {
        physicalMachines = new ArrayList<>(numberOfPhysicalMachines);

        for(int i = 0; i < numberOfPhysicalMachines; i++)
            physicalMachines.add(new PhysicalMachine());

        for (PhysicalMachine physicalMachine : physicalMachines) {
            utilTotal += physicalMachine.getUtilTotal();
        }
    }

    public void setJob(Job job) throws JobEvent, InterruptedException {
        for (PhysicalMachine pm : physicalMachines)
        {
            if(pm.hasFreeVM()) {
                pm.setJob(job);
                return;
            }
        }
    }

    public boolean hasFreePM() {
        for (PhysicalMachine pm : physicalMachines)
            if(pm.hasFreeVM())
                return true;

        return false;
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

    public int[][] getLocationInGrid() {
        return locationInGrid;
    }

    public void setLocationInGrid(int[][] locationInGrid) {
        this.locationInGrid = locationInGrid;
    }

    public double getUtilIdle() {
        return utilIdle;
    }

    public void setUtilIdle(double utilIdle) {
        this.utilIdle = utilIdle;
    }

    public double getUtilTotal() {
        return utilTotal;
    }

    public void setUtilTotal(double utilTotal) {
        this.utilTotal = utilTotal;
    }
}
