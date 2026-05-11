package model;

public class Pion extends Piece {
    public Pion (Position p, String couleur) {
        super(p, couleur);
    }

    @Override
    public boolean coupLegal(int x, int y, int n) {
        return false;
    }

    @Override
    public boolean coupLegal(int x, int y, int scaleX, int scaleY) {
        return false;
    }


    public String getType() { return "Pion"; }
}

