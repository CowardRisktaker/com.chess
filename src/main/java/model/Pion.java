package model;

public class Pion extends Piece {
    public Pion (Position p, String couleur) {
        super(p, couleur);
    }
    public String getType() { return "Pion"; }
}

