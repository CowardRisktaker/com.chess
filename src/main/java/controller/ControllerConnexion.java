package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControllerConnexion {

    @FXML private TextField     tfLogin;
    @FXML private PasswordField pfMotDePasse;
    @FXML private Label         lblErreur;

    @FXML
    private void handleConnexion() {
        // TODO : logique d'authentification
        System.out.println("Login : " + tfLogin.getText());
    }
}