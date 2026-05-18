package model;

public class Cavalier extends Piece {
    public Cavalier(Position p, String couleur) {
        super(p, couleur);
    }


    public boolean coupLegal(int x, int y, int scaleX, int scaleY, Position from, Piece[][] plateau) {
        if ((scaleX == 1 && scaleY == 2) || (scaleX == 2 && scaleY == 1)) {
            if (plateau[from.getX()][from.getY()] == plateau[x][y]) return false;
            Piece cible = plateau[x][y];
            if (cible == null || !cible.getCouleur().equals(this.getCouleur())) {
                this.setP(new Position(x, y));
                return true;
            } else return false;
        }
        return false;
    }


    public String getType() { return "Cavalier"; }
}
