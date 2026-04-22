package model;

public abstract class Piece {
    Position p;
    private final String couleur;


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


    public abstract String getType() ;
}

