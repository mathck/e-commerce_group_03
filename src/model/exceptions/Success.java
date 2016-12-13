package model.exceptions;

/**
 * This event represents a success event that might occur to a job
 */


public class Success extends JobEvent {

    public Success(model.implementations.Job job) {
        super(job);
    }
}
