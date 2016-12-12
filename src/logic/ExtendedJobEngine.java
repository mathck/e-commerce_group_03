package logic;

import controller.GUIController;
import model.exceptions.Failure;
import model.exceptions.JobEvent;
import model.exceptions.NoAvailableDataCenter;
import model.exceptions.Success;
import model.implementations.DataCenter;
import model.implementations.Grid;
import model.implementations.Job;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*  Extension for Task Resubmission
    Description:
    After a task failed on the main resource, it is resubmitted to another node.
    In the meantime, the source node is restarted to exclude possible failures due
    to software and caching.

    The failed task is replicated and sent to two nodes. If one of them also fails,
    the concerning node is restarted as well. If both nodes Fail, the task cannot be finished.
 */

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
                    DataCenter currentDataCenter = null;

                    try {
                        currentDataCenter = grid.getNextBest(); //the datacenter with the lowest utilization is chosen
                        currentDataCenter.setJob(currentJob); //the job is then sent to this datacenter

                        /*
                            "set.job()" determines if the DC has a free PM. There is only a free PM if there is at
                             least one VM which does not run a job.
                        */
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) { //This happens if the job-process did not succeed

                            event.responsiblePM.setLockedForRestart();
                            /*
                                The PM, where the job failed, will be marked for a restart.
                                Marked PMs will no longer appear as free PMs until it is restarted.
                             */

                            try {

                                if (currentDataCenter.hasFreePM()) { //Again, the DC will be checked for a free PM
                                    currentDataCenter.setJob(currentJob);
                                      /*
                                        then the Job is resubmitted to the same DC, but another PM (because the current
                                        PM is locked for a restart)
                                     */

                                } else {
                                    new JobTransferLogic(guiController, dataCenters, currentDataCenter, currentJob);
                                    // the job is resubmitted to a different DC
                                }
                            }
                            catch (JobEvent jobEvent) {

                                try {

                                    if (currentDataCenter.hasFreePM()) {
                                        currentDataCenter.setJob(currentJob);
                                        //the job is duplicated and submitted to the second resource (PM)

                                    } else {
                                        new JobTransferLogic(guiController, dataCenters, currentDataCenter, currentJob);
                                        //if there are no free PMs in this DC it will be transfered to the next DC
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
                    } catch (NoAvailableDataCenter noAvailableDataCenter) {
                        System.out.println("No available DataCenter");
                    } finally {
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
