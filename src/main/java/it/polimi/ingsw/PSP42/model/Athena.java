package it.polimi.ingsw.PSP42.model;

/**
 * Thanks to this simple god if a player's worker, who have this god, step up, the workers of other players can't.
 */
public class Athena extends SimpleGod {

    public Athena(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public void godHashMap() {

    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        return false;
    }

    @Override
    public void powerMove(int x, int y, Worker w) {

    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        return false;
    }

    @Override
    public void powerBuild(int x, int y, int level, Worker w) {

    }

    @Override
    public boolean powerEffectAvailable() {
        return false;
    }

    @Override
    public void powerEffect() {

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
}