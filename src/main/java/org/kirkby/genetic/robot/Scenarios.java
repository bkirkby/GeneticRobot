package org.kirkby.genetic.robot;

import java.util.*;

/* this class has both utility and data function. the data is contained in SCENES and was generated by the utility
   function of this class. the utility function (genNewScenes) will take in a String that represents the number of
   parameters for each scenario (represented by the length of the string) along with the values that each parameter
   could contain (represented by the unique characters found in the string.

   it will then generate all possible permutations of the unique values in each parameter slot. the index of each
   resulting string represents what parameter it is for all scenarios.

   in our robot example, each parameter represents what we know about the west, east, north, south, and current squares.
   each of them can all contain one of three values: either be empty (.), have a can (c), or have a wall (w).

   as an example, the first scenario is represented as "wcccc" which means that in this scenario we are seeing a
   wall(w) to the west(0 idx), a can(c) to the east(1 idx), a can(c) to the north(2 idx), a can(c) to the south(3 idx)
   and a can(c) in the current location (4 idx)

   with 3 different possible values contained in 5 different parameters, we have a total of 3^9=243 possible scenarios
   the robot will find himself in. it should be noted that some scenarios are actually not possible. for example, any
   scenario that says there is a wall (w) at the current location (idx 4) is impossible since the system won't let the
   robot move into a wall. there are 3^4=81 different scenarios that have this condition and they could safely be removed
   without affecting the outcome. this is an optimization that is not necessary for 243 scenarios, but might be wise in
   applications with a larger number of parameters and/or possible values.

   another example would be for any map of functional size (i.e. 4x4 or greater), you would never have a scenario where
   there exists a wall to the north AND the south (3^3=27 scenarios) or to the east AND the west (3^3=27 scenarios).

   TODO: figure out the formula to calc how many can be removed 81+(27-9)+(27-9-2)=115
   TODO: try it with 9 param scenarios which includes knowledge of nw,sw,ne,se but not able to move there
   TODO: try it with 9 param scenarios which includes knowledge of nw,sw,ne,se but IS able to move there (four more actions)
   TODO: test to see what would happen if robot had knowledge of what square it came from
 */

