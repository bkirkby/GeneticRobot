import javafx.application.Application;
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
    private GeneticPopulation gc;
    private XYChart.Series series = new XYChart.Series();
    //private static int NUM_GENERATIONS = GeneticRobotProperties.getNumberOfGenerations();
    TextArea generationTA = new TextArea();
    private boolean paused = false;
    private boolean stopped = false;
    final private Button startButtonReproduction = new Button("start");
    final private Button propsButtonReproduction = new Button("properties");

    @Override
    public void start(final Stage stage) throws Exception {
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
            startButtonReproduction.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (startButtonReproduction.getText() == "start") {
                        propsButtonReproduction.setDisable(true);
                        generationTA.setText("initializing...\n" + generationTA.getText());
                        startReproducing();
                        startButtonReproduction.setText("pause");
                    } else if (startButtonReproduction.getText() == "pause") {
                        pauseGeneration();
                        startButtonReproduction.setText("continue");
                    } else if (startButtonReproduction.getText() == "continue") {
                        contGeneration();
                        startButtonReproduction.setText("pause");
                    }
                }
            });

            propsButtonReproduction.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PropertiesDialog pd = new PropertiesDialog();
                    try {
                        pd.start(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            hbox.getChildren().addAll(startButtonReproduction, propsButtonReproduction);
            hbox.setPadding(new Insets(10, 10, 10, 50));
            generationTA.setPrefRowCount(4);
            generationTA.setPadding(new Insets(0, 3, 0, 3));
            generationTA.setPrefWidth(150);
            generationTA.setMaxWidth(150);
            generationTA.setPrefHeight(450);

            HBox h = new HBox();
            h.getChildren().addAll(lineChart, generationTA);
            h.setPrefHeight(450);
            h.setPrefWidth(800);

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
                stopGeneration();
            }
        });

        stage.setScene(scene);
        stage.show();

        //this has to be here cause it's a hac to get the background color of the textarea changed
        Region reg = (Region)generationTA.lookup(".content");
        reg.setStyle("-fx-background-color: black;");
    }

    @Override
    public void newGenerationScored() {
        if( gc.getLatestGenerationNumber() < GeneticRobotProperties.getNumberOfGenerations() && !stopped && !paused) {
            gc.genNextGeneration();
        }
        if( gc.getLatestGenerationNumber() >= GeneticRobotProperties.getNumberOfGenerations()) {
            propsButtonReproduction.setDisable(false);
            startButtonReproduction.setText("start");
        }
    }

    public void startReproducing() {
        //setup the robot code
        gc = new GeneticPopulation();
        gc.addNewGenerationScoredListener(this);
        gc.setGenerationCompleteEventHandler(new GenerationCompleteEventHandler());

        series.getData().clear();

        gc.genNextGeneration();
    }
    public void pauseGeneration() {
        paused = true;
    }
    public void contGeneration() {
        paused = false;
        gc.genNextGeneration();
    }
    public void stopGeneration() {
        stopped = true;
    }

    private class GenerationCompleteEventHandler implements EventHandler<WorkerStateEvent> {
        @Override
        public void handle(WorkerStateEvent event) {
            Strategy s = gc.getLatestGeneration();
            int genNum = gc.getLatestGenerationNumber();

            series.getData().add(new XYChart.Data(genNum, s.getScore()));
            generationTA.setText(genNum + "    " + s.getScore()+"\n"+generationTA.getText());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
