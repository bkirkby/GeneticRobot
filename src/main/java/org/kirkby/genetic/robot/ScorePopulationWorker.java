package org.kirkby.genetic.robot;

import javafx.concurrent.Task;
import org.kirkby.genetic.robot.org.kirkby.genetic.robot.ga.GeneticPopulation;
import org.kirkby.genetic.robot.org.kirkby.genetic.robot.map.RobotMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kirkby on 8/3/15.
 */
public class ScorePopulationWorker extends Task<ArrayList<GeneticPopulation.Score>> {
    private int[][] population;

    public ScorePopulationWorker(int[][] population) {
        this.population = population;
    }

    private List<RobotMap> generateMaps( int numMaps) {
        ArrayList<RobotMap> ret = new ArrayList<>();
        for(int j=0; j< numMaps; j++) {
            ret.add(new RobotMap());
        }
        return ret;
    }
    private double fitnessFunction( int[] strategy) {
        List<RobotMap> testRobotMaps = generateMaps( GeneticRobotProperties.getNumMapsForFitnessFunction());
        double ret = 0;

        for( RobotMap rm : testRobotMaps) {
            rm.setRobotStrategy(strategy);
            ret += rm.scoreStrategy( GeneticRobotProperties.getNumberOfIterationsOnMap());
        }
        return ret/testRobotMaps.size();
    }

    @Override
    protected ArrayList<GeneticPopulation.Score> call() throws Exception {
        ArrayList<GeneticPopulation.Score> populationScores = new ArrayList<>();
        for(int i=0; i<population.length; i++) {
            populationScores.add(new GeneticPopulation.Score(i, fitnessFunction(population[i])));
        }
        Collections.sort(populationScores);
        return populationScores;
    }
}
