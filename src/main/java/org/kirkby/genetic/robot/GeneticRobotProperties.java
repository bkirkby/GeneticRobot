package org.kirkby.genetic.robot;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by kirkby on 8/3/15.
 */
//https://docs.oracle.com/javase/tutorial/essential/environment/properties.html
public class GeneticRobotProperties {
    private Integer NUM_ITRS_ON_MAP;
    private Integer NUM_MAPS_FOR_FITNESS_FUNCTION;
    private double ELITE_POPULATION_FACTOR;
    private double BREEDER_POPULATION_FACTOR;
    private Integer BREEDER_POPULATION_SIZE;
    private Integer NUMBER_OF_GENERATIONS;
    private Integer MAP_SIZE;
    private Double CAN_DISPERSE_FACTOR;

    private static GeneticRobotProperties props;
    private static ArrayList<PropertiesChangedListener> propertyChangeListeners = new ArrayList<>();

    public static GeneticRobotProperties addPropertyChangeListener( PropertiesChangedListener l) {
        propertyChangeListeners.add( l);
        return getProps();
    }

    public static void firePropertyChanged() {
        for( PropertiesChangedListener pcl : propertyChangeListeners) {
            pcl.propertiesChanged( getProps());
        }
    }

    public static GeneticRobotProperties getProps(){
        if( props == null) {
            props = new GeneticRobotProperties();
        }
        return props;
    }

    private GeneticRobotProperties() {
        initProps();
    }

    public Integer getNumberOfGenerations() {
        return NUMBER_OF_GENERATIONS;
    }
    public  void setNumberOfGenerations(Integer g) {
        NUMBER_OF_GENERATIONS=g;
        firePropertyChanged();
    }
    public  Integer getNumberOfIterationsOnMap() {
        return NUM_ITRS_ON_MAP;
    }
    public  void setNumberOfIterationsOnMap(Integer i) {
        NUM_ITRS_ON_MAP = i;
        firePropertyChanged();
    }
    public  double getElitePopulationFactor() {
        return ELITE_POPULATION_FACTOR;
    }

    public  Integer getNumMapsForFitnessFunction() {
        return NUM_MAPS_FOR_FITNESS_FUNCTION;
    }

    public  void setNumMapsForFitnessFunction(Integer numMapsForFitnessFunction) {
        NUM_MAPS_FOR_FITNESS_FUNCTION = numMapsForFitnessFunction;
        firePropertyChanged();
    }

    public  double getBreederPopulationFactor() {
        return BREEDER_POPULATION_FACTOR;
    }

    public  void setBreederPopulationFactor(double breederPopulationFactor) {
        BREEDER_POPULATION_FACTOR = breederPopulationFactor;
        firePropertyChanged();
    }

    public  Integer getBreederPopulationSize() {
        return BREEDER_POPULATION_SIZE;
    }

    public  void setBreederPopulationSize(Integer breederPopulationSize) {
        BREEDER_POPULATION_SIZE = breederPopulationSize;
        firePropertyChanged();
    }

    public  Integer getMapSize() {
        return MAP_SIZE;
    }

    public  void setMapSize(Integer mapSize) {
        MAP_SIZE = mapSize;
        firePropertyChanged();
    }

    public  Double getCanDisperseFactor() {
        return CAN_DISPERSE_FACTOR;
    }

    public  void setCanDisperseFactor(Double canDisperseFactor) {
        CAN_DISPERSE_FACTOR = canDisperseFactor;
        firePropertyChanged();
    }

    public  void setElitePopulationFactor(double elitePopulationFactor) {
        ELITE_POPULATION_FACTOR = elitePopulationFactor;
    }

    private void initProps() {
        Properties props=new Properties();
        try {
            InputStream is = this.getClass().getResourceAsStream("/default.properties");
            props.load(is);
            is.close();
            for( Object o : props.keySet()) {
                String name = (String)o;
                if( name.compareTo("num-itrs-on-map")==0) {
                    this.setNumberOfIterationsOnMap( Integer.parseInt((String) props.get(o)));
                } else if(name.compareTo("num-maps-for-fitness-function")==0) {
                    this.setNumMapsForFitnessFunction(Integer.parseInt((String) props.get(o)));
                } else if(name.compareTo("elite-population-factor")==0) {
                    this.setElitePopulationFactor(Double.parseDouble((String) props.get(o)));
                } else if(name.compareTo("breeder-population-factor")==0) {
                    this.setBreederPopulationFactor(Double.parseDouble((String) props.get(o)));
                } else if(name.compareTo("breeder-population-size")==0) {
                    this.setBreederPopulationSize(Integer.parseInt((String) props.get(o)));
                } else if(name.compareTo("number-of-generations")==0) {
                    this.setNumberOfGenerations(Integer.parseInt((String) props.get(o)));
                } else if(name.compareTo("map-size")==0) {
                    this.setMapSize( Integer.parseInt((String)props.get(o)));
                } else if(name.compareTo("can-disperse-factor")==0) {
                    this.setCanDisperseFactor(Double.parseDouble((String) props.get(o)));
                }
            }
        } catch( IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
