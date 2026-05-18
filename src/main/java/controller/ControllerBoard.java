package controller;

import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Board;
import model.Piece;
import model.Position;
import model.Utilisateur;
import vue.VueBoard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class ControllerBoard {
    Board                           board;
    Piece                           pi;
    private final List<Position>    history = new ArrayList<>();
    VueBoard                        vue;
    private boolean                 flag = false;

    public ControllerBoard(Board board, VueBoard vue) {
        this.board = board;
        this.vue = vue;
    }

    public void setUtilisateur(Utilisateur user) {

    }

    public void handleClick(int x, int y) throws FileNotFoundException {
        if (!flag) {
            pi = board.getPiece(x, y);
            if (pi == null) return;
            flag = true;
            history.add(new Position(x, y));
        } else {
            if (history.isEmpty()) { flag = false; return; }
            Position from = history.removeLast();
            int oldX = from.getX();
            int oldY = from.getY();
            int scaleX = Math.abs(oldX - x);
            int scaleY = Math.abs(oldY - y);

            boolean legal = pi.coupLegal(x, y, scaleX, scaleY, from, board.getPlateau());
            flag = false;
            if (!legal) { return; }
            vue.removePieceViewAt(x,y);
            board.deplacer(oldX, oldY, x, y);
            vue.rafraichir(oldX, oldY, x, y, pi);
        }
    }
}
