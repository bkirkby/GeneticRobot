import java.util.*;

//TODO NEXT: code for reproduction

public class GeneticCode {
    private static int NUM_ITRS_ON_MAP =200;
    private static int POP_SIZE =200;
    private static int NUM_MAPS_TO_TEST =200;
    private static double ELITE_POPULATION=0.05;
    private static double BREEDING_POPULATION_FACTOR =.25;
    ArrayList<RobotMap> mapsToTest = new ArrayList<>();

    private int[][] population = new int[POP_SIZE][Scenarios.getNumberOfScenes()];
    private static Random RND = new Random(System.currentTimeMillis());

    private double topScore = -50000;
    private int[] bestStrategy;

    public GeneticCode() {
        initPopulation();
    }

    private double fitnessFunction( int[] strategy, List<RobotMap> testRobotMaps) {
        double ret = 0;
        for( RobotMap rm : testRobotMaps) {
            ret += rm.scoreStrategy( strategy, NUM_ITRS_ON_MAP);
        }
        return ret/testRobotMaps.size();
    }
    private void initPopulation() {
        for(int i=0; i< POP_SIZE; i++) {
            for(int j=0; j<Scenarios.getNumberOfScenes(); j++) {
                population[i][j] = RND.nextInt(ParamActions.getNumberOfActions());
            }
        }
    }

    //this method repopulates the population based on the best candidates in the current population. it need to test
    //the current population for fitness
    public void generationNext() {
        //Pair is a tuple of key=population_idx and val=score
        ArrayList<Score> populationScores = new ArrayList<>();

        //now score our pop
        for(int i=0; i<population.length; i++) {
            //setup the test maps
            for(int j=0; j< NUM_MAPS_TO_TEST; j++) {
                mapsToTest.add(new RobotMap());
            }
            populationScores.add(new Score(i, fitnessFunction(population[i], mapsToTest)));
            mapsToTest.clear();
        }
        Collections.sort( populationScores);
        //and create the breeding ground
        BreedingGround bg = new BreedingGround();
        int iBreeders=0;
        for(Score s : populationScores) {
            bg.addBreeder( population[s.popIdx]);
            if(++iBreeders >= population.length* BREEDING_POPULATION_FACTOR) {
                break;
            }
        }
        //take the top ELITE_POPULATION percent of the pop and copy them over.
        topScore = populationScores.get(0).score;
        bestStrategy = population[populationScores.get(0).popIdx];
        int [][] newPop = new int[POP_SIZE][];
        int i;
        for(i=0; i<population.length*ELITE_POPULATION; i++) {
            newPop[i] = population[populationScores.get(i).popIdx];
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

    public int[] getBestStrategy(){
        return bestStrategy;
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
            gc.generationNext();
            System.out.println(i+"    "+gc.getTopScore());
        }
        int [] bestStrat = gc.getBestStrategy();
        for(int i=0; i<bestStrat.length; i++) {
            System.out.print(bestStrat[i]+",");
            if( i%60==0){
                System.out.println();
            }
        }
    }
}