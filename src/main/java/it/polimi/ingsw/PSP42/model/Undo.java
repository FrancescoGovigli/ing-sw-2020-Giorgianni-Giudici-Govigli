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
    private ArrayList <Integer> godValuesToRestore;  // used to contains possible specific SimpleGod's values

    public Undo() {
        this.undoMove = false;
        this.undoBuild = false;
        this.godValuesToRestore = null;
    }

    // UNDO MOVE

    public void undoMoveSet(Worker worker){
        this.undoMoveX = worker.getCurrentX();
        this.undoMoveY = worker.getCurrentY();
        if (worker.getPlayer().getCard().getCurrentValues() != null)
            this.godValuesToRestore = worker.getPlayer().getCard().getCurrentValues();
    }

    public void undoMovePossible(){
        this.undoMove = true;
    }

    public void undoMoveApply(Worker worker){
        //int toFreeX = worker.getCurrentX(); // useless if setPosition() in worker do unSetPosition()
        //int toFreeY = worker.getCurrentY();
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

    public void undoBuildSet(int x, int y, Worker worker){
        this.undoBuildX = x;
        this.undoBuildY = y;
        this.undoBuildLevel = GameBoard.getInstance().getCell(x, y).getLevel();
        if (worker.getPlayer().getCard().getCurrentValues() != null)
            this.godValuesToRestore = worker.getPlayer().getCard().getCurrentValues();
    }

    public void undoBuildPossible(){
        this.undoBuild = true;
    }

    public void undoBuildApply(Worker worker){
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
