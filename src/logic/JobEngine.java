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

    public void run() {
        if(isRunning)
            stop();

        scheduler.scheduleAtFixedRate(periodicJob, 0, 250, TimeUnit.MILLISECONDS);
        isRunning = true;
    }

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
                    DataCenter currentDataCenter = grid.getNextBest();

                    guiController.plotData();

                    try {
                        currentDataCenter.setJob(currentJob);
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) {

                            try {
                                if (currentDataCenter.hasFreePM()) {
                                    currentDataCenter.setJob(currentJob);
                                } else {
                                    new JobTransferLogic(guiController, dataCenters, currentDataCenter, currentJob);
                                }
                            }
                            catch (JobEvent jobEvent) {
                                if (jobEvent instanceof Failure) {
                                    guiController.addException(jobEvent);

                                } else if (jobEvent instanceof Success) {
                                    guiController.addFinished(jobEvent);
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

    public void stop() {
        scheduler.shutdown();
        scheduler = null;
        scheduler = Executors.newScheduledThreadPool(1);
        isRunning = false;
    }
}
