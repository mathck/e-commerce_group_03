package model.implementations;

import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.utility.RandomNumber;

public class Job {

    private int latency = 0;

    void run() throws JobEvent, InterruptedException {

        latency(latency);

        System.out.println("\u001B[34m" + "new job: " + this.hashCode() + "\u001B[0m");

        Thread.sleep(RandomNumber.nextGaussian(10000, 5000));

        if(RandomNumber.nextInt(1, 100) <= 5)
            throw new Failure(this); // 5%
        else {
            throw new Success(this); // 95%
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
