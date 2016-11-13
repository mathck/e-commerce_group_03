package logic;

import model.calculations.PMCalculation;
import model.exceptions.JobEvent;
import model.implementations.DataCenter;
import model.implementations.Job;

import java.util.ArrayList;

class JobTransferLogic {

    JobTransferLogic(ArrayList<DataCenter> dataCenters, DataCenter currentDataCenter, Job currentJob) throws JobEvent, InterruptedException {

        System.out.println("transmit to next datacenter: " + currentJob.hashCode());

        int sourceX = currentDataCenter.getLocationX();
        int sourceY = currentDataCenter.getLocationY();
        int targetX;
        int targetY;
        int nextDataCenterDistance = 0;
        int latencyms = 0;
        DataCenter nextDataCenter = currentDataCenter;

        for (DataCenter dataCenter : dataCenters) {
            if(dataCenter.hasFreePM()) {
                targetX = dataCenter.getLocationX();
                targetY = dataCenter.getLocationY();

                int manhattanDistance = PMCalculation.manhattanDistance(sourceX, sourceY, targetX, targetY);

                if(nextDataCenter == currentDataCenter) {
                    nextDataCenterDistance = manhattanDistance;
                    nextDataCenter = dataCenter;
                }

                if(manhattanDistance <= nextDataCenterDistance && manhattanDistance != 0) {
                    nextDataCenter = dataCenter;
                }
            }
        }
        latencyms = nextDataCenterDistance * 30;
        nextDataCenter.setJob(currentJob);
    }
}
