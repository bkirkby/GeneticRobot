package org.kirkby.genetic.robot.map;

import javafx.util.Pair;
import org.kirkby.genetic.robot.GeneticRobotProperties;
import org.kirkby.genetic.robot.Scenarios;

import java.util.*;

public class RobotMap {
    private char[][] map;
    private static Random RND = new Random(System.currentTimeMillis());
    private Pair<Integer,Integer> robotLoc;
    private int[] robotStrategy;
    private HashMap<String, Pair<Integer,Integer>> cansLoc = new HashMap<>();
    private int score =0;
    static public int PICK_SUCCESS_REWARD=10;
    static public int PICK_FAIL_PUNISHMENT=-1;
    static public int WALL_HIT_PUNISHMENT=-5;

    //action key: 0=skip, 1=north, 2=south, 3=east, 4=west, 5=random, 6=pickup
    public RobotMap() {
        robotLoc = new Pair(RND.nextInt(GeneticRobotProperties.getMapSize()),RND.nextInt(GeneticRobotProperties.getMapSize()));
        map = new char[GeneticRobotProperties.getMapSize()][GeneticRobotProperties.getMapSize()];
        for(int x=0; x<GeneticRobotProperties.getMapSize(); x++) {
            for(int y=0; y<GeneticRobotProperties.getMapSize(); y++) {
                if(RND.nextDouble()<=GeneticRobotProperties.getCanDisperseFactor()) {
                    map[x][y] = 'c';
                    cansLoc.put(Integer.toString(x) + "-" + Integer.toString(y), new Pair(x, y));
                } else {
                    map[x][y] = '.';
                }
            }
        }
    }
    public void printMap() {
        for(int y=0; y<GeneticRobotProperties.getMapSize(); y++) {
            StringBuffer row = new StringBuffer();
            for(int x=0; x<GeneticRobotProperties.getMapSize(); x++) {
                row.append(this.map[x][y]);
                row.append(' ');
            }
            System.out.println( row.toString());
        }
    }
    private String getCurrentScene() {
        String ret = "";
        int x=robotLoc.getKey();
        int y=robotLoc.getValue();
        //west
        ret += (x==0?'w':map[x-1][y]);
        //east
        ret += (x==GeneticRobotProperties.getMapSize()-1?'w':map[x+1][y]);
        //north
        ret += (y==0?'w':map[x][y-1]);
        //south
        ret += (y==GeneticRobotProperties.getMapSize()-1?'w':map[x][y+1]);
        //current
        ret += map[x][y];
        return ret;
    }
    //returns the action,score from the action
    public Pair<Integer,Integer> stepStrategy() {
        if( robotStrategy == null) {
            return null;
        }
        int action = robotStrategy[Scenarios.getSceneIdx(getCurrentScene())];
        int s=performAction(action);
        score+=s;
        return new Pair<Integer,Integer>(action, s);
    }
    public void setRobotStrategy( int[] strategy) {
        this.robotStrategy = strategy;
    }
    public int scoreStrategy( int maxSteps) {
        if( robotStrategy == null) {
            return 0;
        }
        score = 0;
        for( int i=0;i<maxSteps;i++) {
            score += stepStrategy().getValue();
        }
        return score;
    }
    private void setRobotLoc(int x, int y){
        robotLoc=new Pair(x,y);
    }
    public Pair<Integer,Integer> getRobotLoc() {
        return robotLoc;
    }
    public Collection<Pair<Integer, Integer>> getCansLoc() {
        return cansLoc.values();
    }
    //0=skip, 1=north, 2=south, 3=east, 4=west, 5=random, 6=trypick
    private int performAction( int action) {
        int x=robotLoc.getKey();
        int y=robotLoc.getValue();
        //for random move, randomize 1-4 inclusive which are the n,s,e,w movements
        if( action == 5){action = RND.nextInt(4)+1;}
        switch(action) {
            case 1:if(y==0){
                return WALL_HIT_PUNISHMENT;
            }else{
                setRobotLoc(x,--y);
                return 0;
            }
            case 2:if(y==GeneticRobotProperties.getMapSize()-1){return WALL_HIT_PUNISHMENT;}else{setRobotLoc(x,++y);return 0;}
            case 3:if(x==GeneticRobotProperties.getMapSize()-1){return WALL_HIT_PUNISHMENT;}else{setRobotLoc(++x,y);return 0;}
            case 4:if(x==0){return WALL_HIT_PUNISHMENT;}else{setRobotLoc(--x,y);return 0;}
            case 6: {
                if(map[x][y]=='c'){
                    map[x][y]='.';
                    cansLoc.remove( Integer.toString(x)+"-"+Integer.toString(y));
                    return PICK_SUCCESS_REWARD;
                }else{
                    return PICK_FAIL_PUNISHMENT;
                }
            }
            default: return 0;
        }
    }
    public static String actionIntToString(int action) {
        switch(action){
            case 0: return "stay";
            //case 1: return "north"; chnge these now for display purpose
            //case 2: return "south";
            case 1: return "south";
            case 2: return "north";
            case 3: return "east";
            case 4: return "west";
            case 5: return "random";
            case 6: return "pick";
            default: return "unknown";
        }
    }
    public int getScore(){
        return score;
    }
    /*public static void main(String[] args) {
        RobotMap rm = new RobotMap();
        rm.printMap();
        int[] strategy = new int[Scenarios.getNumberOfScenes()];
        //int[] strategy = new int[19683];
        for(int i=0; i<strategy.length; i++) {
            strategy[i] = RND.nextInt(ParamActions.getNumberOfActions());
        }

        System.out.println("score: "+rm.scoreStrategy(strategy,50));
    }*/
}
