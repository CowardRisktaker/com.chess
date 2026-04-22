package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import vue.VueAccueil;

import java.io.FileNotFoundException;


public class Main extends Application {
    public static Stage stageApp ;
    public static VBox vbox;
    @Override
    public void start(Stage stage) throws FileNotFoundException {
        stageApp = stage;
        Pane pane = new Pane();
        vbox = new VBox();
        //vbox.setSpacing(12);
        vbox.getChildren().add(pane);
        VueAccueil scene1 = new VueAccueil(vbox);


        stage.getIcons().add(new Image("file:src/main/resources/images/icon.svg.png"));
        stageApp.setScene(scene1);
        stageApp.show();
    }

    public static VBox getVBox(){
        return vbox;
    }
}
