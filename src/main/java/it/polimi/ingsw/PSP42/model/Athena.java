package it.polimi.ingsw.PSP42.model;

/**
 * TODO
 * Thanks to this simple god if a player's worker, who have this god, step up, the workers of other players can't.
 */
public class Athena extends SimpleGod {

    public Athena(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"NULL"};
        String[] preMove = {"NULL"};
        String[] move = {"move"};
        String[] preBuild = {"NULL"};
        String[] build = {"build"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        //TODO
    }

    @Override
    public boolean powerMove(int x, int y, Worker w) {
        //TODO
    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        //TODO
    }

    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        //TODO
    }

    @Override
    public boolean powerEffectAvailable() {
        return false;
    }

    @Override
    public boolean powerEffect() {
        return false;
    }

    @Override
    public String[][] getWhatToDo() {
        return phase;
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