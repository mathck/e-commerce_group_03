package model.exceptions;

public class Failure extends JobEvent {

    public Failure(model.implementations.Job job) {
        super(job);
    }
}
