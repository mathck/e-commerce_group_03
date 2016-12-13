package model.exceptions;

/**
 * This event represents a failure event that might occur to a job
 */

public class Failure extends JobEvent {

    public Failure(model.implementations.Job job) {
        super(job);
    }
}
