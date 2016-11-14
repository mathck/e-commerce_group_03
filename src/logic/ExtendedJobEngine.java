package logic;

import controller.GUIController;
import model.implementations.Grid;

public class ExtendedJobEngine extends JobEngine {

    @Override
    public void initPeriodicJobProducer(Grid grid, GUIController guiController) {
        // TODO James: implement me
        // steps:
        // 1. copy baseline implementation
        // 2. wenn ein job auf einer physicalMachine failed
        //      -> physicalMachine.setLockedForRestart()
        //      -> JobTransferLogic
        //
        // ...
        // whatever wir noch definiert haben für unseren extended algorithmus
    }

    private static ExtendedJobEngine instance;

    public static ExtendedJobEngine getInstance() {
        if (ExtendedJobEngine.instance == null) {
            ExtendedJobEngine.instance = new ExtendedJobEngine();
        }
        return ExtendedJobEngine.instance;
    }
}
