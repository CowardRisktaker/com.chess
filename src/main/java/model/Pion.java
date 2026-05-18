package model;

public class Pion extends Piece {
    public Pion (Position p, String couleur) {
        super(p, couleur);
    }

    public boolean coupLegal(int x, int y, int scaleX, int scaleY, Position from, Piece[][] plateau) {
        if (scaleY <= 1 && scaleX == 0) {
            Piece cible = plateau[x][y];
            if (plateau[from.getX()][from.getY()] == plateau[x][y]) return false;
            if (cible == null || !cible.getCouleur().equals(this.getCouleur())) { this.setP(new Position(x, y));
                return true;
            } return false;
        }
        return false;
    }


    public String getType() { return "Pion"; }
}

