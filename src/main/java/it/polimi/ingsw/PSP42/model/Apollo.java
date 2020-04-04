package it.polimi.ingsw.PSP42.model;

import javax.swing.*;

public class Apollo extends YourMoveGod {

    public Apollo(Worker w1,Worker w2) {
        super(w1,w2);
    }

    //verifica se oltre che alle celle disponibili normalmente la mossa va in una cella occupata da worker e in quel caso scambia
    public boolean powerMoveAvailable(int x,int y,Worker w) {
        Cell[] adj = this.adjacentCellMovePowerAvailable(x,y,w); // Ritorna celle disponibili normali + potere
        for (int i = 0; i < adj.length; i++) {
            if (GameBoard.getInstance().getCell(x,y).equals(adj[i]))
                return true;
        }
        return false;
    }

    public void setPowerPosition(int x,int y,Worker w){
        Worker change;
        int tempPosx = 0;
        int tempPosy;
        if((change = GameBoard.getInstance().getCell(x,y).getWorker())!=null)
            tempPosx = change.getCurrentX();
            tempPosy = change.getCurrentY();
            change.setPosition(w.getCurrentX(),w.getCurrentY());
            w.setPosition(tempPosx,tempPosy);

    }


    // se la cella è occupata da un worker la metto nell'array e mi salvo in un arraylist i riferimenti dei worker che probabilmente dovrò scambiare
    public Cell[] adjacentCellMovePowerAvailable(int x, int y,Worker w){
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(w.getCurrentX(), w.getCurrentY());
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                                          // c cell isn't out of map and and
                        (c[i][j].getLevel() != 4) &&                                // is not 4th level and
                        ((c[i][j].getLevel() - GameBoard.getInstance().getCell(x,y).getLevel() <= 1) ||      // one gap level on ascent and
                                (c[i][j].getLevel() - GameBoard.getInstance().getCell(x,y).getLevel() >= - 3)))      // limit for the descent
                {
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;

    }


}

