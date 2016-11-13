package logic;

import controller.GUIController;
import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.implementations.DataCenter;
import model.implementations.Grid;
import model.implementations.Job;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobEngine {

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private TimerTask periodicJob;

    private static JobEngine instance;
    private boolean isRunning = false;

    private JobEngine () {

    }

    public static JobEngine getInstance() {
        if (JobEngine.instance == null) {
            JobEngine.instance = new JobEngine();
        }
        return JobEngine.instance;
    }

    public void initPeriodicJobProducer(Grid grid, GUIController guiController) {
        ArrayList<DataCenter> dataCenters = new ArrayList<>(grid.getDataCenters());
        ExecutorService executor = Executors.newCachedThreadPool();
        periodicJob = new TimerTask() {

            @Override
            public void run() {
                executor.execute(() -> {

                    Job currentJob = new Job();
                    DataCenter currentDataCenter = dataCenters.get(1); // TODO why 1 and not 0?

                    guiController.plotData();

                    try {
                        for (DataCenter dataCenter : dataCenters) {
                            if (dataCenter.hasFreePM()) {
                                dataCenter.setJob(currentJob);
                                return;
                            }
                        }
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) {
                            guiController.addException(event);

                            try {
                                if (currentDataCenter.hasFreePM()) {
                                    currentDataCenter.setJob(currentJob);
                                    return;

                                } else {

                                    new JobTransferLogic(dataCenters, currentDataCenter, currentJob);
                                }
                            }
                            catch (JobEvent jobEvent) {
                                if (jobEvent instanceof Failure) {
                                    guiController.addException(jobEvent);
                                    System.out.println("second try failed");

                                } else if (jobEvent instanceof Success) {
                                    guiController.addFinished(jobEvent);
                                    System.out.println("second try success");

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else if (event instanceof Success) {
                            guiController.addFinished(event);
                        }
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        };
    }

    public void run() {
        if(isRunning)
            stop();

        scheduler.scheduleAtFixedRate(  periodicJob,
                                        0,      // delay
                                        500,    // period length
                                        TimeUnit.MILLISECONDS);
        isRunning = true;
    }

    // TODO make this work kk
    private void stop() {
        scheduler.shutdown();
        scheduler = null;
        scheduler = Executors.newScheduledThreadPool(1);
        isRunning = false;
    }
}
