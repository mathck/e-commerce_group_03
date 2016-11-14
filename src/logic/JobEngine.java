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

    public void run() {
        if(isRunning)
            stop();

        scheduler.scheduleAtFixedRate(periodicJob, 0, 250, TimeUnit.MILLISECONDS);
        isRunning = true;
    }

    public void stop() {
        scheduler.shutdown();
        scheduler = null;
        scheduler = Executors.newScheduledThreadPool(1);
        isRunning = false;
    }
}
