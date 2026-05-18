package controller;

import DAO.DAOPartie;
import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import model.Board;
import model.Utilisateur;
import vue.VueBoard;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class ControllerAccueil {
    @FXML private Label       welcomeLabel;
    @FXML private MenuButton  menuButton;
    private Utilisateur       user;
    private int               idPartie;

    public void creerAccueil() {}

    // utilisé dans fxml
    @FXML
    public void creerBoard(ActionEvent event) throws FileNotFoundException, SQLException {
        DAOPartie daoPartie = new DAOPartie();
        idPartie = daoPartie.creerPartie(user.getId(), "joueur_vs_joueur");
        VueBoard board = new VueBoard(new Board(), idPartie);
        Main.stageApp.setScene(board.creerScene());
        Main.stageApp.setTitle(user.getLogin());
        Main.stageApp.show();
    }

    public void chargerBoard() {

    }

    public void setUtilisateur(Utilisateur user) {
        welcomeLabel.setText("Bienvenue " + user.getLogin() + " !");
        menuButton.setText(user.getLogin());
        this.user = user;
    }
}