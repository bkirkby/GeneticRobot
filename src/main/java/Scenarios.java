import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Scenarios {
    // '.'=empty, 'c'=can, 'w'=wall
    // west, east, north, south, current
    private static String[] SCENES = {"wcccc", "wwwww", "wc.c.", "w.ww.", "w.w.c", ".c..c", "ccwcc", "c..cc", ".ww..", "w.cc.", "c..w.", "c.wc.",
            "wwwwc", "..w..", "c..cw", ".c.w.", "wwcw.", "wcc..", "cwcww", "..c..", "cc..w", ".c..w", "...c.", "cw.wc", "cccw.", "ccc.w", "..ccc",
            "wcwww", ".wcw.", "cc..c", "wccc.", "..ccw", "ccwc.", "cw.ww", "ccww.", "w.w..", "c..wc", "wcc.c", "w.wcw", "wwccc", "..w.c", "wcwwc",
            "wc.cc", ".c.wc", ".cwww", ".c.cw", "wwccw", ".c.ww", "w.wcc", "cwc..", "...cw", "ccc.c", "c.w..", "wc.cw", "..c.c", ".wcwc", "wccww",
            "ccc..", "cccc.", "..cc.", "..c.w", ".cc.w", "cwwc.", "wcc.w", "w.w.w", "...cc", "ccwcw", "wccwc", "wccw.", "..cw.", "wcccw", ".wcww",
            "cc...", "wcww.", ".cc.c", "cwcc.", "wc..w", "cwc.w", "cww.c", "wc.w.", "c.w.w", "ww.cw", "...ww", "cwc.c", "cw.cc", "wwwc.", "c.w.c",
            "..cwc", ".cccc", "cw.cw", "c..ww", "ccccc", "c.cww", "www..", ".wcc.", ".cwwc", "wwwcc", "ccccw", "cww..", "wc..c", ".wc..", "c.cwc",
            ".cc..", "wwcwc", "..cww", "w..cc", "wc...", "cc.wc", ".w...", ".c...", ".wccc", "w.wwc", "wwwcw", "wwww.", "www.w", "cwcw.", "...w.",
            ".cwcc", "...wc", "cwcwc", ".wccw", "wwcww", ".wc.c", "c.ccw", ".w..c", ".ccc.", "c.cw.", ".cwcw", "w..cw", "cww.w", "www.c", "ww.cc",
            ".cww.", "cc.ww", "w.www", "cw.w.", ".w.ww", "w...c", "c.ccc", "c...w", "c.c.c", "ww.c.", ".wc.w", ".cw.w", "..wc.", "w..c.", ".cw.c",
            ".wwcc", "cc.w.", "....w", "cc.cw", "w.c..", "wc.ww", ".cwc.", "c.cc.", ".w.wc", "c.www", "ccw..", "c.c.w", "c...c", "....c", ".ccw.",
            "cc.cc", "wc.wc", ".ccwc", "ccw.w", "cwccc", "w..w.", "..wcc", "cwwwc", "w.cw.", "ww.wc", "w.cwc", "cwccw", "ccw.c", "ww.ww", "..wcw",
            "w...w", ".ccww", "cw...", ".cccw", ".wwc.", ".....", ".w..w", ".w.w.", "cwwww", "w.wc.", "cwww.", "cw.c.", ".wwww", "w..wc", "wcwcc",
            "wcw.w", "ww..w", "cwwcw", "wwcc.", "wcw.c", ".c.c.", "cc.c.", "wcwcw", "ww..c", "cwwcc", "w.cww", "w..ww", "wwc.c", "..ww.", ".c.cc",
            "wwc..", "cw..c", "ww.w.", ".w.c.", "..w.w", ".wwwc", "w.ccw", "cw..w", ".wwcw", ".www.", "cccwc", "c.wwc", "c.c..", "c....", "w....",
            "ww...", "c.wcc", "wcwc.", "wcw..", "c..c.", ".cw..", ".w.cc", "cccww", "..wwc", "w.c.c", "wwc.w", ".ww.w", "w.ccc", "ccwwc", "c.ww.",
            ".ww.c", "ccwww", ".w.cw", "..www", "c.wcw", "w.c.w"
    };
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
    static public Iterable<String> getScenes() {
        return Arrays.asList(SCENES);
    }
    static public void main(String[] args) {
        Iterable<String> sc = genNewScenes(".......cw");
        int count=0;
        for( String s : sc) {
            System.out.print("\"" + s + "\", ");
            if( count++%10 == 9) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("total: "+count);
    }
}