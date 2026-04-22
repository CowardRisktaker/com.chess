package vue;



import controller.ControllerAccueil;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Builder;
import vue.VueBoard;

import java.io.FileNotFoundException;

public class VueAccueil extends Scene {
private final Button boutonJouer;
    public VueAccueil(VBox vbox)  {
        super(vbox, 1200, 900);

        //vbox.setStyle("-fx-padding: 20;");



        boutonJouer = new Button("Jouer");
        boutonJouer.setOnMouseClicked(e -> {
            try {
                new ControllerAccueil();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        vbox.getChildren().add(boutonJouer);
        }
    }

