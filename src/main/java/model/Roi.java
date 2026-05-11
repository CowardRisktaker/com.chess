package model;

import java.util.ArrayList;

public class Roi extends Piece {
     int distMax = 1;
    ArrayList<Position> sorties ;
    public Roi(Position p, String couleur) {
        super(p, couleur);
        sorties = new ArrayList<Position>();
        //sortiesPossible();
    }

    @Override
    public boolean coupLegal(int x, int y, int n) {

    }

    @Override
    public boolean coupLegal(int x, int y, int scaleX, int scaleY) {

    }

    public void sortiesPossible() {
        int x = this.getP().getX();
        int y = this.getP().getY();
        sorties.add(new Position(x - distMax, y - distMax ));
        sorties.add(new Position(x - distMax, y ));
        sorties.add(new Position(x - distMax, y + distMax ));
        sorties.add(new Position(x, y - distMax ));
        sorties.add(new Position(x, y));
        sorties.add(new Position(x, y + distMax ));
        sorties.add(new Position(x + distMax, y - distMax ));
        sorties.add(new Position(x + distMax, y));
        sorties.add(new Position(x + distMax, y + distMax ));

        int[] sortiePossibleX =     {x + distMax, x + distMax, x + distMax, x, x, x, x - distMax, x - distMax, x - distMax};
        int[] getSortiePossibleY =  {y + distMax, y + distMax, y + distMax, y, y, y, y - distMax, y - distMax, y - distMax};
    }
    public String getType() { return "Roi"; }
}

