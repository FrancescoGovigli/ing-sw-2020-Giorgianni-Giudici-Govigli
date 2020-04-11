package it.polimi.ingsw.PSP42.model;

/**
 * Simple God who can build before moving if he does not move up a level while moving
 */
public class Prometheus extends SimpleGod{

    private int buildNum = 0;
    private int moveNum = 0;

    public Prometheus(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"NULL"};
        String[] preMove = {"build"};
        String[] move = {"move"};
        String[] preBuild = {"NULL"};
        String[] build = {"build"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (buildNum == 0 && GameBoard.getInstance().moveAvailable(x, y, w))    // if building pre-move was not done
            return true;
        if (buildNum != 0 &&    // if building pre-move was done &&
            (GameBoard.getInstance().getCell(x, y).getCellLevel() !=    // (the level of the future cell is not the same as the current one
             GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getCellLevel()) &&   // as the current one) &&
            GameBoard.getInstance().moveAvailable(x, y, w)) // the future cell is available
            return true;
        return false;
    }

    @Override
    public boolean powerMove(int x, int y, Worker w) {
        if (powerMoveAvailable(x, y, w)) {
            w.setPosition(x, y);
            buildNum = 0;
            moveNum = 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (GameBoard.getInstance().buildAvailable(x, y, w)) {
            if (moveNum == 0)
                buildNum = 1;
            if (moveNum == 1) {
                moveNum = 0;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerMoveAvailable(x, y, w)){
            w.buildBlock(x, y);
            return true;
        }
        return false;
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
}