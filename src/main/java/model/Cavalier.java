package model;

public class Cavalier extends Piece {
    public Cavalier(Position p, String couleur) {
        super(p, couleur);
    }

    @Override
    public boolean coupLegal(int x, int y, int n) {
        Position pos = new Position(x, y);
        if (pos.equals(new Position(x-n, y-n )) || pos.equals(new Position(x+n, y-n )) || pos.equals(new Position(x-n, y+n)) || pos.equals(new Position(x+n, y+n))) {
            this.setP(pos);

        }
    }
    public boolean coupLegal(int x, int y, int scaleX, int scaleY) {
        Position pos = new Position(x, y);
        if (pos.equals(new Position(x-scaleX, y-scaleY )) || pos.equals(new Position(x+scaleX, y-scaleY )) || pos.equals(new Position(x-scaleX, y+scaleY)) || pos.equals(new Position(x+scaleX, y+scaleY))) {
            this.setP(pos);
        }
    }

    public String getType() { return "Cavalier"; }
}
