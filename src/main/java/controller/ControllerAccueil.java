package controller;

import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Board;
import vue.VueBoard;

import java.io.FileNotFoundException;

public class ControllerAccueil {

    @FXML
    public VueBoard creerBoard(ActionEvent event) throws FileNotFoundException {
        VueBoard board = new VueBoard(new Board()) ;
        Main.stageApp.setScene(board.creerScene());
        Main.stageApp.setTitle("com.chess!");
        Main.stageApp.show();


        return null;
    }

}