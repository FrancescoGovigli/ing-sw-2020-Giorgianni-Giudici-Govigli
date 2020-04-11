package it.polimi.ingsw.PSP42.model;

import javax.swing.*;

/**
 * Simple god that allowed a worker to move in a cell occupied by another worker, switching their position.
 */
public class Apollo extends SimpleGod {

    public Apollo(Worker w1,Worker w2) {
        super(w1, w2);
    }

    @Override
    public void godHashMap() {

    }

    /**
     * Used to verify if the worker is able to move even if there is an other worker on the other cell.
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param w worker who wants to move
     * @return true if worker is able to move, false otherwise
     */
    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        Cell[] adj = this.adjacentCellMovePowerAvailable(x, y, w);
        for (int i = 0; i < adj.length; i++) {
            if (GameBoard.getInstance().getCell(x, y).equals(adj[i]))
                return true;
        }
        return false;
    }

    /**
     * Used to move in a cell occupied by another worker and switch the two positions.
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param w worker who wants to move
     */
    @Override
    public void powerMove(int x, int y, Worker w) {
        Worker change;
        int tempPosX = 0;
        int tempPosY;
        if((change = GameBoard.getInstance().getCell(x, y).getWorker())!= null) {
            tempPosX = change.getCurrentX();
            tempPosY = change.getCurrentY();
            change.setPosition(w.getCurrentX(), w.getCurrentY());
            w.setPosition(tempPosX, tempPosY);
        }
    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        return GameBoard.getInstance().buildAvailable(x, y, w);
    }

    @Override
    public void powerBuild(int x, int y, int level, Worker w) {
        w.buildBlock(x, y);
    }

    @Override
    public boolean powerEffectAvailable() {
        return false;
    }

    @Override
    public void powerEffect() {

    }

    /**
     * Used to know where the worker can move even if the cell is occupied by another worker.
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param w worker who wants to move
     * @return a position's array of all the possible moves
     */
    public Cell[] adjacentCellMovePowerAvailable(int x, int y, Worker w){
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(w.getCurrentX(), w.getCurrentY());
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                             // c cell isn't out of map and and
                        (c[i][j].getLevel() != 4) &&                  // is not 4th level and
                        ((c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() <= 1) &&      // one gap level on ascent and
                                (c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() >= - 3)))      // limit for the descent
                {
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;
    }
}