package vue;

import DAO.DAOPartie;
import app.Main;
import controller.ControllerBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.*;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Objects;

public class VueBoard extends Pane {
    private final GridPane          root = new GridPane();
    private final Board             board;
    private final ControllerBoard   controller;
    private final Piece[][]         plateau;
    private final int               idPartie;
    private final DAOPartie         dao;


    public VueBoard(Board board, int idPartie) throws SQLException {
        this.board = board;
        this.controller = new ControllerBoard(this.board, this);
        this.plateau = board.getPlateau();
        this.idPartie = idPartie;
        this.dao = new DAOPartie();
    }
    private void renderPieces() throws FileNotFoundException {
        for (int i = 0; i < 8; i++) {
            root.getColumnConstraints().add(new ColumnConstraints(80));
            root.getRowConstraints().add(new RowConstraints(80));
        }
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Rectangle r = new Rectangle(80, 80);
                Piece pi = plateau[x][y];
                r.setId(x + "-" + y);
                r.getStyleClass().add(((x + y) % 2 == 0) ? "impair" : "pair");
                root.add(r, x, y);
                if (pi != null) {
                    ImageView img = imageView(pi);
                    root.add(img, x, y);
                }
                String s = String.valueOf(r);
            }
        }
        root.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            int xs = (int) (e.getX()) / 80;
            int ys = (int) (e.getY()) / 80;
            try {
                controller.handleClick(xs, ys);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void rafraichir(int oldX, int oldY, int x, int y, Piece pi) throws FileNotFoundException {
        removePieceViewAt(oldX, oldY);
        board.getPlateau()[x][y] = pi;
        ImageView img = imageView(pi);
        root.add(img, x, y);
    }
    public static ImageView imageView(Piece pi) throws FileNotFoundException {
        FileInputStream imageStream = new FileInputStream("src/main/resources/images/" + pi.getType() + pi.getCouleur() + ".png");
        Image pic = new Image(imageStream);
        ImageView img = new ImageView(pic);
        img.setFitWidth(70);
        img.setFitHeight(70);
        return img;
    }
    public void removePieceViewAt(int x, int y) {
        root.getChildren().removeIf(node ->
                node instanceof ImageView
                        && GridPane.getColumnIndex(node) != null
                        && GridPane.getRowIndex(node) != null
                        && GridPane.getColumnIndex(node) == x
                        && GridPane.getRowIndex(node) == y
        );
    }
    @FXML
    public void setBoard(ActionEvent event) {
    }
    public Scene creerScene() throws FileNotFoundException {
        HBox root1 = new HBox();
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        root.setStyle("""
            -fx-border-color: black;
            -fx-border-style: solid;
            """);
        this.setStyle("-fx-background-color: white;");
        Rectangle rectangle = new Rectangle(750, 750, Color.web("#A47449"));
        rectangle.relocate(70, 70);
        rectangle.setId("rectangle");
        Button bouton = getButton();
        root.relocate(120, 120);
        this.getChildren().addAll(rectangle, root);
        root1.getChildren().add(this);
        root1.getChildren().add(bouton);
        renderPieces();
        Main.stageApp.setTitle("com.chess!");
        Main.stageApp.show();
        return new Scene(root1, 950, 850);
    }

    private Button getButton() {
        Button bouton = new Button("Sauvegarder");
        bouton.setOnAction(_ -> {
            String etat = Sauvegarde.sauver(this.board);
            dao.sauvegarder(etat, idPartie);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Sauvegardé!");
            alert.showAndWait();
        });
        return bouton;
    }
}
