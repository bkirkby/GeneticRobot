package org.kirkby.genetic.robot;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Strategy {
    private StringProperty generation;
    private int generationNumber;
    private StringProperty score;
    private double scoreNumber;
    private int [] strategy;
    public Strategy( int generation, double score, int [] strategy) {
        generationNumber = generation;
        scoreNumber = score;
        this.score = new SimpleStringProperty(Double.toString(score));
        this.generation = new SimpleStringProperty(Integer.toString(generation));
        this.strategy = strategy;
    }
    //generation
    public void setGeneration( String v) {
        generationProperty().set(v);
    }
    public String getGeneration() {
        return generationProperty().get();
    }
    public StringProperty generationProperty() {
        if( generation == null) {
            generation = new SimpleStringProperty(this,"generation");
        }
        return generation;
    }
    public int getGenerationNumber() {
        return generationNumber;
    }

    //score
    public void setScore( String v) {
        scoreProperty().set(v);
    }
    public String getScore() {
        return scoreProperty().get();
    }
    public StringProperty scoreProperty() {
        if( score == null) {
            score = new SimpleStringProperty(this,"score");
        }
        return score;
    }
    public double getScoreNumber() {
        return scoreNumber;
    }

    public int[] getStrategy() {
        return strategy;
    }

}