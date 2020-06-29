package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

public class Undo {

    private boolean undoMove;    // used to check if is possible do an undo_move
    private boolean undoBuild;   // used to check if is possible do an undo_build
    private int undoMoveX;       // the x value for the step_back
    private int undoMoveY;       // the y value for the step_back
    private int undoBuildX;      // the x value for the build_back
    private int undoBuildY;      // the y value for the build_back
    private int undoBuildLevel;  // the level to reset in cell (x,y)
    private ArrayList<Integer> godValuesToRestore;  // used to contains possible specific SimpleGod's values

    public Undo() {
        this.undoMove = false;
        this.undoBuild = false;
        this.godValuesToRestore = null;
    }

    // UNDO MOVE

    /**
     * Method for acquiring the position of the worker before he moves.
     * @param worker interested Worker
     */
    public void undoMoveSet(Worker worker) {
        this.undoMoveX = worker.getCurrentX();
        this.undoMoveY = worker.getCurrentY();
        if (worker.getPlayer().getCard().getCurrentValues() != null)
            this.godValuesToRestore = worker.getPlayer().getCard().getCurrentValues();
    }

    public void undoMovePossible(){
        this.undoMove = true;
    }

    /**
     * Method for restoring worker position before his move.
     * @param worker interested Worker
     */
    public void undoMoveApply(Worker worker) {
        if (undoMove) {
            worker.setPosition(undoMoveX, undoMoveY);
            if (godValuesToRestore != null)
                worker.getPlayer().getCard().reSetValues((ArrayList<Integer>) godValuesToRestore.clone());
        }
    }

    public void undoMoveDone(){
        this.undoMove = false;
    }

    // UNDO BUILD

    /**
     * Method for acquiring cell values before it is built on it.
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param worker interested Worker
     */
    public void undoBuildSet(int x, int y, Worker worker) {
        this.undoBuildX = x;
        this.undoBuildY = y;
        this.undoBuildLevel = GameBoard.getInstance().getCell(x, y).getLevel();
        if (worker.getPlayer().getCard().getCurrentValues() != null)
            this.godValuesToRestore = worker.getPlayer().getCard().getCurrentValues();
    }

    public void undoBuildPossible(){
        this.undoBuild = true;
    }

    /**
     * Method for restoring cell state before construction.
     * @param worker interested Worker
     */
    public void undoBuildApply(Worker worker) {
        if (undoBuild) {
            GameBoard.getInstance().getCell(undoBuildX, undoBuildY).setSpecificCellLevel(undoBuildLevel);
            if (godValuesToRestore != null)
                worker.getPlayer().getCard().reSetValues((ArrayList<Integer>) godValuesToRestore.clone());
        }
    }

    public void undoBuildDone(){
        this.undoBuild = false;
    }
}
