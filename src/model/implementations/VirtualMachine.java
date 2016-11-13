package model.implementations;

import model.exceptions.JobEvent;

import java.util.Date;

public class VirtualMachine {

    private double size;
    private double consumedMemory;
    private double consumedCPU;
    private double consumedBandwidth;
    private double pageDirtyingRate;
    private Date dateStarted;
    private long runningTime;
    private String originRequest;

    private Job job;

    public VirtualMachine(int consCPU, int consMemory, int consBandwidth) {
        this.consumedCPU = consCPU;
        this.consumedMemory = consMemory;
        this.consumedBandwidth = consBandwidth;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getConsumedMemory() {
        return consumedMemory;
    }

    public void setConsumedMemory(double consumedMemory) {
        this.consumedMemory = consumedMemory;
    }

    public double getConsumedCPU() {
        return consumedCPU;
    }

    public void setConsumedCPU(double consumedCPU) {
        this.consumedCPU = consumedCPU;
    }

    public double getConsumedBandwidth() {
        return consumedBandwidth;
    }

    public void setConsumedBandwidth(double consumedBandwidth) {
        this.consumedBandwidth = consumedBandwidth;
    }

    public double getPageDirtyingRate() {
        return pageDirtyingRate;
    }

    public void setPageDirtyingRate(double pageDirtyingRate) {
        this.pageDirtyingRate = pageDirtyingRate;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public long getRunningTime() {
        runningTime = System.currentTimeMillis() - getDateStarted().getTime();

        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public String getOriginRequest() {
        return originRequest;
    }

    public void setOriginRequest(String originRequest) {
        this.originRequest = originRequest;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) throws JobEvent, InterruptedException {
        this.job = job;
        job.run();
    }

    public boolean hasJob() {
        return job != null;
    }
}
