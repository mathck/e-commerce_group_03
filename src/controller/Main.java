package controller;

import assets.Assets;
import assets.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.BaselineJobEngine;
import logic.ExtendedJobEngine;
import model.implementations.Grid;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader guiLoader = setUpGUI(primaryStage);
        initInfrastructure(guiLoader.getController());
    }

    private void initInfrastructure(GUIController guiController) {
        Grid grid = new Grid(Settings.numberOfDataCenters, Settings.gridSize); // initialization of the grid

        guiController.drawGrid(grid);
        guiController.drawPieCharts();

        BaselineJobEngine.getInstance().initPeriodicJobProducer(grid, guiController);
        ExtendedJobEngine.getInstance().initPeriodicJobProducer(grid, guiController);
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
}
