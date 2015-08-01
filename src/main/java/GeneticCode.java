import java.util.Random;

//TODO NEXT: code for reproduction

public class GeneticCode {
    private static int MAX_RUN_STEPS_ON_MAP=100;
    private static int MAX_POP_SIZE=20;

    private int[][] population = new int[MAX_POP_SIZE][Scenarios.getNumberOfScenes()];
    private static Random RND = new Random(System.currentTimeMillis());

    public float fitnessFunction( int[] strategy, Iterable<RobotMap> testRobotMaps) {
        float ret = 0;
        for( RobotMap rm : testRobotMaps) {
            ret += rm.scoreStrategy( strategy, MAX_RUN_STEPS_ON_MAP);
        }
        return ret;
    }

    private void initPopulation() {
        for(int i=0; i<MAX_POP_SIZE; i++) {
            for(int j=0; j<Scenarios.getNumberOfScenes(); j++) {
                population[i][j] = RND.nextInt(ParamActions.getNumberOfActions());
            }
        }
    }

    static public void main( String[] args) {

    }

}