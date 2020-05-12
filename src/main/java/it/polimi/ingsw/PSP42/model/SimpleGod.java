package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

public abstract class SimpleGod {

    protected ArrayList<Player> effectPlayers = new ArrayList<>();
    protected String[][] phase;
    protected Worker w1;
    protected Worker w2;

    // UNDO
    private ArrayList<Integer> values = null;

    /**
     * Method to obtain the current state of the Simple God's variables
     * [NOTE] In the Simple God that make it override it is important to insert
     * the variables in the ArrayList in the order in which they were declared in the class,
     * in other words the first variable declared must be insert in the first position of values
     * @return values.clone() (a clone of the ArrayList of Integer containing these variables)
     */
    public ArrayList<Integer> getCurrentValues() {
        if(values != null)
            return (ArrayList<Integer>) values.clone();
        return null;
    }

    /**
     * Method to restore the state of the Simple God's variables
     * [NOTE] In the Simple God that make it override it is important
     * that the variables to be reset are taken from the ArrayList in the order in which they were declared in the class,
     * in other words the first variable declared is in the first position of valuesToRestore
     * @param valuesToRestore
     */
    public void reSetValues(ArrayList<Integer> valuesToRestore) {
        values = null;
    }

    // CURRENT CODE
    public SimpleGod(Worker w1, Worker w2) {
        this.w1 = w1;
        this.w2 = w2;
        this.phase = setPhase();
    }

    public abstract String[][] setPhase();

    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (GameBoard.getInstance().moveAvailable(x, y, w))
            return true;
        return false;
    }

    public boolean powerMove(int x, int y, Worker w) {
        for (Player player: effectPlayers)
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        if (powerMoveAvailable(x, y, w)) {
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (GameBoard.getInstance().buildAvailable(x, y, w))
            return true;
        return false;
    }

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

    public boolean powerInitialPosition(int x, int y, Worker w) {
        if (w.getCurrentX() == -1 && w.getCurrentY() == -1 &&   // if w is out of map and
            GameBoard.getInstance().getCell(x, y).getWorker() == null &&    // the initial cell is free and
            GameBoard.getInstance().getCell(x, y).getLevel() == 0) {        // the cell level is 0 (ground)
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    public String[][] getWhatToDo() {
        return phase;
    }

}