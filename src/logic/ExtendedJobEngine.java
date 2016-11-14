package logic;

import controller.GUIController;
import model.implementations.Grid;

public class ExtendedJobEngine extends JobEngine {

    @Override
    public void initPeriodicJobProducer(Grid grid, GUIController guiController) {

    }

    private static ExtendedJobEngine instance;

    public static ExtendedJobEngine getInstance() {
        if (ExtendedJobEngine.instance == null) {
            ExtendedJobEngine.instance = new ExtendedJobEngine();
        }
        return ExtendedJobEngine.instance;
    }
}
