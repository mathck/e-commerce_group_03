package logic;

import controller.GUIController;
import model.implementations.Grid;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

abstract class JobEngine {

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    TimerTask periodicJob;
    private boolean isRunning = false;

    public abstract void initPeriodicJobProducer(Grid grid, GUIController guiController);

    public void run(GUIController guiController) {

        if(isRunning)
            stop();

        if (this instanceof BaselineJobEngine)
            guiController.enableBaseline();
        else if (this instanceof ExtendedJobEngine)
            guiController.enableExtension();

        scheduler.scheduleAtFixedRate(periodicJob, 0, 50, TimeUnit.MILLISECONDS);
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
        scheduler.shutdown();
    }
}
