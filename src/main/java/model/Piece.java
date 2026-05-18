package model;

public abstract class Piece {
    Position        p;
    private String  couleur;

    public Piece() {
        this.p = new Position(p.getX(), p.getY());
        this.couleur = couleur ;
    }
    public Piece(Position p, String couleur ) {
        this.p = new Position(p.getX(), p.getY());
        this.couleur = couleur ;
    }

    public String getCouleur() {
        return this.couleur;
    }

    public Position getP() {
        return p;
    }

    public void setP(Position p) {
        this.p = p;
    }

    public void setNull() { this.p = null; }


    public abstract boolean coupLegal(int x, int y, int scaleX, int scaleY, Position from, Piece[][] plateau);

    public abstract String getType() ;

    @Override
    public String toString() {
        return "couleur= " + couleur +
            "; type = " + getType() +
            "; position = " + p.toString();
    }
}


