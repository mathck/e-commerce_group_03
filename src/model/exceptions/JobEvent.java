package model.exceptions;

import model.implementations.Job;

public class JobEvent extends Exception {

    public Job Job;

    public JobEvent(Job job) {
        this.Job = job;
    }
}
