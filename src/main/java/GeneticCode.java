import java.util.*;

public class GeneticCode implements Runnable {
    private static int NUM_ITRS_ON_MAP =200;
    private static int NUM_MAPS_TO_TEST =200;
    private static double ELITE_POPULATION=0.05;
    private static double BREEDING_POPULATION_FACTOR =.25;
    private static int POP_SIZE =200;

    private int[][] population = new int[POP_SIZE][Scenarios.getNumberOfScenes()];
    private static Random RND = new Random(System.currentTimeMillis());

    private int maxGenerations=1000;
    private Vector<Strategy> generationHistory = new Vector<>();

    private Vector<NewGenerationScoredListener> newGenerationListeners = new Vector<>();

    private boolean paused = false;
    private boolean stopped = false;

    public GeneticCode( int maxGenerations) {
        this.maxGenerations=maxGenerations;
        initPopulation();
    }

    private List<RobotMap> generateMaps( int numMaps) {
        ArrayList<RobotMap> ret = new ArrayList<>();
        for(int j=0; j< numMaps; j++) {
            ret.add(new RobotMap());
        }
        return ret;
    }

    private double fitnessFunction( int[] strategy) {
        List<RobotMap> testRobotMaps = generateMaps(NUM_MAPS_TO_TEST);
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
            populationScores.add(new Score(i, fitnessFunction(population[i])));
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
        generationHistory.add( new Strategy( populationScores.get(0).score, population[populationScores.get(0).popIdx]));
        fireNewGenerationScoredEvent( generationHistory.size(), generationHistory.lastElement());
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

    public Strategy getLatestGeneration() {
        return generationHistory.lastElement();
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
    public void run() {
        for(int i=0; i<this.maxGenerations && stopped==false; i++) {
            if(!paused) {
                generationNext();
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
    private void fireNewGenerationScoredEvent( int generationNumber, Strategy generation) {
        for( NewGenerationScoredListener l : newGenerationListeners) {
            l.newGenerationScored( generationNumber, generation);
        }
    }
    public void addNewGenerationScoredListener( NewGenerationScoredListener listener) {
        newGenerationListeners.add( listener);
    }

    public void pauseGeneration() {
        paused = true;
    }
    public void contGeneration() {
        paused = false;
    }
    public void stopGeneration() {
        stopped = true;
    }

    static public void main( String[] args) {
        GeneticCode gc = new GeneticCode(1000);
        gc.addNewGenerationScoredListener(new NewGenerationScoredListener() {
            @Override
            public void newGenerationScored(int generationNumber, Strategy strategy) {
                System.out.println(generationNumber + "    " + strategy.getScore());
            }
        });

        (new Thread(gc)).start();

        int [] bestStrat = gc.getLatestGeneration().getStrategy();
        for(int i=0; i<bestStrat.length; i++) {
            System.out.print(bestStrat[i]+",");
            if( i!=0 && i%60==0){
                System.out.println();
            }
        }
    }
}