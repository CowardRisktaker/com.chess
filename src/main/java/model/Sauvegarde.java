package model;

public class Sauvegarde {

    public Sauvegarde() {
    }
    public void sauverPartie() {

    }
    public static String sauver(Board board)  {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board.getPiece(x, y);
                if (piece == null) sb.append("null").append(";");
                else {
                    sb.append(piece.getType()).append("_").append(piece.getCouleur()).append("_").append(x).append(",").append(y);
                    sb.append(";");
                }
            }
        }
        return sb.toString();
    }
    public static String charger(Board board) {
        return null;
    }
}

