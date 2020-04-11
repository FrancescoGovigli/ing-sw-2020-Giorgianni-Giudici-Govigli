package it.polimi.ingsw.PSP42.model;

/**
 * Simple god able to move 2 times (the starting position cannot be the same as the arrival)
 */
public class Artemis extends SimpleGod {

    private int moveNum = 0;
    private int startX = 0;
    private int startY = 0;

    public Artemis (Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"NULL"};
        String[] preMove = {"move"};
        String[] move = {"move"};
        String[] preBuild = {"NULL"};
        String[] build = {"build"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (moveNum == 0){
            startX = w.getCurrentX();
            startY = w.getCurrentY();
        }
        if (startX != x && startY != y && GameBoard.getInstance().moveAvailable(x, y, w)) {
            moveNum = moveNum + 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean powerMove(int x, int y, Worker w) {
        if (powerMoveAvailable(x, y, w)) {
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (GameBoard.getInstance().buildAvailable(x, y, w))
            return true;
        return false;
    }

    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerMoveAvailable(x, y, w)){
            w.buildBlock(x, y);
            moveNum = 0;
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