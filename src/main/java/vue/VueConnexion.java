package vue;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class VueConnexion {

    public static Scene connexion() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                VueConnexion.class.getResource("/VueConnexion.fxml")
        );
        Parent root = loader.load();
        return new Scene(root, 600, 450);
    }
}