import javafx.util.Pair;

import java.util.*;

//TODO NEXT: code for reproduction

public class GeneticCode {
    private static int MAX_RUN_STEPS_ON_MAP=100;
    private static int MAX_POP_SIZE=20;
    private static int MAX_MAPS_TO_TEST=5;
    private static double ELITE_POPULATION=.1;
    private static double BREEDING_POPULATION=.25;

    private int[][] population = new int[MAX_POP_SIZE][Scenarios.getNumberOfScenes()];
    private static Random RND = new Random(System.currentTimeMillis());

    private double topScore = 0;

    public GeneticCode() {
        initPopulation();
    }

    private double fitnessFunction( int[] strategy, List<RobotMap> testRobotMaps) {
        double ret = 0;
        for( RobotMap rm : testRobotMaps) {
            ret += rm.scoreStrategy( strategy, MAX_RUN_STEPS_ON_MAP);
        }
        return ret/testRobotMaps.size();
    }
    private void initPopulation() {
        for(int i=0; i<MAX_POP_SIZE; i++) {
            for(int j=0; j<Scenarios.getNumberOfScenes(); j++) {
                population[i][j] = RND.nextInt(ParamActions.getNumberOfActions());
            }
        }
    }

    //this method repopulates the population based on the best candidates in the current population. it need to test
    //the current population for fitness
    public void repop() {
        //Pair is a tuple of key=population_idx and val=score
        TreeSet<Score> populationScores = new TreeSet<>();
        //setup the test maps
        final ArrayList<RobotMap> mapsForTest = new ArrayList<>();
        for(int i=0; i<MAX_MAPS_TO_TEST; i++) {
            mapsForTest.add(new RobotMap());
        }
        //now score our pop
        for(int i=0; i<population.length; i++) {
            populationScores.add(new Score(i, fitnessFunction(population[i], mapsForTest)));
        }
        //and create the breeding ground
        BreedingGround bg = new BreedingGround();
        int iBreeders=0;
        for(Score s : populationScores) {
            bg.addBreeder( population[s.popIdx]);
            if(++iBreeders >= population.length*BREEDING_POPULATION) {
                break;
            }
        }
        //take the top ELITE_POPULATION percent of the pop and copy them over.
        int [][] newPop = new int[MAX_POP_SIZE][];
        int i;
        for(i=0; i<population.length*ELITE_POPULATION; i++) {
            Score sc = populationScores.pollFirst();
            if(i==0){topScore = sc.score;}
            newPop[i] = population[sc.popIdx];
        }
        //now fill out the rest of the new population by breeding
        for(i=(int)(population.length*ELITE_POPULATION); i<population.length; i++) {
            newPop[i]=bg.getNextOfBrood();
        }
        population = newPop;
    }

    public double getTopScore(){
        return topScore;
    }

    private static class Score implements Comparable<Score> {
        public int popIdx;
        public double score;
        public Score(int popIdx, double score){
            this.popIdx = popIdx;
            this.score = score;
        }
        public String toString() {
            return "idx:"+popIdx+" score:"+score;
        }
        public int compareTo( Score s) {
            if( s==null){return -1;}
            if( s.score == this.score){return 0;}
            return s.score<this.score?-1:1;
        }
    }
    static public void main( String[] args) {
        GeneticCode gc = new GeneticCode();

        for(int i=0; i<1000; i++) {
            gc.repop();
            System.out.println(gc.getTopScore());
        }
    }
}