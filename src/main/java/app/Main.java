package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

    public class Main extends Application {
        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage stage) {
            GridPane root = new GridPane();
            for (int i = 0; i < 5; i++) {
                root.getColumnConstraints().add(new ColumnConstraints(80));
                root.getRowConstraints().add(new RowConstraints(80));
                // 30 = espacement des cases
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    // fait un plateau 8/8
                    if ((i+j)%2 == 0)
                        root.add(new Rectangle(80, 80, Color.BLUE), i, j);
                }
            }
            Scene scene = new Scene(root, 1000, 700);
            stage.setScene(scene);
            stage.show();
        }
    }
