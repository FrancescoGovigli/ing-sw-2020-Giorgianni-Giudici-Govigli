package it.polimi.ingsw.PSP42.model;

/**
 * Thanks to this simple god if a player's worker, who have this god, step up, the workers of other players can't.
 */
public class Athena extends OpponentTurnGod {

    public Athena(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public boolean powerAvailable(int x, int y, Worker w) {
        return false;
    }

    @Override
    public void setPower(int x, int y, Worker w) {

    }

    public boolean workerStepUp(int x, int y, Worker w) {
        if (GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getLevel() -
                GameBoard.getInstance().getCell(x, y).getLevel() == -1)
            return true;
        else
            return false;
    }

    public void blockOthersStepUp () {

    }

    public void godRequest() {

    }
}
