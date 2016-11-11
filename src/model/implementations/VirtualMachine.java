package model.implementations;

import java.util.Date;

public class VirtualMachine {

    private double size;
    private double consMemory;
    private double consCPU;
    private double consBandwidth;
    private double pageDirtyingRate;
    private Date dateStarted;
    private long runningTime;
    private String originRequest;

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getConsMemory() {
        return consMemory;
    }

    public void setConsMemory(double consMemory) {
        this.consMemory = consMemory;
    }

    public double getConsCPU() {
        return consCPU;
    }

    public void setConsCPU(double consCPU) {
        this.consCPU = consCPU;
    }

    public double getConsBandwidth() {
        return consBandwidth;
    }

    public void setConsBandwidth(double consBandwidth) {
        this.consBandwidth = consBandwidth;
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
}
