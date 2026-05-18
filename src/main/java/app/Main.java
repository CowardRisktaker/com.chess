package app;

import DAO.DAOAcces;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Board;
import vue.VueAccueil;
import vue.VueBoard;
import vue.VueConnexion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Objects;


public class Main extends Application {
    public static Stage     stageApp ;
    public static VBox      vbox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
   public void start(Stage stage) throws Exception {
//        VueBoard board = new VueBoard(new Board());
        VueConnexion root = new VueConnexion();
        stageApp = stage;
//        stage.setScene(board.creerScene());
        stage.setScene(root.connexion());
        stage.setTitle("com.chess");
        stage.show();

   }

    public static VBox getVBox(){
        return vbox;
    }
}
