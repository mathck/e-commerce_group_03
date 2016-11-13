package controller;

import assets.Assets;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

public class Main extends Application {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader guiLoader = setUpGUI(primaryStage);
        initInfrastructure(guiLoader.getController());
    }

    private void initInfrastructure(GUIController guiController) {
        Grid grid = new Grid(100, 20);

        guiController.DrawGrid(grid);
        startPeriodicJobProducer(grid, guiController);
    }

    private FXMLLoader setUpGUI(Stage primaryStage) throws java.io.IOException {
        FXMLLoader guiLoader = new FXMLLoader(getClass().getResource("../views/GUI.fxml"));
        Parent root = guiLoader.load();
        primaryStage.getIcons().add(Assets.applicationIcon());
        primaryStage.setTitle("Extended Task Resubmission");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.setResizable(false);
        primaryStage.show();
        return guiLoader;
    }

    private void startPeriodicJobProducer(Grid grid, GUIController guiController) {
        ArrayList<DataCenter> dataCenters = new ArrayList<>(grid.getDataCenters());
        ExecutorService executor = Executors.newCachedThreadPool();
        TimerTask periodicJob = new TimerTask() {

            @Override
            public void run() {
                executor.execute(() -> {

                    // plot Linechart in fixed time interval
                    guiController.plotData();

                    try {

                        for (DataCenter dataCenter : dataCenters) {
                            if (dataCenter.hasFreePM()) {
                                dataCenter.setJob(new Job());
                                return;
                            }
                        }
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) {
                            guiController.addException(event);
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

        scheduler.scheduleAtFixedRate(  periodicJob,
                                        2000,    // delay
                                        500,    // period length
                                        TimeUnit.MILLISECONDS);
    }
}
