package controller;

import assets.Assets;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.calculations.PMCalculation;
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

                    Job currentJob = new Job(); //only for init
                    DataCenter currentDataCenter = dataCenters.get(1); //only for init


                    // plot Linechart in fixed time interval
                    guiController.plotData();

                    try {

                        for (DataCenter dataCenter : dataCenters) {
                            if (dataCenter.hasFreePM()) {
                                dataCenter.setJob(currentJob);
                                return;
                            }
                        }
                    }
                    catch (JobEvent event) {
                        if (event instanceof Failure) {
                            guiController.addException(event);

                            try {
                                if (currentDataCenter.hasFreePM()) {
                                    currentDataCenter.setJob(currentJob);
                                    return;
                                }else{
                                    // select datacenter with lowest manhattan distance and transmit job

                                    System.out.println("transmit to next datacenter");
                                    int sourceX = currentDataCenter.getLocationX();
                                    int sourceY = currentDataCenter.getLocationY();
                                    int targetX;
                                    int targetY;
                                    int nextDataCenterDistance = 0;
                                    int latencyms = 0;
                                    DataCenter nextDataCenter = currentDataCenter;

                                    for (DataCenter dataCenter : dataCenters) {
                                        if(dataCenter.hasFreePM()){
                                            targetX = dataCenter.getLocationX();
                                            targetY = dataCenter.getLocationY();

                                            int manhattanDistance = PMCalculation.manhattanDistance(sourceX,sourceY,targetX,targetY);

                                            if(nextDataCenter == currentDataCenter) {
                                                nextDataCenterDistance = manhattanDistance;
                                                nextDataCenter = dataCenter;
                                            }

                                            if(manhattanDistance <= nextDataCenterDistance && manhattanDistance != 0){
                                                nextDataCenter = dataCenter;
                                            }
                                        }
                                    }
                                    latencyms = nextDataCenterDistance*30;
                                    nextDataCenter.setJob(currentJob);
                                }
                            }
                            catch (JobEvent event1) {
                                if (event1 instanceof Failure) {
                                    guiController.addException(event1);
                                    System.out.println("second try failed");

                                } else if (event1 instanceof Success) {
                                    guiController.addFinished(event1);
                                    System.out.println("second try success");

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

        scheduler.scheduleAtFixedRate(  periodicJob,
                                        2000,    // delay
                                        500,    // period length
                                        TimeUnit.MILLISECONDS);
    }
}
