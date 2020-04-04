package it.polimi.ingsw.PSP42.model;

public class Athena extends OpponentTurnGod {

    public Athena(Worker w) {
        super(w);
    }

    public boolean workerStepUp(int x, int y) {
        if (GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getLevel() -
                GameBoard.getInstance().getCell(x, y).getLevel() == -1)
            return true;
        else
            return false;
    }

    public void blockOthersStepUp () {

    }
}
