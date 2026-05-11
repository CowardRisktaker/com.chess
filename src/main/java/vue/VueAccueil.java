package vue;



import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

public class VueAccueil extends Scene {
    public VueAccueil(Parent root) {
        super(root);
    }

    public static Scene creerScene() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(VueAccueil.class.getResource("/VueAccueil.fxml")));
        return new Scene(root, 600, 450);
    }

}


