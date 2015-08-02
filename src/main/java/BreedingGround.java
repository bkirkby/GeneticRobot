import java.util.LinkedList;
import java.util.Random;

public class BreedingGround {
    //it's ordered from top to bottom

    private static double MUTATION_RATE=0.05;
    private LinkedList<int[]> breederPop = new LinkedList<>();
    int p1Idx=0,p2Idx=1;
    private static Random RND = new Random(System.currentTimeMillis());

    public void addBreeder(int[] breeder) {
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
        if(++p2Idx == breederPop.size()) {//check if we go past the end
            p2Idx = 0;
        }
        if( p2Idx == p1Idx) {//check if we are going to breed with self
            if(++p1Idx == breederPop.size()) {
                p1Idx = 0;
            }
        }
    }

    public int[] getNextOfBrood() {
        int [] ret = crossBreedAndMutate( breederPop.get(p1Idx), breederPop.get(p2Idx));
        advancePartners();
        return ret;
    }
}