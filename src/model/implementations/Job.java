package model.implementations;

import assets.Settings;
import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.utility.RandomNumber;

public class Job {

    private int jobDuration;
    private int latency = 0;
    private double additionalFailureRate = 0;

    public Job() {
        jobDuration = RandomNumber.nextGaussian(3000, 1000);
    }

    /*
    Runs a job by using a job duration as Thread.sleep timer.
    The execution of a job fails with a increasing probability.
    If a job fails, a failure exception is thrown, otherwise, a success exception is thrown.
     */
    void run() throws JobEvent, InterruptedException {

        latency(latency);

        System.out.println("\u001B[34m" + "started job: " + this.hashCode() + "\u001B[0m");

        Thread.sleep(jobDuration);

        if(RandomNumber.nextInt(1, 100) <= calculateTotalFailureRate())
            throw new Failure(this);
        else {
            throw new Success(this);
        }
    }

    /*
    Calculates the total failure rate by adding the general failure rate and the additional failure rate of the PM.
     */
    private int calculateTotalFailureRate() {
        if ((additionalFailureRate / 10) <= Settings.AdditionalFailureThreshold)
            return (int) (Settings.failureRate + additionalFailureRate / 10);

        else return Settings.failureRate + Settings.AdditionalFailureThreshold;
    }

    private void latency(int ms) throws InterruptedException {
        Thread.sleep(ms);
        this.latency = 0;
    }

    public void addLatency(int latencyms) {
        latency += latencyms;
    }

    public void setAdditionalFailureRate(double additionalFailureRate) {
        this.additionalFailureRate = additionalFailureRate;
    }
}
