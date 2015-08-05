import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;

/**
 * Created by kirkby on 8/4/15.
 */
public class PropertiesDialog extends Application {
    @Override
    public void start(final Stage primaryStage) throws Exception {

        primaryStage.setOpacity(.7);

        final Stage dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);

        dialog.setTitle("properties");

        VBox mainVBox = new VBox();

        Scene scene = new Scene( mainVBox, 300, 300);
        scene.getStylesheets().add("MainApplication.css");

        //num-itrs-on-map
        final TextField tfNumItrsOnMap = new TextField(Integer.toString(GeneticRobotProperties.getNumberOfIterationsOnMap()));
        tfNumItrsOnMap.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("num-itrs-on-map: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfNumItrsOnMap);//here
            mainVBox.getChildren().add(hb);
        }

        //num-maps-for-fitness-function
        final TextField tfNumMapsForFitnessFunction = new TextField(Integer.toString(GeneticRobotProperties.getNumMapsForFitnessFunction()));
        tfNumMapsForFitnessFunction.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("num-maps-for-fitness-function: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfNumMapsForFitnessFunction);//here
            mainVBox.getChildren().add(hb);
        }

        //elite-population-factor=.05
        final TextField tfElitePopulationFactor = new TextField(Double.toString(GeneticRobotProperties.getElitePopulationFactor()));
        tfElitePopulationFactor.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("elite-population-factor: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfElitePopulationFactor);//here
            mainVBox.getChildren().add(hb);
        }

        //breeder-population-factor=.2
        final TextField tfBreederPopFactor = new TextField(Double.toString(GeneticRobotProperties.getBreederPopulationFactor()));
        tfBreederPopFactor.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("breeder-population-factor: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfBreederPopFactor);//here
            mainVBox.getChildren().add(hb);
        }

        //breeder-population-size=150
        final TextField tfBreederPopSize = new TextField(Integer.toString(GeneticRobotProperties.getBreederPopulationSize()));
        tfBreederPopSize.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("breeder-population-size: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfBreederPopSize);//here
            mainVBox.getChildren().add(hb);
        }

        //number-of-generations=1000
        final TextField tfNumberOfGenerations = new TextField(Integer.toString(GeneticRobotProperties.getNumberOfGenerations()));
        tfNumberOfGenerations.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("number-of-generations: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfNumberOfGenerations);//here
            mainVBox.getChildren().add(hb);
        }

        //map-size=10
        final TextField tfMapSize = new TextField(Integer.toString(GeneticRobotProperties.getMapSize()));
        tfMapSize.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("map-size: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfMapSize);//here
            mainVBox.getChildren().add(hb);
        }

        //can-disperse-factor=.5
        final TextField tfCanDisperseFactor = new TextField(Double.toString(GeneticRobotProperties.getCanDisperseFactor()));
        tfCanDisperseFactor.getStyleClass().add("properties-element");
        {
            HBox hb = new HBox();
            hb.setSpacing(10);
            hb.setPadding(new Insets(3, 3, 3, 3));
            Label lbl = new Label("can-disperse-factor: ");//here
            lbl.getStyleClass().add("properties-element");
            hb.getChildren().addAll(lbl, tfCanDisperseFactor);//here
            mainVBox.getChildren().add(hb);
        }

        //close button
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(10, 10, 10, 50));
        Button btnOk = new Button("ok");
        btnOk.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                try {
                    GeneticRobotProperties.setNumberOfIterationsOnMap(Integer.parseInt(tfNumItrsOnMap.getText()));
                    GeneticRobotProperties.setNumMapsForFitnessFunction(Integer.parseInt(tfNumMapsForFitnessFunction.getText()));
                    GeneticRobotProperties.setElitePopulationFactor(Double.parseDouble(tfElitePopulationFactor.getText()));
                    GeneticRobotProperties.setBreederPopulationFactor(Double.parseDouble(tfBreederPopFactor.getText()));
                    GeneticRobotProperties.setBreederPopulationSize(Integer.parseInt(tfBreederPopSize.getText()));
                    GeneticRobotProperties.setNumberOfGenerations(Integer.parseInt(tfNumberOfGenerations.getText()));
                    GeneticRobotProperties.setMapSize(Integer.parseInt(tfMapSize.getText()));
                    GeneticRobotProperties.setCanDisperseFactor(Double.parseDouble(tfCanDisperseFactor.getText()));
                    primaryStage.setOpacity(1);
                }catch(NumberFormatException nfe) {
                    Alert a = new Alert(Alert.AlertType.ERROR, "NumberFormatException "+nfe.getMessage());
                    a.show();
                    return;
                }
                dialog.close();
            }
        });
        buttons.getChildren().addAll(btnOk);

        mainVBox.getChildren().add(buttons);

        btnOk.requestFocus();

        dialog.setScene(scene);

        dialog.showAndWait();

    }
}
