import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Created by kirkby on 8/2/15.
 */
public class MainApplication extends Application implements NewGenerationScoredListener {
    private GeneticCode gc;
    private XYChart.Series series = new XYChart.Series();
    private static int NUM_GENERATIONS = 1000;

    @Override
    public void newGenerationScored(int generationNumber, Strategy strat) {
        series.getData().add(new XYChart.Data(generationNumber, strat.getScore()));
    }

    @Override
    public void start(Stage stage) throws Exception {
        //setup the robot code
        gc = new GeneticCode(NUM_GENERATIONS);
        gc.addNewGenerationScoredListener( this);
        (new Thread(gc)).start();

        stage.setTitle("robot strategy performance by generation");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("generation");
        yAxis.setLabel("avg score");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setCreateSymbols(false);

        lineChart.setTitle("robot strategy performance by generation");
        //defining a series
        //XYChart.Series series = new XYChart.Series();
        series.setName("generation x performance");

        Scene scene  = new Scene(lineChart,800,600);
        scene.getStylesheets().add("MainApplication.css");
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
