import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Created by kirkby on 8/2/15.
 */
public class MainApplication extends Application implements NewGenerationScoredListener {
    private GeneticCode gc;
    private XYChart.Series series = new XYChart.Series();
    private static int NUM_GENERATIONS = 1000;
    private Thread geneticCodeThread;
    TextArea generationTA = new TextArea();

    @Override
    public void newGenerationScored(int generationNumber, Strategy strat) {
        //TODO: concurrent modification probs: http://docs.oracle.com/javafx/2/api/javafx/concurrent/Task.html
        series.getData().add(new XYChart.Data(generationNumber, strat.getScore()));
        generationTA.setText(generationNumber + "    " + strat.getScore()+"\n"+generationTA.getText());
    }

    @Override
    public void start(Stage stage) throws Exception {
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
        series.setName("genetic brood");

        VBox vbox = new VBox();

        Scene scene = new Scene( new Group(), 700, 600);
        scene.getStylesheets().add("MainApplication.css");
        lineChart.getData().add(series);

        //tabs!!!
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //setup the reproduction tab
        {
            VBox reproductionVbox = new VBox();
            final Button start = new Button("start");
            start.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (start.getText() == "start") {
                        startReproducing();
                        start.setText("pause");
                    } else if (start.getText() == "pause") {
                        gc.pauseGeneration();
                        start.setText("continue");
                    } else if (start.getText() == "continue") {
                        gc.contGeneration();
                        start.setText("pause");
                    }
                }
            });
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.getChildren().addAll(start);
            hbox.setPadding(new Insets(10, 10, 10, 50));
            generationTA.setPrefRowCount(4);
            generationTA.setPadding(new Insets(0, 5, 0, 5));
            generationTA.setStyle(
                    "-fx-text-fill: #00ff00;" +
                            "-fx-font-family: Verdana;" +
                            "-fx-font-size: 10;" +
                            "-fx-font-weight: bold;"
            );
            generationTA.setPrefWidth(150);
            generationTA.setMaxWidth(150);
            generationTA.setPrefHeight(450);

            HBox h = new HBox();
            h.getChildren().addAll(lineChart, generationTA);
            h.setPrefHeight(450);
            h.setPrefWidth(800);

            //reproductionVbox.getChildren().addAll(hbox, lineChart, generationTA);
            reproductionVbox.getChildren().addAll(hbox, h);
            Tab genTab = new Tab();
            genTab.setText("reproduce");
            genTab.setContent(reproductionVbox);
            tabPane.getTabs().add(genTab);
        }

        //setup the robot map tab
        {
            VBox robotMapVBox = new VBox();
            final Button start = new Button("start");
            start.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //TODO: run a strat simulation
                }
            });

            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.getChildren().addAll(start);
            hbox.setPadding(new Insets(10, 10, 10, 50));
            //robotMapVBox.getChildren().addAll(hbox, lineChart);
            Tab mapTab = new Tab();
            mapTab.setText("test a strat");
            mapTab.setContent(robotMapVBox);
            tabPane.getTabs().add(mapTab);
        }

        vbox.getChildren().addAll(tabPane);
        tabPane.setPrefWidth( 800);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setOnCloseRequest( new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
                if( gc!= null) {
                    gc.stopGeneration();
                }
            }
        });

        stage.setScene(scene);
        stage.show();

        //this has to be here cause it's a hac to get the background color of the textarea changed
        Region reg = (Region)generationTA.lookup(".content");
        reg.setStyle("-fx-background-color: black;");
    }

    public void startReproducing() {
        //setup the robot code
        gc = new GeneticCode(NUM_GENERATIONS);
        gc.addNewGenerationScoredListener( this);
        gc.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Overrid //todo: concurrency issues
            public void handle(WorkerStateEvent event) {

            }
        });
        geneticCodeThread = new Thread(gc);
        geneticCodeThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
