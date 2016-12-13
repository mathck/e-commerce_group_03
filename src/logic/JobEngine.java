package logic;

import controller.GUIController;
import model.implementations.Grid;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The abstract JobEngine describes the similarities between
 * the different JobEngine implementations
 * A ScheduledExecutorService is used for the job creation
 */

abstract class JobEngine {

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    TimerTask periodicJob;
    private boolean isRunning = false;

    public abstract void initPeriodicJobProducer(Grid grid, GUIController guiController);

    public void run(GUIController guiController) {

        if(isRunning)
            stop(guiController);

        if (this instanceof BaselineJobEngine)
            guiController.enableBaseline(true);
        else if (this instanceof ExtendedJobEngine)
            guiController.enableExtension(true);

        scheduler.scheduleAtFixedRate(periodicJob, 0, 5, TimeUnit.MILLISECONDS);
        isRunning = true;
    }

    public void stop(GUIController guiController) {
        isRunning = false;
        scheduler.shutdown();

        if (this instanceof BaselineJobEngine)
            guiController.enableBaseline(false);
        else if (this instanceof ExtendedJobEngine)
            guiController.enableExtension(false);
    }
}