public class Scenarios {
    // '.'=empty, 'c'=can, 'w'=wall
    // SCENES[][0]=west, SCENES[][1]=east, SCENES[][2]=north, SCENES[][3]=south, SCENES[][4]=current
    private static HashMap<String, Integer> SCENES = new HashMap<String,Integer>(){{
        put("ccccc",0); put("c.c.c",1); put(".wc..",2); put("c.www",3); put("cw..c",4); put("wwc.w",5); put("c..ww",6); put("w...c",7); put("cww.c",8); put("ccccw",9);
        put("w.w.c",10); put(".c..w",11); put(".wwwc",12); put(".cw.w",13); put(".cw.c",14); put(".w.w.",15); put("w.w..",16); put("cwcww",17); put("c.c.w",18); put("..w..",19);
        put("cww..",20); put("wwcc.",21); put("wcwc.",22); put(".wwww",23); put("w.cww",24); put("..cwc",25); put(".....",26); put("wc.c.",27); put(".c..c",28); put("w..c.",29);
        put(".wwcw",30); put("wc.w.",31); put("cwwc.",32); put("wwc..",33); put("w.wc.",34); put("c.cc.",35); put(".c.cc",36); put("cw.c.",37); put(".wc.c",38); put("ww.wc",39);
        put(".w.ww",40); put("..cww",41); put("w...w",42); put("wcwcw",43); put(".www.",44); put("cww.w",45); put("wwwwc",46); put("....w",47); put("cw..w",48); put("w.w.w",49);
        put("wc.cw",50); put("ww.ww",51); put(".w.wc",52); put("wcwcc",53); put(".c.c.",54); put("c.ccc",55); put("..w.c",56); put("wwwww",57); put("wc.cc",58); put(".cwc.",59);
        put("cccw.",60); put("wwc.c",61); put("....c",62); put("ww.cw",63); put("ccwwc",64); put("w.ccw",65); put("c.wcc",66); put("cwccw",67); put("c..cc",68); put("wccww",69);
        put("cc.ww",70); put(".w.c.",71); put("wwwcw",72); put("..cc.",73); put("wcw..",74); put(".ccw.",75); put("ccwww",76); put("wc...",77); put("wccwc",78); put("ccc.c",79);
        put("c..cw",80); put("ccc..",81); put("cwccc",82); put("ww.w.",83); put("c.wcw",84); put("wwww.",85); put(".wwcc",86); put("w.ccc",87); put(".w..w",88); put("c..w.",89);
        put(".cccw",90); put("cc.wc",91); put(".ww.c",92); put(".w.cw",93); put("wwwc.",94); put("..ccw",95); put("c.ww.",96); put("cccc.",97); put("wcw.w",98); put("c.c..",99);
        put("cwcwc",100); put("wc..w",101); put("cw...",102); put(".cw..",103); put("w....",104); put(".ww.w",105); put(".ccww",106); put(".w.cc",107); put("w.cwc",108); put("wcw.c",109);
        put("..ccc",110); put("w.cw.",111); put("..cw.",112); put("c..wc",113); put(".c...",114); put(".ccwc",115); put("cwcw.",116); put("ccc.w",117); put("wc..c",118); put("ww.cc",119);
        put("c.wwc",120); put("wwwcc",121); put(".wwc.",122); put("ww..c",123); put("w.c.c",124); put("w.www",125); put("www.c",126); put("cwwww",127); put("wcccc",128); put(".ww..",129);
        put("cc.cc",130); put("cwc.c",131); put(".ccc.",132); put("..ww.",133); put("c...c",134); put("cwc.w",135); put("ww..w",136); put("ccwcc",137); put("w.c.w",138); put("...w.",139);
        put(".cc.w",140); put("w..wc",141); put("ccwc.",142); put("c...w",143); put("www.w",144); put("cwwwc",145); put("...wc",146); put("c.w.c",147); put(".wcw.",148); put("cw.wc",149);
        put("w.wwc",150); put("cw.ww",151); put(".cc.c",152); put(".cwww",153); put("w..ww",154); put("wccc.",155); put("ww.c.",156); put("c.w.w",157); put("..c..",158); put(".w..c",159);
        put("cc.w.",160); put("..c.c",161); put("c..c.",162); put("..www",163); put("wwcwc",164); put("ww...",165); put("ccww.",166); put("...ww",167); put("www..",168); put("wccw.",169);
        put(".cccc",170); put("..wwc",171); put("c.wc.",172); put("cc.cw",173); put(".wcww",174); put("wwcww",175); put("ccwcw",176); put("cwcc.",177); put("wcccw",178); put("..c.w",179);
        put("w.cc.",180); put(".wcwc",181); put(".w...",182); put("cccww",183); put("w.wcc",184); put("...c.",185); put(".c.w.",186); put("c.ccw",187); put("cw.cw",188); put("cwwcc",189);
        put("w..cw",190); put("wcwwc",191); put(".cww.",192); put("wc.wc",193); put("w.wcw",194); put(".wcc.",195); put("cwwcw",196); put("..w.w",197); put("wwcw.",198); put(".cwcw",199);
        put("c.cw.",200); put("wcc..",201); put("cc...",202); put("cccwc",203); put("cw.cc",204); put(".cwcc",205); put("w..cc",206); put("ccw..",207); put(".c.cw",208); put("wcww.",209);
        put("..wc.",210); put(".wc.w",211); put("ccw.w",212); put("cwc..",213); put("..wcc",214); put(".c.ww",215); put("w.c..",216); put(".c.wc",217); put(".wccw",218); put("...cc",219);
        put(".cwwc",220); put("cc.c.",221); put("wcc.w",222); put("cc..c",223); put(".wccc",224); put("c.w..",225); put("wwccc",226); put("c....",227); put("w..w.",228); put("c.cwc",229);
        put("ccw.c",230); put("cwww.",231); put("wcwww",232); put("c.cww",233); put("wwccw",234); put("w.ww.",235); put("..wcw",236); put("cc..w",237); put("wcc.c",238); put(".cc..",239);
        put("cw.w.",240); put("wc.ww",241); put("...cw",242);
    }};

    static public Iterable<String> genNewScenes(String sceneParamVals) {
        HashSet<String> scenes = new HashSet<String>();
        char[] data = new char[sceneParamVals.length()];
        recurInclusivePermutation(sceneParamVals,data,sceneParamVals.length()-1, 0, scenes);
        return scenes;
    }
    //this method takes a set of characters found in str
    static private void recurInclusivePermutation(String str, char[] data, int last, int idx, Collection<String> scenes) {
        int i, len = str.length();
        for(i=0; i<len; i++) {
            data[idx] = str.charAt(i);
            if(idx==last){
                scenes.add(new String(data));
            } else {
                recurInclusivePermutation(str, data, last, idx+1, scenes);
            }
        }
    }
    static public int getNumberOfScenes() {
        return SCENES.size();
    }
    static public int getSceneIdx(String s) {
        return SCENES.get(s);
    }
    static public void main(String[] args) {
        Iterable<String> sc = genNewScenes("...cw");
        int count=0;
        for( String s : sc) {
            System.out.print("put(\"" + s + "\","+count+"); ");
            if( (count !=0) && (count%7 == 0)) {
                System.out.println();
            }
            count++;
        }
        System.out.println();
        System.out.println("total: "+count);
    }
}