package model.implementations;

import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.utility.RandomNumber;

public class Job {

    void run() throws JobEvent, InterruptedException {

        System.out.println("started new job");

        Thread.sleep(RandomNumber.nextGaussian(5000, 1000));

        if(RandomNumber.nextInt(1, 100) <= 5) // 5%
            throw new Failure(this);
        else {
            throw new Success(this);
        }
    }
}
