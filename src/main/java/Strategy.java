public class Strategy {
    private double score;
    private int [] strategy;
    public Strategy( double score, int [] strategy) {
        this.score = score;
        this.strategy = strategy;
    }
    public double getScore(){
        return score;
    }
    public int[] getStrategy(){
        return strategy;
    }
}