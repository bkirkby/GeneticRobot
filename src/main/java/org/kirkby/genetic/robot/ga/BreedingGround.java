package org.kirkby.genetic.robot.ga;

import org.kirkby.genetic.robot.Scenarios;
import org.kirkby.genetic.robot.map.ParamActions;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Random;

public class BreedingGround {
    //it's ordered from top to bottom

    private static double MUTATION_RATE=0.005;
    private LinkedList<int[]> breederPop = new LinkedList<>();
    int p1Idx=0;
    ArrayDeque<Integer> indexesOfP2s = new ArrayDeque<>();
    private static Random RND = new Random(System.currentTimeMillis());

    public void addBreeder(int[] breeder) {
        if( breederPop.isEmpty()) {
            p1Idx=0;
        } else {
            indexesOfP2s.push(breederPop.size());
        }
        breederPop.add( breeder);
    }

    private static int[] crossBreedAndMutate( int[] p1, int[] p2) {
        int[] ret = new int[p1.length];
        int split = RND.nextInt(Scenarios.getNumberOfScenes());
        int i;
        for( i=0; i<split; i++) {
            ret[i]=RND.nextDouble()<=MUTATION_RATE?RND.nextInt(ParamActions.getNumberOfActions()):p1[i];
        }
        for( i=split; i<p2.length; i++) {
            ret[i]=RND.nextDouble()<=MUTATION_RATE?RND.nextInt(ParamActions.getNumberOfActions()):p2[i];
        }
        return ret;
    }

    private void advancePartners() {
        if( indexesOfP2s.isEmpty()) {
            if(++p1Idx >= breederPop.size()){
                p1Idx=0;
            }
            for(int i=0; i<breederPop.size(); i++) {
                if( i != p1Idx) {
                    indexesOfP2s.push(i);
                }
            }
        }
    }

    public int[] getNextOfBrood() {
        int [] ret = crossBreedAndMutate( breederPop.get(p1Idx), breederPop.get(indexesOfP2s.pop()));
        advancePartners();
        return ret;
    }
}