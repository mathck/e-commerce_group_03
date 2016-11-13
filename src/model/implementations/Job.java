package model.implementations;

import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.utility.RandomNumber;

public class Job {

    void run() throws JobEvent, InterruptedException {

        System.out.println("new job: " + this.hashCode());

        Thread.sleep(RandomNumber.nextGaussian(10000, 5000));

        if(RandomNumber.nextInt(1, 100) <= 5)
            throw new Failure(this); // 5%
        else {
            throw new Success(this); // 95%
        }
    }
}
