package controller;

import assets.Assets;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.implementations.Grid;
import model.interfaces.INode;

import java.util.Arrays;
import java.util.TimerTask;
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

        // just for testing to review the produced array in the output
        for (INode[] node : grid.getNodes())
            System.out.println(Arrays.toString(node));

        guiController.DrawGrid(grid);
        startPeriodicDataTask(guiController);
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

    /**
     * KILL ME
     */
    private void startPeriodicDataTask(GUIController guiController) {
        TimerTask periodicDataTask = new TimerTask() {
            @Override
            public void run() {
                guiController.AddDataPoint();
            }
        };

        scheduler.scheduleAtFixedRate(  periodicDataTask,
                                        2000,   // delay
                                        500,    // period length
                                        TimeUnit.MILLISECONDS);
    }
}
