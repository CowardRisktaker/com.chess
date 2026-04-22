package vue;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Piece;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import model.Board;
import model.Position;
import java.util.ArrayList;
import java.util.List;


public class VueBoard extends Pane {
    private final GridPane root = new GridPane();
    private final Board board;
    static boolean flag = false;
    Piece pi;
    Position p;
    private final List<Position> history = new ArrayList<>();


    public VueBoard(Board board) throws FileNotFoundException {
        this.board = board;
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        root.setStyle("""
            -fx-border-color: black;
            -fx-border-style: solid;
            """);
        this.setStyle("-fx-background-color: white;");
        Rectangle rectangle = new Rectangle(750, 750, Color.web("#A47449"));
        rectangle.relocate(70, 70);
        rectangle.setId("rectangle");
        root.relocate(120, 120);
        this.getChildren().addAll(rectangle, root);
        renderPieces();
    }
    private void renderPieces() throws FileNotFoundException {
        Piece[][] plateau = board.getPlateau();

//        Rectangle rectangle = new Rectangle(750, 750, Color.web("#A47449"));
//        rectangle.relocate(70, 70);
//        rectangle.setId("rectangle");
//        rectangle.setStyle("");
        for (int i = 0; i < 8; i++) {
            root.getColumnConstraints().add(new ColumnConstraints(80));
            root.getRowConstraints().add(new RowConstraints(80));
            // 80 = espacement des cases
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Rectangle r = new Rectangle(80, 80);
                r.setId(x + "-" + y);
                r.getStyleClass().add(((x + y) % 2 == 0) ? "impair" : "pair");
                root.add(r, x, y);
                String s = String.valueOf(r);
                System.out.println(s);
            }
        }

//        root.relocate(120, 120);
//        this.getChildren().addAll(rectangle, root);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                pi = plateau[x][y];
                if (pi != null) {
                    ImageView img = imageView(pi);
                    root.add(img, x, y);
                }
            }
        }

        root.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            int xs = (int) (e.getX()) / 80;
            int ys = (int) (e.getY()) / 80;
            if(!flag) {
                pi = board.getPiece(xs, ys);
                if (pi == null) return;
                flag = true;
                System.out.println("coordonnées prend: " + xs + "-" + ys + " Piece est :" + pi.getType());
                history.add(new Position(xs, ys));
            }
            else{
                Position from;
                System.out.println("coordonnées relache: " + xs + "-" + ys);
                if (history.isEmpty()) {
                    flag = false;
                }
                else {
                    from = history.removeLast();
                    int oldX = from.getX();
                    int oldY = from.getY();
                    flag = false;
                    board.getPlateau()[oldX][oldY] = null;
                    removePieceViewAt(oldX, oldY);
                    board.getPlateau()[xs][ys] = null;
                    removePieceViewAt(xs, ys);
                    board.getPlateau()[xs][ys] = pi;
                    pi.setP(new Position(xs, ys));
                    FileInputStream img;
                    try {
                        img = new FileInputStream("src/main/resources/images/" + pi.getType() + pi.getCouleur() + ".png");
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    Image pic1 = new Image(img);
                    ImageView fimg = new ImageView(pic1);
                    fimg.setFitWidth(70);
                    fimg.setFitHeight(70);
                    root.add(fimg, xs, ys);
                }
            }
        });
    }

    private void removePieceViewAt(int x, int y) {
        root.getChildren().removeIf(node ->
                node instanceof ImageView
                        && GridPane.getColumnIndex(node) != null
                        && GridPane.getRowIndex(node) != null
                        && GridPane.getColumnIndex(node) == x
                        && GridPane.getRowIndex(node) == y
        );
    }

    public static ImageView imageView(Piece pi) throws FileNotFoundException {
        FileInputStream imageStream = new FileInputStream("src/main/resources/images/" + pi.getType() + pi.getCouleur() + ".png");
        Image pic = new Image(imageStream);
        ImageView img = new ImageView(pic);
        img.setFitWidth(70);
        img.setFitHeight(70);
        return img;
    }
}
