package model.implementations;

import assets.Settings;
import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.utility.RandomNumber;

public class Job {

    private int latency = 0;

    void run() throws JobEvent, InterruptedException {

        latency(latency);

        System.out.println("new job: " + this.hashCode());

        Thread.sleep(RandomNumber.nextGaussian(10000, 5000));

        if(RandomNumber.nextInt(1, 100) <= Settings.failureRate)
            throw new Failure(this);
        else {
            throw new Success(this);
        }
    }

    void latency(int ms) throws InterruptedException {
        Thread.sleep(ms);
        this.latency = 0;
    }

    public void addLatency(int latencyms) {
        latency += latencyms;
    }
}
