import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.util.Pair;

import java.util.Collection;

/**
 * Created by kirkby on 8/4/15.
 */
public class RobotMapWorker extends Service<Integer> {
    private RobotMap robotMap;

    public RobotMapWorker (RobotMap rm) {
        this.robotMap = rm;
    }

    public Pair<Integer, Integer> getRobotLoc() {
        return robotMap.getRobotLoc();
    }
    public Collection<Pair<Integer,Integer>> getCansLoc() {
        return robotMap.getCansLoc();
    }


    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>(){
                protected Integer call() {
                    robotMap.stepStrategy();
                    return 0;
                }
        };
    }
}
