package model;

public class Tour extends Piece{
    public Tour(Position p, String couleur) {
        super(p, couleur);
    }
    public String getType() { return "Tour"; }
}
