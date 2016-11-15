package logic;

import controller.GUIController;
import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.Success;
import model.implementations.DataCenter;
import model.implementations.Grid;
import model.implementations.Job;
import model.implementations.PhysicalMachine;

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
                    PhysicalMachine currentPM_A = null;
                    PhysicalMachine currentPM_B = null;
                    PhysicalMachine currentPM_C = null;

                    int jobCounter = 0;
                    boolean bFailed = false;
                    boolean cFailed = false;

                    guiController.plotData();
                    guiController.addEnergyUtil(grid.getUtilAverage());

                    try {
                        currentDataCenter.setJob(currentJob);
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) {

                            currentPM_A = event.responsiblePM;
                            currentPM_A.setLockedForRestart();

                            try {

                                if (currentDataCenter.hasFreePM(2)) {

                                    currentDataCenter.setJob(currentJob);
                                    currentDataCenter.setJob(currentJob);

                                } else {
                                    new JobTransferLogic(guiController, dataCenters, currentDataCenter, currentJob);
                                }
                            }
                            catch (JobEvent jobEvent) {

                                jobCounter++;

                                if(jobCounter == 1)
                                    currentPM_B = jobEvent.responsiblePM;
                                else
                                    currentPM_C = jobEvent.responsiblePM;

                                if (jobEvent instanceof Failure) {
                                    guiController.addException(jobEvent);

                                    if(currentPM_B == jobEvent.responsiblePM)
                                        bFailed = true;

                                    if(currentPM_C == jobEvent.responsiblePM)
                                        cFailed = true;

                                } else if (jobEvent instanceof Success) {
                                    guiController.addFinished(jobEvent);
                                }

                                if(jobCounter == 2) {
                                    if (!(bFailed && cFailed)) {
                                        if(bFailed && currentPM_B != null)
                                            currentPM_B.setLockedForRestart();
                                        else if(cFailed && currentPM_C != null)
                                            currentPM_C.setLockedForRestart();
                                    }
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

    private static ExtendedJobEngine instance;

    public static ExtendedJobEngine getInstance() {
        if (ExtendedJobEngine.instance == null) {
            ExtendedJobEngine.instance = new ExtendedJobEngine();
        }
        return ExtendedJobEngine.instance;
    }
}
