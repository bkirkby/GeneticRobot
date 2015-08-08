package org.kirkby.genetic.robot.ga;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.kirkby.genetic.robot.*;
import org.kirkby.genetic.robot.map.ParamActions;

import java.util.*;

public class GeneticPopulation {
    private int[][] population;
    private static Random RND = new Random(System.currentTimeMillis());

    private Vector<Strategy> generationHistory = new Vector<>();

    private Vector<NewGenerationScoredListener> newGenerationListeners = new Vector<>();
    private EventHandler<WorkerStateEvent> generationCompleteEventHandler;

    public GeneticPopulation() {
        initPopulation();
    }

    private void initPopulation() {
        population = new int[GeneticRobotProperties.getBreederPopulationSize()][Scenarios.getNumberOfScenes()];
        for(int i=0; i< GeneticRobotProperties.getBreederPopulationSize(); i++) {
            for(int j=0; j< Scenarios.getNumberOfScenes(); j++) {
                population[i][j] = RND.nextInt(ParamActions.getNumberOfActions());
            }
        }
    }

    //this method repopulates the population based on the best candidates in the current population. it need to test
    //the current population for fitness
    public void genNextGeneration(){
        //now score our pop by calling a separate thread and awaiting the response. we do this so
        //we can write to the Javafx UI of MainApplication which gets called on succeeded state change
        //of a thread
        {
            final Task<ArrayList<Score>> task = new ScorePopulationWorker(population);
            task.setOnSucceeded(generationCompleteEventHandler);
            Thread t = new Thread(task);
            t.start();
            while( t.isAlive()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        ArrayList<Score> populationScores = task.getValue();
                        generationHistory.add(new Strategy(generationHistory.size()+1, populationScores.get(0).score,
                                population[populationScores.get(0).popIdx]));

                        //now create the breeding ground based on scores
                        BreedingGround bg = new BreedingGround();
                        int iBreeders=0;
                        for(Score s : populationScores) {
                            bg.addBreeder( population[s.popIdx]);
                            if(++iBreeders >= population.length* GeneticRobotProperties.getBreederPopulationFactor()) {
                                break;
                            }
                        }
                        //generate the new population by first saving the elite, then breeding
                        int [][] newPop = new int[GeneticRobotProperties.getBreederPopulationSize()][];
                        int i;
                        for(i=0; i<population.length* GeneticRobotProperties.getElitePopulationFactor(); i++) {
                            newPop[i] = population[populationScores.get(i).popIdx];
                        }
                        //fill out the rest of the new population by breeding
                        for(i=(int)(population.length* GeneticRobotProperties.getElitePopulationFactor()); i<population.length; i++) {
                            newPop[i]=bg.getNextOfBrood();
                        }
                        population = newPop;

                        fireNewGenerationScoredEvent();
                    }
                });
        }
    }

    public Strategy getLatestGeneration() {
        return generationHistory.lastElement();
    }

    public int getLatestGenerationNumber() {
        return generationHistory.size();
    }

    public static class Score implements Comparable<Score> {
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

    private void fireNewGenerationScoredEvent() {
        for( NewGenerationScoredListener l : newGenerationListeners) {
            l.newGenerationScored();
        }
    }
    public void setGenerationCompleteEventHandler( EventHandler<WorkerStateEvent> eh) {
        this.generationCompleteEventHandler = eh;
    }
    public void addNewGenerationScoredListener( NewGenerationScoredListener listener) {
        newGenerationListeners.add( listener);
    }

    static public void main( String[] args) {

    }
}