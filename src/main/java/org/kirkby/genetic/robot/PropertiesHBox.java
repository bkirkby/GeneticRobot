package org.kirkby.genetic.robot;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
        tfCanDisperseFactor.focusedProperty().addListener(new PropChangeListener<>(tfCanDisperseFactor));
        //map-size
        tfMapSize = new TextField();
        tfMapSize.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the grid size of the map");
        tfMapSize.setTooltip(tp);
        tfMapSize.setAlignment(Pos.CENTER);
        tfMapSize.focusedProperty().addListener(new PropChangeListener<>(tfMapSize));
        //number-of-generations
        tfNumOfGeneratons = new TextField();
        tfNumOfGeneratons.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the number of generations to evolve");
        tfNumOfGeneratons.setTooltip(tp);
        tfNumOfGeneratons.setAlignment(Pos.CENTER);
        tfNumOfGeneratons.focusedProperty().addListener(new PropChangeListener<>(tfNumOfGeneratons));
        //breeder-population-size
        tfBreederPopSize = new TextField();
        tfBreederPopSize.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the population size of each generation");
        tfBreederPopSize.setTooltip(tp);
        tfBreederPopSize.setAlignment(Pos.CENTER);
        tfBreederPopSize.focusedProperty().addListener(new PropChangeListener<>(tfBreederPopSize));
        //breeder-population-factor
        tfBreederPopFactor = new TextField();
        tfBreederPopFactor.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the percent population to breed for next gen");
        tfBreederPopFactor.setTooltip(tp);
        tfBreederPopFactor.setAlignment(Pos.CENTER);
        tfBreederPopFactor.focusedProperty().addListener(new PropChangeListener<>(tfBreederPopFactor));
        //elite-population-factor
        tfElitePopFactor = new TextField();
        tfElitePopFactor.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the percent population to gene copy for next gen");
        tfElitePopFactor.setTooltip(tp);
        tfElitePopFactor.setAlignment(Pos.CENTER);
        tfElitePopFactor.focusedProperty().addListener(new PropChangeListener<>(tfElitePopFactor));
        //num-maps-for-fitness-function
        tfNumMapForFitnessFunction = new TextField();
        tfNumMapForFitnessFunction.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the number of maps to run through for each candidate in each generation\nthe score is an average score of these map runs");
        tfNumMapForFitnessFunction.setTooltip(tp);
        tfNumMapForFitnessFunction.setAlignment(Pos.CENTER);
        tfNumMapForFitnessFunction.focusedProperty().addListener(new PropChangeListener<>(tfNumMapForFitnessFunction));
        //num-itrs-on-map
        tfNumItrsOnMap = new TextField();
        tfNumItrsOnMap.getStyleClass().add("prop-field");
        tp = new Tooltip();
        tp.getStyleClass().add("prop-tooltip");
        tp.setText("the number of steps to iterate through on each map");
        tfNumItrsOnMap.setTooltip(tp);
        tfNumItrsOnMap.setAlignment(Pos.CENTER);
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

    public void ConvertTextFieldToProps( GeneticRobotProperties props) {
        try {
            props.setCanDisperseFactor(Double.parseDouble(tfCanDisperseFactor.getText()));
            props.setMapSize(Integer.parseInt(tfMapSize.getText()));
            props.setNumberOfGenerations(Integer.parseInt(tfNumOfGeneratons.getText()));
            props.setBreederPopulationSize(Integer.parseInt(tfBreederPopSize.getText()));
            props.setBreederPopulationFactor(Double.parseDouble(tfBreederPopFactor.getText()));
            props.setElitePopulationFactor(Double.parseDouble(tfElitePopFactor.getText()));
            props.setNumMapsForFitnessFunction(Integer.parseInt(tfNumMapForFitnessFunction.getText()));
            props.setNumberOfIterationsOnMap(Integer.parseInt(tfNumItrsOnMap.getText()));
        }catch( NumberFormatException nfe) {
            Alert alt = new Alert(Alert.AlertType.ERROR, nfe.getClass().getName()+": "+nfe.getMessage());
            alt.show();
        }
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
                ConvertTextFieldToProps(GeneticRobotProperties.getProps());
            } else {
                tf.getStyleClass().add("prop-field-edit");
                tf.getTooltip().show(tf, MainApplication.stage.getX() + tf.localToScene(0.0, 0.0).getX(),
                        tf.getScene().getWindow().getY() + tf.localToScene(0.0, 0.0).getY() + tf.getScene().getY() + tf.getHeight() + 1);
            }
        }
    }
}
