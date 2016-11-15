package model.exceptions;

import model.implementations.Job;
import model.implementations.PhysicalMachine;

public class JobEvent extends Exception {

    public Job Job;
    public PhysicalMachine responsiblePM;

    public JobEvent(Job job) {
        this.Job = job;
    }
}
