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

    public int getUtilTotal() {
        int total = 0;

        for (PhysicalMachine pm : physicalMachines)
            total += pm.getUtilTotal();

        return total;
    }

    @Override
    public String toString() {
        return "1";
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
