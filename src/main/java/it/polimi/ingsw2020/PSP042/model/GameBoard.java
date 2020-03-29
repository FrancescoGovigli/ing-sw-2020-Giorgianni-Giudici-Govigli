package it.polimi.ingsw2020.PSP042.model;

public class GameBoard {

    public boolean moveAvailable(int x, int y) {
        boolean condition = false;
        Cell[] c = adjacentCellAvailable(w.getCurrentX(),w.getCurrentY());
        for(int i=0; i < c.length; i++)
            if(c[i] == getCell(x,y))
                condition = true;
        return condition;
    }

    public boolean buildAvailable(int x, int y) {
        boolean condition = false;
    }

    public boolean workerAvailable(Worker w) {
        if(adjacentCellAvailable(w.getCurrentX(),w.getCurrentY())[0]=false)
            w.setAvailable(false);
        else
            w.setAvailable(true);
        return w.getAvailable();
    }
}

