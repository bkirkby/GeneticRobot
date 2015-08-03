import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RobotMap {
    private static int MAP_SIZE = 10;
    private char[][] map = new char[MAP_SIZE][MAP_SIZE];
    private static Random RND = new Random(System.currentTimeMillis());
    private Pair<Integer,Integer> robotLoc = new Pair(RND.nextInt(MAP_SIZE),RND.nextInt(MAP_SIZE));
    private double CAN_POP_FACTOR = .5;
    //action key: 0=skip, 1=north, 2=south, 3=east, 4=west, 5=random, 6=pickup
    public RobotMap() {
        for(int x=0; x<MAP_SIZE; x++) {
            for(int y=0; y<MAP_SIZE; y++) {
                if(RND.nextDouble()>CAN_POP_FACTOR) {
                    map[x][y] = 'c';
                } else {
                    map[x][y] = '.';
                }
            }
        }
    }
    public void printMap() {
        for(int y=0; y<MAP_SIZE; y++) {
            StringBuffer row = new StringBuffer();
            for(int x=0; x<MAP_SIZE; x++) {
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
        ret += (x==MAP_SIZE-1?'w':map[x+1][y]);
        //north
        ret += (y==0?'w':map[x][y-1]);
        //south
        ret += (y==MAP_SIZE-1?'w':map[x][y+1]);
        //current
        ret += map[x][y];
        return ret;
    }
    public int scoreStrategy( int[] strategy, int maxSteps) {
        int score = 0;
        for( int i=0;i<maxSteps;i++) {
            score += performAction(strategy[Scenarios.getSceneIdx(getCurrentScene())]);
        }
        return score;
    }
    private void setRobotLoc(int x, int y){
        robotLoc=new Pair(x,y);
    }
    //0=skip, 1=north, 2=south, 3=east, 4=west, 5=random, 6=trypick
    private int performAction( int action) {
        int x=robotLoc.getKey();
        int y=robotLoc.getValue();
        //for random move, randomize 1-4 inclusive which are the n,s,e,w movements
        if( action == 5){action = RND.nextInt(4)+1;}
        switch(action) {
            case 1:if(y==0){return -5;}else{setRobotLoc(x,--y);return 0;}
            case 2:if(y==MAP_SIZE-1){return -5;}else{setRobotLoc(x,++y);return 0;}
            case 3:if(x==MAP_SIZE-1){return -5;}else{setRobotLoc(++x,y);return 0;}
            case 4:if(x==0){return -5;}else{setRobotLoc(--x,y);return 0;}
            case 6:if(map[x][y]=='c'){map[x][y]='.';return 10;}else{return -1;}
            default: return 0;
        }
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
