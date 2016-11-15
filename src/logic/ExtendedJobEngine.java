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

public class ExtendedJobEngine extends JobEngine {

    @Override
    public void initPeriodicJobProducer(Grid grid, GUIController guiController) {
        ArrayList<DataCenter> dataCenters = new ArrayList<>(grid.getDataCenters());
        ExecutorService executor = Executors.newCachedThreadPool();
        periodicJob = new TimerTask() {

            @Override
            public void run() {
                executor.execute(() -> {

                    Job currentJob = new Job();
                    DataCenter currentDataCenter = grid.getNextBest();

                    try {
                        currentDataCenter.setJob(currentJob);
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) {

                            event.responsiblePM.setLockedForRestart();

                            try {

                                if (currentDataCenter.hasFreePM()) {
                                    currentDataCenter.setJob(currentJob);

                                } else {
                                    new JobTransferLogic(guiController, dataCenters, currentDataCenter, currentJob);
                                }
                            }
                            catch (JobEvent jobEvent) {

                                try {

                                    if (currentDataCenter.hasFreePM()) {
                                        currentDataCenter.setJob(currentJob);

                                    } else {
                                        new JobTransferLogic(guiController, dataCenters, currentDataCenter, currentJob);
                                    }
                                }
                                catch (JobEvent secondJobEvent) {

                                    if (secondJobEvent instanceof Failure) {
                                        guiController.addException(secondJobEvent);
                                    }

                                    if(jobEvent instanceof Success || secondJobEvent instanceof Success) {
                                        guiController.addFinished(jobEvent instanceof Success ? jobEvent : secondJobEvent);
                                    }

                                    if(jobEvent instanceof Failure && secondJobEvent instanceof Failure) {
                                        // don't do anything
                                    }
                                    else if (jobEvent instanceof Failure) {
                                        jobEvent.responsiblePM.setLockedForRestart();
                                    }
                                    else if (secondJobEvent instanceof Failure) {
                                        secondJobEvent.responsiblePM.setLockedForRestart();
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
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
                    finally {
                        guiController.addEnergyUtil(grid.getUtilAverage());
                    }
                });
            }
        };
    }

    private static ExtendedJobEngine instance;

    public static ExtendedJobEngine getInstance() {
        if (ExtendedJobEngine.instance == null) {
            ExtendedJobEngine.instance = new ExtendedJobEngine();
        }
        return ExtendedJobEngine.instance;
    }
}
