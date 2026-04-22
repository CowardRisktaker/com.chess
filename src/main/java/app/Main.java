package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import vue.VueAccueil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class Main extends Application {
    public static Stage stageApp ;
    public static VBox vbox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stageApp = stage;

        Parent root = FXMLLoader.load(getClass().getResource("/VueConnexion.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("ChessEngine");
        stage.show();
    }
    /* @Override
    public void start(Stage stage) {
        stageApp = stage;
        Pane pane = new Pane();
        vbox = new VBox();
        //vbox.setSpacing(12);
        vbox.getChildren().add(pane);
        VueAccueil scene1 = new VueAccueil(vbox);
        stageApp.setScene(scene1);
        stageApp.show();
    }
*/
    public static VBox getVBox(){
        return vbox;
    }
}
