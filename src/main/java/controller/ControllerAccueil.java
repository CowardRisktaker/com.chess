package controller;

import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Board;
import model.Piece;
import vue.VueAccueil;
import vue.VueBoard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ControllerAccueil {

    public ControllerAccueil() throws FileNotFoundException {
        Main.getVBox().getChildren().add(new VueBoard(new Board()));
        Main.stageApp.setTitle("com.chess!");
        Main.stageApp.show();

    }

    public void openScene2(ActionEvent event) throws IOException {
        // Load the FXML file for the second scene
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("Scene2.fxml"));
        // Create a new stage for the second scene


    }

}