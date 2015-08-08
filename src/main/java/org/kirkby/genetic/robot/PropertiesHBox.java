package org.kirkby.genetic.robot;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Created by kirkby on 8/8/15.
 */
public class PropertiesHBox extends HBox implements PropertiesChangedListener {
    private static GeneticRobotProperties props = GeneticRobotProperties.getProps(); //initialize the props
    private final TextField tfCanDisperseFactor;
    private final TextField tfMapSize;
    private final TextField tfNumOfGeneratons;
    private final TextField tfBreederPopSize;
    private final TextField tfBreederPopFactor;
    private final TextField tfElitePopFactor;
    private final TextField tfNumMapForFitnessFunction;
    private final TextField tfNumItrsOnMap;

    public PropertiesHBox() {
        //can-disperse-factor
        tfCanDisperseFactor = new TextField();
        tfCanDisperseFactor.getStyleClass().add("prop-field");
        Tooltip tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("The density of cans dispersed over the map");
        tfCanDisperseFactor.setTooltip(tp);
        tfCanDisperseFactor.setAlignment(Pos.CENTER);
        tfCanDisperseFactor.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfCanDisperseFactor.focusedProperty().addListener(new PropChangeListener<>(tfCanDisperseFactor));
        //map-size
        tfMapSize = new TextField();
        tfMapSize.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the grid size of the map");
        tfMapSize.setTooltip(tp);
        tfMapSize.setAlignment(Pos.CENTER);
        tfMapSize.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfMapSize.focusedProperty().addListener(new PropChangeListener<>(tfMapSize));
        //number-of-generations
        tfNumOfGeneratons = new TextField();
        tfNumOfGeneratons.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the number of generations to evolve");
        tfNumOfGeneratons.setTooltip(tp);
        tfNumOfGeneratons.setAlignment(Pos.CENTER);
        tfNumOfGeneratons.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfNumOfGeneratons.focusedProperty().addListener(new PropChangeListener<>(tfNumOfGeneratons));
        //breeder-population-size
        tfBreederPopSize = new TextField();
        tfBreederPopSize.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the population size of each generation");
        tfBreederPopSize.setTooltip(tp);
        tfBreederPopSize.setAlignment(Pos.CENTER);
        tfBreederPopSize.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfBreederPopSize.focusedProperty().addListener(new PropChangeListener<>(tfBreederPopSize));
        //breeder-population-factor
        tfBreederPopFactor = new TextField();
        tfBreederPopFactor.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the percent population to breed for next gen");
        tfBreederPopFactor.setTooltip(tp);
        tfBreederPopFactor.setAlignment(Pos.CENTER);
        tfBreederPopFactor.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfBreederPopFactor.focusedProperty().addListener(new PropChangeListener<>(tfBreederPopFactor));
        //elite-population-factor
        tfElitePopFactor = new TextField();
        tfElitePopFactor.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the percent population to gene copy for next gen");
        tfElitePopFactor.setTooltip(tp);
        tfElitePopFactor.setAlignment(Pos.CENTER);
        tfElitePopFactor.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfElitePopFactor.focusedProperty().addListener(new PropChangeListener<>(tfElitePopFactor));
        //num-maps-for-fitness-function
        tfNumMapForFitnessFunction = new TextField();
        tfNumMapForFitnessFunction.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the percent population to gene copy for next gen");
        tfNumMapForFitnessFunction.setTooltip(tp);
        tfNumMapForFitnessFunction.setAlignment(Pos.CENTER);
        tfNumMapForFitnessFunction.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfNumMapForFitnessFunction.focusedProperty().addListener(new PropChangeListener<>(tfNumMapForFitnessFunction));
        //num-itrs-on-map
        tfNumItrsOnMap = new TextField();
        tfNumItrsOnMap.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the percent population to gene copy for next gen");
        tfNumItrsOnMap.setTooltip(tp);
        tfNumItrsOnMap.setAlignment(Pos.CENTER);
        tfNumItrsOnMap.setOnMouseClicked(new PropMouseClickEventHandler<>());
        tfNumItrsOnMap.focusedProperty().addListener(new PropChangeListener<>(tfNumItrsOnMap));

        this.getChildren().addAll(tfCanDisperseFactor, tfMapSize, tfNumOfGeneratons, tfBreederPopSize,
                tfBreederPopFactor, tfElitePopFactor, tfNumMapForFitnessFunction, tfNumItrsOnMap);
    }

    public void setProperties( GeneticRobotProperties props) {
        tfCanDisperseFactor.setText( Double.toString(props.getCanDisperseFactor()));
        tfMapSize.setText( Integer.toString(props.getMapSize()));
        tfNumOfGeneratons.setText( Integer.toString(props.getNumberOfGenerations()));
        tfBreederPopSize.setText( Integer.toString(props.getBreederPopulationSize()));
        tfBreederPopFactor.setText( Double.toString(props.getBreederPopulationFactor()));
        tfElitePopFactor.setText( Double.toString(props.getElitePopulationFactor()));
        tfNumMapForFitnessFunction.setText( Integer.toString(props.getNumMapsForFitnessFunction()));
        tfNumItrsOnMap.setText( Integer.toString(props.getNumberOfIterationsOnMap()));
    }


    @Override
    public void propertiesChanged(GeneticRobotProperties props) {
        setProperties(props);
    }

    private class PropChangeListener<T> implements ChangeListener<T> {
        private TextField tf;
        public PropChangeListener( TextField tf) {
            this.tf = tf;
        }
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            if(!(Boolean)newValue) {
                tf.getStyleClass().remove("prop-field-edit");
                tf.getTooltip().hide();
            }
        }
    }

    private class PropMouseClickEventHandler<T extends MouseEvent> implements EventHandler<T> {
        @Override
        public void handle( T event) {
            if( event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                TextField tf = (TextField)event.getSource();
                tf.getStyleClass().add("prop-field-edit");

                tf.getTooltip().show(tf, MainApplication.stage.getX()+tf.getLayoutX(), MainApplication.stage.getY()+tf.getLayoutY());
            }
        }
    }
}
