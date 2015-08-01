import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RobotMap {
    private static int MAP_SIZE = 5;
    private char[][] map = new char[MAP_SIZE][MAP_SIZE];
    private static Random RND = new Random();
    private Pair<Integer,Integer> robotLoc = new Pair(RND.nextInt(MAP_SIZE),RND.nextInt(MAP_SIZE));
    //action key: 0=skip, 1=north, 2=south, 3=east, 4=west, 5=random, 6=pickup
    private static char[] ACTIONMAP = new char[]{'-','n','s','e','w','r','p'};
    public RobotMap() {
        for(int x=0; x<MAP_SIZE; x++) {
            for(int y=0; y<MAP_SIZE; y++) {
                if(RND.nextFloat()>.5) {
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
    //s is an index of condition values at the location. s[0]=west, s[1]=east, s[2]=north, s[3]=south, s[4]=current
    private boolean testScenarioCondition(String s) {
        int x = robotLoc.getKey();
        int y = robotLoc.getValue();
        //the following conditionals && statements check the following:
        //   *  s.charAt(i)=='w'&&x|y==0|MAP_SIZE-1 : check to see if we are supposed to see a wall on the edge. if so and we are on the edge, then it passes
        //   *  x|y!=MAP_SIZE-1|0 : a boundary check for looking inside the map array
        //   *  map[x[+|-]1][y[+|-]1]==s.charAt(i) : see if this matches the conditional check for that specific location. if all of these are true, then we have our condition is true
        if( ((s.charAt(0)=='w'&&x==0) || (x!=0 && map[x-1][y]==s.charAt(0))) //west
                && ((s.charAt(1)=='w'&&x==MAP_SIZE-1) || (x!=MAP_SIZE-1 && map[x+1][y]==s.charAt(1))) //east
                && ((s.charAt(2)=='w'&&y==0) || (y!=0 && map[x][y-1]==s.charAt(2))) //north
                && ((s.charAt(3)=='w'&&y==MAP_SIZE-1) || (y!=MAP_SIZE-1 && map[x][y+1]==s.charAt(3))) //south
                && map[x][y]==s.charAt(4) //current
/*                && ((s.charAt(5)=='w'&&(x==0||y==0)) || (y!=0 && x!=0 && map[x-1][y-1]==s.charAt(5))) //nw
                && ((s.charAt(6)=='w'&&(x==0||y==MAP_SIZE-1)) || (y!=MAP_SIZE-1 && x!=0 && map[x-1][y+1]==s.charAt(6))) //sw
                && ((s.charAt(7)=='w'&&(x==MAP_SIZE-1||y==0)) || (y!=0 && x!=MAP_SIZE-1 && map[x+1][y-1]==s.charAt(7))) //ne
                && ((s.charAt(8)=='w'&&(x==MAP_SIZE-1||y==MAP_SIZE-1)) || (y!=MAP_SIZE-1 && x!=MAP_SIZE-1 && map[x+1][y+1]==s.charAt(8))) //se*/
                ){
            System.out.print(s+":");
            return true;
        }
        return false;
    }
    public int scoreStrategy( int[] strategy, int maxSteps) {
        int score = 0;
        for( int j=0;j<maxSteps;j++) {
            int i = 0;
            System.out.print(""+robotLoc.getKey()+","+robotLoc.getValue()+":");
            //find which scenario we are in
            for (String s : Scenarios.getScenes()) {
                if (testScenarioCondition(s)) {
                    System.out.print(getActionChar(strategy[i])+":");
                    score += performAction(strategy[i]);
                    System.out.println( score);
                    printMap();
                    break;
                }
                i++;
            }
        }
        return score;
    }
    private void setRobotLoc(int x, int y){
        robotLoc=new Pair(x,y);
    }
    private static char getActionChar(int action){
        return ACTIONMAP[action];
    }
    //0=skip, 1=north, 2=south, 3=east, 4=west, 5=random, 6=trypick
    private int performAction( int action) {
        int x=robotLoc.getKey();
        int y=robotLoc.getValue();
        //for random move, randomize 1-4 inclusive which are the n,s,e,w movements
        if( action == 5){action = RND.nextInt(4)+1;}
        System.out.print(getActionChar(action) + ":");
        switch(action) {
            case 1:if(y==0){return -5;}else{setRobotLoc(x,y--);return 0;}
            case 2:if(y==MAP_SIZE-1){return -5;}else{setRobotLoc(x,y++);return 0;}
            case 3:if(x==MAP_SIZE-1){return -5;}else{setRobotLoc(x++,y);return 0;}
            case 4:if(x==0){return -5;}else{setRobotLoc(x--,y);return 0;}
            case 6:if(map[x][y]=='c'){map[x][y]='.';return 10;}else{return -1;}
            default: return 0;
        }
    }
    public static void main(String[] args) {
        RobotMap rm = new RobotMap();
        rm.printMap();
        int[] strategy = new int[243];
        //int[] strategy = new int[19683];
        for(int i=0; i<strategy.length; i++) {
            strategy[i] = RND.nextInt(7);
        }

        System.out.println("score: "+rm.scoreStrategy(strategy,50));
    }
}
