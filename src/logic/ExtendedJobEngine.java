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
        // TODO James: implement me
        // steps:
        // 1. copy baseline implementation
        // 2. wenn ein job auf einer physicalMachine failed
        //      -> physicalMachine.setLockedForRestart()
        //      -> JobTransferLogic
        //
        // ...
        // whatever wir noch definiert haben für unseren extended algorithmus


        ArrayList<DataCenter> dataCenters = new ArrayList<>(grid.getDataCenters());
        ExecutorService executor = Executors.newCachedThreadPool();
        periodicJob = new TimerTask() {

            @Override
            public void run() {
                executor.execute(() -> {

                    Job currentJob = new Job();
                    DataCenter currentDataCenter = grid.getNextBest();
                    PhysicalMachine currentPM_A = currentDataCenter.getPhysicalMachines().get(0);
                    PhysicalMachine currentPM_B = currentDataCenter.getPhysicalMachines().get(0);
                    PhysicalMachine currentPM_C = currentDataCenter.getPhysicalMachines().get(0);

                    guiController.plotData();
                    guiController.addEnergyUtil(grid.getUtilAverage());

                    try {
                        currentPM_A = currentDataCenter.setJob(currentJob);
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) {

                            try {
                                currentPM_A.setLockedForRestart();
                                if (currentDataCenter.hasFreePM()) {
                                    currentPM_B = currentDataCenter.setJob(currentJob);

                                    try {
                                        if (currentDataCenter.hasFreePM()) {
                                            currentPM_C = currentDataCenter.setJob(currentJob);
                                        } else {
                                            new JobTransferLogic(guiController, dataCenters, currentDataCenter, currentJob);
                                        }
                                    }
                                    catch (JobEvent jobjobEvent) {
                                        if (jobjobEvent instanceof Failure) {
                                            guiController.addException(jobjobEvent);

                                        } else if (jobjobEvent instanceof Success) {
                                            guiController.addFinished(jobjobEvent);
                                        }

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

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

    private static ExtendedJobEngine instance;

    public static ExtendedJobEngine getInstance() {
        if (ExtendedJobEngine.instance == null) {
            ExtendedJobEngine.instance = new ExtendedJobEngine();
        }
        return ExtendedJobEngine.instance;
    }
}
