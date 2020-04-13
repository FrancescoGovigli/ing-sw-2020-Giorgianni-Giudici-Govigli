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
        String[] preMove = {"MOVE"};
        String[] move = {"MOVE"};
        String[] preBuild = {"NULL"};
        String[] build = {"BUILD"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    /**
     * Method used to check if is possible move the worker in cell (x,y) and checking if he would not come back at starting position
     * @param x (x coordinate of where you would like to go)
     * @param y (y coordinate of where you would like to go)
     * @param w (worker who would like to move)
     * @return true if worker can be moved, false otherwise
     */
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

    /**
     * Method to build in a position and resetting at default value the attributes
     * @param x (x coordinate of where you would like to build)
     * @param y (y coordinate of where you would like to build)
     * @param w (worker who would like to build)
     * @return true if worker can build, false otherwise
     */
    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerBuildAvailable(x, y, level, w)){
            w.buildBlock(x, y);
            moveNum = 0;
            startX = 0;
            startY = 0;
            return true;
        }
        return false;
    }
}