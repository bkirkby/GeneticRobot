package org.kirkby.genetic.robot;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
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
import org.kirkby.genetic.robot.org.kirkby.genetic.robot.ga.GeneticPopulation;
import org.kirkby.genetic.robot.org.kirkby.genetic.robot.map.RobotMap;
import org.kirkby.genetic.robot.org.kirkby.genetic.robot.map.RobotMapWorker;

import java.util.*;


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
        xAxis.setLabel("gen");
        yAxis.setLabel("score");
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
            final Button stepBtn = new Button("step");
            stepBtn.setDisable( true);
            final Button runBtn = new Button("run");
            final Button resetBtn = new Button("reset");
            final Label update = new Label("x/0/0");
            stepBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (stepBtn.getText() == "step") {
                        if (rm == null) {
                            mapCreateNew();
                        }
                        Pair<Integer, Integer> actionAndScore = rm.stepStrategy();
                        update.setText(RobotMap.actionIntToString(actionAndScore.getKey()) + "/" + Double.toString(actionAndScore.getValue()) + "/"
                                + Integer.toString(rm.getScore()));
                        Pair<Integer,Integer> robotLoc = rm.getRobotLoc();
                        //if they got the reward then they must have picked up a can, so let's remove it
                        if(actionAndScore.getValue()== RobotMap.PICK_SUCCESS_REWARD) {
                            //remove( Object) below is broken, so i need to rebuild the list and set it to seriesCans
                            //fwiw, remove(int) and remove(int,int) are also broken
                            //seriesCans.getData().remove(new XYChart.Data(robotLoc.getKey() + 1.5, robotLoc.getValue() + 1.5, .2));
                            ArrayList<XYChart.Data> al = new ArrayList();
                            for( Pair<Integer, Integer> canLoc : rm.getCansLoc()) {
                                al.add(new XYChart.Data(canLoc.getKey()+1.5,canLoc.getValue()+1.5,.2));
                            }
                            seriesCans.setData(FXCollections.observableList(al));
                        }
                        mapMoveRobot(robotLoc);
                    }
                }
            });
            runBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //concurrent service to run
                }
            });
            resetBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mapCreateNew();
                }
            });

            hbox.getChildren().addAll(stepBtn, resetBtn, update);
            hbox.setPadding(new Insets(10, 10, 10, 200));
            robotMapVBox.getChildren().add(hbox);

            //list/map hbox
            hbox = new HBox();
            hbox.setSpacing(10);
            //list
            topStrategyList = FXCollections.observableList(new ArrayList<Strategy>());
            TableColumn<Strategy,String> generationCol = new TableColumn<Strategy,String>("gen");
            generationCol.setCellValueFactory(new PropertyValueFactory<Strategy, String>("generation"));
            generationCol.setPrefWidth(75);
            TableColumn<Strategy,String> scoreCol = new TableColumn<Strategy,String>("score");
            scoreCol.setCellValueFactory(new PropertyValueFactory("score"));
            scoreCol.setPrefWidth(75);
            strategyTableView.getColumns().setAll(generationCol, scoreCol);
            strategyTableView.setItems(topStrategyList);
            strategyTableView.setPrefWidth(155);
            strategyTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Strategy>(){
                @Override
                public void changed(ObservableValue<? extends Strategy> ov, Strategy oldVal, Strategy newVal) {
                    if( newVal!= null) {
                        rm.setRobotStrategy( newVal.getStrategy());
                        stepBtn.setDisable( false);
                    } else {
                        stepBtn.setDisable( true);
                    }
                }
            });
            hbox.getChildren().add(strategyTableView);
            //map
            {
                final NumberAxis x_axis = new NumberAxis(1, GeneticRobotProperties.getMapSize()+1, 1);
                x_axis.getStyleClass().addAll("map-axis");
                final NumberAxis y_axis = new NumberAxis(1, GeneticRobotProperties.getMapSize()+1, 1);
                y_axis.getStyleClass().addAll("map-axis");
                final BubbleChart<Number, Number> blc = new
                        BubbleChart<Number, Number>(x_axis, y_axis);
                seriesRobot.setName("robot");
                seriesCans.setName("cans");
                blc.getData().addAll(seriesRobot, seriesCans);
                hbox.getChildren().add(blc);
                mapCreateNew();
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
    }

    private void mapCreateNew() {
        rm = new RobotMap();
        if( strategyTableView.getSelectionModel().getSelectedItem() != null) {
            rm.setRobotStrategy( strategyTableView.getSelectionModel().getSelectedItem().getStrategy());
        }
        mapMoveRobot(rm.getRobotLoc());
        ArrayList<XYChart.Data> al = new ArrayList();
        for( Pair<Integer, Integer> canLoc : rm.getCansLoc()) {
            al.add(new XYChart.Data(canLoc.getKey() + 1.5, canLoc.getValue() + 1.5, .2));
        }

        seriesCans.setData(FXCollections.observableList(al));
    }

    /*private ObservableList<XYChart.Data> transformMapCanList( List<Pair<Integer,Integer>> cansLoc) {
        seriesCan.
        return ol;
    }*/

    private void mapMoveRobot( Pair<Integer,Integer> xy) {
        if( seriesRobot.getData().size() > 0) {
            seriesRobot.getData().remove(0);
        }
        seriesRobot.getData().add(new XYChart.Data(xy.getKey() + 1.5, xy.getValue() + 1.5, .35));
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

            topStrategyList.add(0,s);
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
