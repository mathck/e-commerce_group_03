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

public class BaselineJobEngine extends JobEngine {

    @Override
    public void initPeriodicJobProducer(Grid grid, GUIController guiController) {
        ArrayList<DataCenter> dataCenters = new ArrayList<>(grid.getDataCenters());
        ExecutorService executor = Executors.newCachedThreadPool();
        periodicJob = new TimerTask() {

            @Override
            public void run() {
                executor.execute(() -> {

                    Job currentJob = new Job();
                    guiController.increaseTotalCreatedJobs();

                    DataCenter currentDataCenter = grid.getNextBest();

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
                    finally {
                        guiController.addEnergyUtil(grid.getUtilAverage());
                    }
                });
            }
        };
    }

    private static BaselineJobEngine instance;

    public static BaselineJobEngine getInstance() {
        if (BaselineJobEngine.instance == null) {
            BaselineJobEngine.instance = new BaselineJobEngine();
        }
        return BaselineJobEngine.instance;
    }
}
