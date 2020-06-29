package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

public abstract class SimpleGod {

    protected ArrayList<Player> playersWithEffect = new ArrayList<>();  // players that influence current Player
    protected String[][] phase;
    protected Worker w1;
    protected Worker w2;
    private ArrayList<Integer> values = null;   // UNDO

    public SimpleGod(Worker w1, Worker w2) {
        this.w1 = w1;
        this.w2 = w2;
        this.phase = setPhase();
    }

    public abstract String[][] setPhase();

    /**
     * Method to check if worker can move to cell (x,y).
     * @param x (x position of where he wants to go)
     * @param y (y position of where he wants to go)
     * @param w (concerned worker)
     * @return true if the cell is available for the move, false otherwise
     */
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (GameBoard.getInstance().moveAvailable(x, y, w))
            return true;
        return false;
    }

    /**
     * Method used to apply the move.
     * @param x (x position of the destination)
     * @param y (y position of the destination)
     * @param w (concerned worker)
     * @return true if the move was successful, false otherwise
     */
    public boolean powerMove(int x, int y, Worker w) {
        for (Player player : playersWithEffect)
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        if (powerMoveAvailable(x, y, w)) {
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    /**
     * Method to check if worker can build in cell (x,y).
     * @param x (x position of where he wants to build)
     * @param y (y position of where he wants to build)
     * @param level (level he wants to build)
     * @param w (concerned worker)
     * @return true if the cell is available for the build, false otherwise
     */
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (GameBoard.getInstance().buildAvailable(x, y, w))
            return true;
        return false;
    }

    /**
     * Method used to apply the build.
     * @param x (x position of the building)
     * @param y (y position of the building)
     * @param level (level to build)
     * @param w (concerned worker)
     * @return true if the construction was successful, false otherwise
     */
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerBuildAvailable(x, y, level, w)) {
            w.buildBlock(x, y);
            return true;
        }
        return false;
    }

    public boolean powerEffectAvailable() {
        return false;
    }

    public boolean powerEffect() {
        return false;
    }

    public String effectON() {
        return null;
    }

    public String effectOFF() {
        return null;
    }

    /**
     * Method used to set the starting position of the worker.
     * @param x (starting position x)
     * @param y (starting position y)
     * @param w (concerned worker)
     * @return true if the position is available, false otherwise
     */
    public boolean powerInitialPosition(int x, int y, Worker w) {
        if (w.getCurrentX() == -1 && w.getCurrentY() == -1 &&   // if w is out of map and
            GameBoard.getInstance().getCell(x, y).getWorker() == null &&    // the initial cell is free and
            GameBoard.getInstance().getCell(x, y).getLevel() == 0) {        // the cell level is 0 (ground)
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    /**
     * Method used to obtain how the god behaves during his turn
     * @return phase (matrix of strings where the first column indicates the phase of the turn and each row,
     *                starting from the second column, indicates the moves available in the phase indicated
     *                by the first column)
     */
    public String[][] getWhatToDo() {
        return phase;
    }

    /**
     * Method to obtain the current state of the Simple God's variables
     * [NOTE] In the Simple God that makes override it is important to insert
     * the variables in the ArrayList in the order in which they were declared in the class,
     * in other words the first variable declared must be insert in the first position of values
     * @return values.clone() (a clone of the ArrayList of Integer containing these variables)
     */
    public ArrayList<Integer> getCurrentValues() {
        if (values != null)
            return (ArrayList<Integer>) values.clone();
        return null;
    }

    /**
     * Method to restore the state of the Simple God's variables.
     * [NOTE] In the Simple God that make it override it is important
     * that the variables to be reset are taken from the ArrayList in the order in which they were declared in the class,
     * in other words the first variable declared is in the first position of valuesToRestore
     * @param valuesToRestore possible values to restore after an undo
     */
    public void reSetValues(ArrayList<Integer> valuesToRestore) {
        values = null;
    }
}
