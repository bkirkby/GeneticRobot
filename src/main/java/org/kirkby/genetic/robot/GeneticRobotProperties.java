package org.kirkby.genetic.robot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by kirkby on 8/3/15.
 */
//https://docs.oracle.com/javase/tutorial/essential/environment/properties.html
public class GeneticRobotProperties {
    private static int NUM_ITRS_ON_MAP=75;
    private static int NUM_MAPS_FOR_FITNESS_FUNCTION=20;
    private static double ELITE_POPULATION_FACTOR=.05;
    private static double BREEDER_POPULATION_FACTOR=.2;
    private static int BREEDER_POPULATION_SIZE=150;
    private static int NUMBER_OF_GENERATIONS=1000;
    private static int MAP_SIZE=10;
    private static double CAN_DISPERSE_FACTOR=.5;

    private static Properties props;

    public static int getNumberOfGenerations() {
        return NUMBER_OF_GENERATIONS;
    }
    public static void setNumberOfGenerations(int g) {
        NUMBER_OF_GENERATIONS=g;
    }
    public static int getNumberOfIterationsOnMap() {
        return NUM_ITRS_ON_MAP;
    }
    public static void setNumberOfIterationsOnMap(int i) {
        NUM_ITRS_ON_MAP = i;
    }
    public static double getElitePopulationFactor() {
        return ELITE_POPULATION_FACTOR;
    }

    public static int getNumMapsForFitnessFunction() {
        return NUM_MAPS_FOR_FITNESS_FUNCTION;
    }

    public static double getBreederPopulationFactor() {
        return BREEDER_POPULATION_FACTOR;
    }

    public static int getBreederPopulationSize() {
        return BREEDER_POPULATION_SIZE;
    }

    public static int getMapSize() {
        return MAP_SIZE;
    }

    public static double getCanDisperseFactor() {
        return CAN_DISPERSE_FACTOR;
    }

    public static void setNumMapsForFitnessFunction(int numMapsForFitnessFunction) {
        NUM_MAPS_FOR_FITNESS_FUNCTION = numMapsForFitnessFunction;
    }

    public static void setElitePopulationFactor(double elitePopulationFactor) {
        ELITE_POPULATION_FACTOR = elitePopulationFactor;
    }

    public static void setBreederPopulationFactor(double breederPopulationFactor) {
        BREEDER_POPULATION_FACTOR = breederPopulationFactor;
    }

    public static void setBreederPopulationSize(int breederPopulationSize) {
        BREEDER_POPULATION_SIZE = breederPopulationSize;
    }

    public static void setMapSize(int mapSize) {
        MAP_SIZE = mapSize;
    }

    public static void setCanDisperseFactor(double canDisperseFactor) {
        CAN_DISPERSE_FACTOR = canDisperseFactor;
    }

    public static void initProps() {
        if( props == null)  {
            props = new Properties();
            try {
                FileInputStream fis = new FileInputStream("default.properties");
                props.load( fis);
                fis.close();
            } catch( FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch( IOException ioe) {
                ioe.printStackTrace();
            }
            for( String s : props.stringPropertyNames()) {
                System.out.println( s);
            }
        }
    }

}
