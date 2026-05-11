package model;

public class Tour extends Piece {

    public Tour(Position p, String couleur) {
        super(p, couleur);
    }

    @Override
    public boolean coupLegal(int x, int y, int n) {
        return false;
    }

    @Override
    public boolean coupLegal(int x, int y, int scaleX, int scaleY) {
        if (scaleX == 0 || scaleY == 0) {
            this.setP(new Position(x, y));
            return true;
        }
        return false;
    }

    @Override
    public String getType() { return "Tour"; }
}
