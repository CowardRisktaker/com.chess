package vue;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

public class VueConnexion {

    public Scene connexion() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/VueConnexion.fxml")));
        return new Scene(root, 600, 450);
    }
}