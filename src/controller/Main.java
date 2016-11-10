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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/main.fxml"));
        primaryStage.getIcons().add(Assets.applicationIcon());
        primaryStage.setTitle("Extended Task Resubmission");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        initInfrastructure();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initInfrastructure() {
        Grid grid = new Grid(20, 10);

        // just for testing to review the produced array in the output
        for (INode[] node : grid.getNodes()) {
            System.out.println(Arrays.toString(node));
        }
    }
}
