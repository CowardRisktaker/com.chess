package model;

public class Fou extends Piece {

    public Fou(Position p, String couleur) {
        super(p, couleur);
    }

    @Override
    public boolean coupLegal(int x, int y, int scaleX, int scaleY, Position from, Piece[][] plateau) {
        int startX = from.getX();
        int startY = from.getY();
        if (scaleX == scaleY) {
            int stepX = (x > startX) ? 1 : -1;
            int stepY = (y > startY) ? 1 : -1;
            if (plateau[startX][startY] == plateau[x][y]) return false;
            for (int cx = startX + stepX, cy = startY + stepY; cx != x && cy != y; cx += stepX, cy += stepY) {
                if (plateau[cx][cy] != null) return false;
            }
            Piece cible = plateau[x][y];
            if (cible == null || !cible.getCouleur().equals(this.getCouleur())) {
                this.setP(new Position(x, y));
                return true;
            }
            return false;
        }
        return false;
    }

    public String getType() { return "Fou"; }
}
