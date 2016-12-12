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


/*  Baseline: Task Resubmission
    Description:
    A job may fail now whenever a failed task is detected,
    in this case at runtime the task is resubmitted either
    to the same or to a different resource for execution.
 */

public class BaselineJobEngine extends JobEngine {

    @Override
    public void initPeriodicJobProducer(Grid grid, GUIController guiController) {
        ArrayList<DataCenter> dataCenters = new ArrayList<>(grid.getDataCenters()); //creation of a grid of datacenters
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

                            try {
                                if (currentDataCenter.hasFreePM()) { //Again, the DC will be checked for a free PM
                                    currentDataCenter.setJob(currentJob);
                                    /*
                                        then the Job is resubmitted to the same DC, either to the same PM or another
                                        PM within the DC
                                     */
                                } else {
                                    //if there are no free PMs in this DC it will be resubmitted to a different DC
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
                    } catch (NoAvailableDataCenter noAvailableDataCenter) {
                        System.out.println("No available DataCenter");
                    } finally {
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
