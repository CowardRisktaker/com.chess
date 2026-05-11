package controller;

import DAO.DAOAcces;
import app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Utilisateur;
import vue.VueAccueil;

public class ControllerConnexion {

    @FXML private TextField     tfLogin;
    @FXML private PasswordField pfMotDePasse;
    @FXML private Label         lblErreur;

    @FXML
    private VueAccueil handleConnexion() throws Exception {
        String login = tfLogin.getText() ;
        String mdp = pfMotDePasse.getText();

         DAOAcces dao = new DAOAcces();
         Utilisateur user = dao.checkLogin(login);


        if (user != null && dao.checkPass(login, mdp)) {
            System.out.println("OK");
            Main.stageApp.setScene(VueAccueil.creerScene());
        }
        else {
            lblErreur.setText("Login ou mot de passe incorrect");
            lblErreur.setVisible(true);
        }
        dao.closeConn();
        return null;
    }
}