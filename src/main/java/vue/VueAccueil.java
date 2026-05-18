package vue;



import controller.ControllerAccueil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import model.Utilisateur;

import java.util.Objects;

public class VueAccueil extends Scene {
    public VueAccueil(Parent root) {
        super(root);
    }

    public static Scene creerScene(Utilisateur user) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(VueAccueil.class.getResource("/VueAccueil.fxml")));
        Parent root = loader.load();
        ControllerAccueil controller = loader.getController();
        controller.setUtilisateur(user);
        return new Scene(root, 600, 450);
    }
}


