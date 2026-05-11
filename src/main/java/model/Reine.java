package model;

public class Reine extends Piece{
    public Reine(Position p, String couleur) {
        super(p, couleur);
    }

    @Override
    public boolean coupLegal(int x, int y, int n) {

    }

    @Override
    public boolean coupLegal(int x, int y, int scaleX, int scaleY) {

    }

    public String getType() { return "Reine"; }
}
