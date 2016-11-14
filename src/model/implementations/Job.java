package model.implementations;

import assets.Settings;
import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.utility.RandomNumber;

public class Job {

    private int latency = 0;
    private double additionalFailureRate = 0;

    void run() throws JobEvent, InterruptedException {

        latency(latency);

        System.out.println("\u001B[34m" + "started job: " + this.hashCode() + "\u001B[0m");

        Thread.sleep(RandomNumber.nextGaussian(10000, 5000));

        if(RandomNumber.nextInt(1, 100) <= calculateTotalFailureRate())
            throw new Failure(this);
        else {
            throw new Success(this);
        }
    }

    private int calculateTotalFailureRate() {
        return (Settings.failureRate + additionalFailureRate / 10) <= Settings.AdditionalFailureThreshold ?
               (int) (Settings.failureRate + additionalFailureRate / 10) : Settings.AdditionalFailureThreshold;
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
