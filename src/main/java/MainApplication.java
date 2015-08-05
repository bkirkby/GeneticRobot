import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.util.ArrayList;


/**
 * Created by kirkby on 8/2/15.
 */
public class MainApplication extends Application implements NewGenerationScoredListener {
    private GeneticPopulation gc;
    private RobotMapWorker rmw;
    private RobotMap rm;
    private XYChart.Series seriesGenScore = new XYChart.Series();
    private XYChart.Series seriesRobot = new XYChart.Series();
    private XYChart.Series seriesCans = new XYChart.Series();
    //private static int NUM_GENERATIONS = GeneticRobotProperties.getNumberOfGenerations();
    TextArea generationTA = new TextArea();
    private boolean paused = false;
    private boolean stopped = false;
    final private Button startButtonReproduction = new Button("start");
    final private Button propsButtonReproduction = new Button("properties");
    final private TableView<Strategy> strategyTableView = new TableView<>();
    private ObservableList<Strategy> topStrategyList;

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
        //defining a
        seriesGenScore.setName("genetic brood");

        VBox vbox = new VBox();

        Scene scene = new Scene( new Group(), 700, 600);
        scene.getStylesheets().add("MainApplication.css");
        lineChart.getData().add(seriesGenScore);

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
            //buttons hbox
            HBox hbox = new HBox();
            hbox.setSpacing(10);
            final Button startBtn = new Button("step");
            final Button resetBtn = new Button("reset");
            final Label update = new Label("x/0.0");
            startBtn.setOnAction(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     if (startBtn.getText() == "step") {
                         if (rm == null) {
                             rm = new RobotMap();
                             rm.setRobotStrategy(strategyTableView.getSelectionModel().getSelectedItem().getStrategy());
                             seriesRobot.getData().add( new XYChart.Data(rm.getRobotLoc().getKey()+.5 , rm.getRobotLoc().getValue()+.5, .35));
                             for(Pair<Integer,Integer> canLoc : rm.getCansLoc()) {
                                 seriesCans.getData().add( new XYChart.Data( canLoc.getKey()+.5, canLoc.getValue()+.5, .2));
                             }
                         }
                         Pair<Integer,Integer> actionAndScore = rm.stepStrategy();
                         update.setText( RobotMap.actionIntToString(actionAndScore.getKey())+"/"+Double.toString(actionAndScore.getValue())+"/"
                                 +Integer.toString(rm.getScore()));
                         seriesRobot.getData().remove(0);
                         seriesRobot.getData().add(new XYChart.Data(rm.getRobotLoc().getKey() + .5, rm.getRobotLoc().getValue() + .5, .35));


                        /*rmw = new RobotMapWorker( rm);

                        //setup map
                        seriesRobot.getData().add( new XYChart.Data(rmw.getRobotLoc().getKey()+.5 , rmw.getRobotLoc().getValue()+.5, .35));
                        for(Pair<Integer,Integer> canLoc : rmw.getCansLoc()) {
                            seriesCans.getData().add( new XYChart.Data( canLoc.getKey()+.5, canLoc.getValue()+.5, .2));
                        }
                        stepRobot();*/
                     }
                 }
             });
            resetBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    rm = null;
                    seriesCans.getData().clear();
                    seriesRobot.getData().clear();
                }
            });

            hbox.getChildren().addAll(startBtn, resetBtn, update);
            hbox.setPadding(new Insets(10, 10, 10, 200));
            robotMapVBox.getChildren().add(hbox);

            //list/map hbox
            hbox = new HBox();
            hbox.setSpacing(10);
            //list
            topStrategyList = FXCollections.observableList(new ArrayList<Strategy>());
            TableColumn<Strategy,String> generationCol = new TableColumn<Strategy,String>("generation");
            generationCol.setCellValueFactory(new PropertyValueFactory<Strategy, String>("generation"));
            TableColumn<Strategy,String> scoreCol = new TableColumn<Strategy,String>("score");
            scoreCol.setCellValueFactory(new PropertyValueFactory("score"));
            strategyTableView.getColumns().setAll(generationCol, scoreCol);
            strategyTableView.setItems(topStrategyList);
            strategyTableView.setPrefWidth(155);
            hbox.getChildren().add(strategyTableView);
            //map
            {
                final NumberAxis x_axis = new NumberAxis(1, GeneticRobotProperties.getMapSize(), 1);
                final NumberAxis y_axis = new NumberAxis(1, GeneticRobotProperties.getMapSize(), 1);
                final BubbleChart<Number, Number> blc = new
                        BubbleChart<Number, Number>(x_axis, y_axis);
                seriesRobot.setName("robot");
                seriesCans.setName("cans");
                blc.getData().addAll(seriesRobot, seriesCans);
                hbox.getChildren().add(blc);
            }
            robotMapVBox.getChildren().add(hbox);

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

//    @Override
//    public void robotAdvanced() {
        //TODO robot advanced
//    }

    public void stepRobot() {
        final Service<Integer> svc = new RobotMapWorker( new RobotMap());
        svc.setOnSucceeded(new RobotMapProgressEventHandler());

        svc.start();
        /*Thread t = new Thread(svc);
        t.start();
        while(t.isAlive()) {
            try {
                Thread.sleep(10);
            } catch( InterruptedException e) {
                break;
            }
        }
        svc.addEventHandler(WorkerStateEvent.WORKER_STATE_RUNNING,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.print('b');
                    }
                }
        );
        svc.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        System.out.println('c');
                        //int score = task.getValue();
                        //rmw.getRobotLoc();
                    }
                }
        );*/
    }

    public void startReproducing() {
        //setup the robot code
        gc = new GeneticPopulation();
        gc.addNewGenerationScoredListener(this);
        gc.setGenerationCompleteEventHandler(new GenerationCompleteEventHandler());

        seriesGenScore.getData().clear();

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

            seriesGenScore.getData().add(new XYChart.Data(s.getGenerationNumber(), s.getScoreNumber()));
            generationTA.setText(s.getGeneration() + "    " + s.getScore() + "\n" + generationTA.getText());

            topStrategyList.add( s);
        }
    }

    private class RobotMapProgressEventHandler implements EventHandler<WorkerStateEvent> {
        @Override
        public void handle(WorkerStateEvent event) {
            seriesRobot.getData().remove(0);
            seriesRobot.getData().add(new XYChart.Data(rmw.getRobotLoc().getKey() + .5, rmw.getRobotLoc().getValue() + .5, .35));

            stepRobot();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
