package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

/**
 * Simple God who can build before moving only if he does not move up while moving.
 */
public class Prometheus extends SimpleGod {

    private int buildNum = 0;
    private int moveNum = 0;

    public Prometheus(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"EMPTY"};
        String[] preMove = {"BUILD"};
        String[] move = {"MOVE"};
        String[] preBuild = {"EMPTY"};
        String[] build = {"BUILD"};
        String[] end = {"EMPTY"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    /**
     * Method used to move the worker in cell (x,y) and checking if he building before.
     * @param x (x coordinate of where you would like to go)
     * @param y (y coordinate of where you would like to go)
     * @param w (worker who would like to move)
     * @return true if worker can be moved, false otherwise
     */
    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (buildNum == 0 && GameBoard.getInstance().moveAvailable(x, y, w))    // if building pre-move was not done
            return true;
        if (buildNum != 0 &&    // if building pre-move was done &&
            (GameBoard.getInstance().getCell(x, y).getLevel() <=    // (the level of the future cell is less then or equal to
             GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getLevel()) &&   // the level of the current cell) &&
            GameBoard.getInstance().moveAvailable(x, y, w)) // the future cell is available
            return true;
        return false;
    }

    /**
     * Method used to move the worker in cell (x,y).
     * @param x (x coordinate of where you would like to go)
     * @param y (y coordinate of where you would like to go)
     * @param w (worker who would like to move)
     * @return true if worker can be moved, false otherwise
     */
    @Override
    public boolean powerMove(int x, int y, Worker w) {
        for (Player player : playersWithEffect) {
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        }
        if (powerMoveAvailable(x, y, w)) {
            w.setPosition(x, y);
            moveNum = 1;
            return true;
        }
        return false;
    }

    /**
     * Method to build in a position and counting how many times he has built in a turn.
     * @param x (x coordinate of where you would like to build)
     * @param y (y coordinate of where you would like to build)
     * @param w (worker who would like to build)
     * @return true if worker can build, false otherwise
     */
    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerBuildAvailable(x, y, level, w)) {
            if (moveNum == 0)
                buildNum = 1;
            if (moveNum == 1) {
                buildNum = 0;
                moveNum = 0;
            }
            w.buildBlock(x, y);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Integer> getCurrentValues() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(buildNum);
        values.add(moveNum);
        return (ArrayList<Integer>) values.clone();
    }

    @Override
    public void reSetValues(ArrayList<Integer> valuesToRestore) {
        this.buildNum = valuesToRestore.get(0);
        this.moveNum = valuesToRestore.get(1);
    }
}
