package org.kirkby.genetic.robot.map;

public class ParamActions {
    //action key: 0=skip, 1=north, 2=south, 3=east, 4=west, 5=random, 6=pickup
    private static char[] ACTIONMAP = new char[]{'-','n','s','e','w','r','p'};

    public static char getActionChar(int a) {
        return ACTIONMAP[a];
    }

    public static int getNumberOfActions() {
        return ACTIONMAP.length;
    }
}