package model;


public class Board {
    Piece[][] plateau = new Piece[8][8];
public Board() {
    initPieces();
}
    private void initPieces() {

        plateau[0][0] = new Tour(new Position(0, 0), "Noir") ;
        plateau[1][0] = new Cavalier(new Position(1, 0),"Noir");
        plateau[2][0] = new Fou(new Position(2, 0),"Noir");
        plateau[3][0] = new Reine(new Position(3, 0), "Noir");
        plateau[4][0] = new Roi(new Position(4, 0), "Noir");
        plateau[5][0] = new Fou(new Position(5, 0), "Noir");
        plateau[6][0] = new Cavalier(new Position(6, 0), "Noir");
        plateau[7][0] = new Tour(new Position(7, 0), "Noir");

        for (int x = 0; x < 8; x++) {
            plateau[x][1] = new Pion(new Position(x, 1), "Noir");
        }

        for (int x = 0; x < 8; x++) {
            plateau[x][6] = new Pion(new Position(x, 6), "Blanc");
        }

        plateau[0][7] = new Tour(new Position(0, 7), "Blanc");
        plateau[1][7] = new Cavalier(new Position(1, 7), "Blanc");
        plateau[2][7] = new Fou(new Position(2, 7), "Blanc");
        plateau[3][7] = new Reine(new Position(3, 7), "Blanc");
        plateau[4][7] = new Roi(new Position(4, 7), "Blanc");
        plateau[5][7] = new Fou(new Position(5, 7), "Blanc");
        plateau[6][7] = new Cavalier(new Position(6, 7), "Blanc");
        plateau[7][7] = new Tour(new Position(7, 7), "Blanc");
    }




    public Piece[][] getPlateau() {
        return plateau;
    }

    public Piece getPiece(int x, int y) {
        return this.plateau[x][y];
    }

    }

