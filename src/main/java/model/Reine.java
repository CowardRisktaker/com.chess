package model;

public class Reine extends Piece{
    public Reine(Position p, String couleur) {
        super(p, couleur);
    }


    @Override
    public boolean coupLegal(int x, int y, int scaleX, int scaleY, Position from, Piece[][] plateau) {
        int startX = from.getX();
        int startY = from.getY();
        if ((scaleX == 0 || scaleY == 0) || (scaleX == scaleY)) {
            int stepX = (x > startX) ? 1 : -1;
            int stepY = (y > startY) ? 1 : -1;
            if (plateau[startX][startY] == plateau[x][y]) return false;
            if (scaleX == scaleY) {
                for (int cx = startX + stepX, cy = startY + stepY; cx != x && cy != y; cx += stepX, cy += stepY) {
                    if (plateau[cx][cy] != null) return false;
                }
                Piece cible = plateau[x][y];
                if (cible == null || !cible.getCouleur().equals(this.getCouleur())) {
                    this.setP(new Position(x, y));
                    return true;
                }
            }
            if (scaleY == 0) {
                int step = (x > startX) ? 1 : -1;
                if (plateau[startX][startY] == plateau[x][y]) return false;
                for (int cx = startX + step; cx != x; cx += step) {
                    if (plateau[cx][startY] != null) return false;
                }
                Piece cible = plateau[x][startY];
                if (cible == null || !cible.getCouleur().equals(this.getCouleur())) {
                    this.setP(new Position(x, y));
                    return true;
                }
                return false;
            }
        }
        if (scaleX == 0) {
            int step = (y > startY) ? 1 : -1;
            for (int cy = startY + step; cy != y; cy += step) {
                if (plateau[startX][cy] != null) return false;
            }
            Piece cible = plateau[startX][y];
            if (cible == null || !cible.getCouleur().equals(this.getCouleur())) {
                this.setP(new Position(x, y));
                return true;
            }
            return false;
        }
        return false;
    }

    public String getType() { return "Reine"; }
}
