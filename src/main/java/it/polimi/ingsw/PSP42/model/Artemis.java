package it.polimi.ingsw.PSP42.model;

/**
 * Simple god able of moving 2 times (the starting position cannot be the same as the arrival)
 */
public class Artemis extends YourMoveGod {

    public Artemis (Worker w1, Worker w2) {
        super(w1, w2);
    }

    /**
     * Method to check if the target cell is covered by Artemis power
     * @param x position on x-axis of desired destination
     * @param y position on y-axis of desired destination
     * @param w worker that want to move
     * @return true if the cell specified is reachable, false otherwise
     */
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        Cell[] availableCell = adjacentCellMovePowerAvailable(w);
        for (int i = 0; i < availableCell.length && availableCell[i] != null; i++){
            if (availableCell[i].equals(GameBoard.getInstance().getCell(x, y)))
                return true;
        }
        return false;
    }

    /**
     *  Method to set Artemis position
     * @param x position on x-axis of destination
     * @param y position on y-axis of destination
     * @param w worker that want to move
     */
    public void setPowerPosition(int x, int y, Worker w){
        w.setPosition(x, y);
    }

    /**
     * Method to control all available cells using Artemis power
     * @param w worker dressed to Artemis
     * @return cellPowerAvailable array containing all valid target cells
     */
    public Cell[] adjacentCellMovePowerAvailable(Worker w){
        /*this method verifies the positions available immediately around the worker and
        for each of these it checks which cells are accessible to her*/
        int currentX = w.getCurrentX();
        int currentY = w.getCurrentY();
        int index = 0;
        Cell[][] firstStepCell = GameBoard.getInstance().submatrixGenerator(currentX, currentY);
        Cell[][] secondStepCell = new Cell[3][3];
        Cell[] cellPowerAvailable = new Cell[64];
        for (int i = 0; i < 3; i++) {    //looking for the next available cells around
            for (int j = 0; j < 3; j++) {
                if (firstStepCell[i][j] != null &&                                          // c cell isn't out of map and
                    (firstStepCell[i][j].getWorker() == null) &&                            // there isn't a worker and
                    (firstStepCell[i][j].getLevel() != 4) &&                                // is not 4th level and
                    ((firstStepCell[i][j].getLevel() - GameBoard.getInstance().getCell(currentX, currentY).getLevel() <= 1) ||      // one gap level on ascent or
                     (firstStepCell[i][j].getLevel() - GameBoard.getInstance().getCell(currentX, currentY).getLevel() >= - 3)))      // limit for the descent
                {
                    secondStepCell = GameBoard.getInstance().submatrixGenerator(currentX-1+i, currentY-1+j);
                    for (int r = 0; r < 3; r++) {    //looking for the next available cells around
                        for (int c = 0; c < 3; c++) {
                            if (secondStepCell[r][c] != null &&                                          // c cell isn't out of map and
                                secondStepCell[r][c] != GameBoard.getInstance().getCell(currentX-1+i, currentY-1+j) &&
                                (secondStepCell[r][c].getWorker() == null) &&                            // there isn't a worker and
                                (secondStepCell[r][c].getLevel() != 4) &&                                // is not 4th level and
                                ((secondStepCell[r][c].getLevel() - GameBoard.getInstance().getCell(currentX-1+i, currentY-1+j).getLevel() <= 1) ||      // one gap level on ascent or
                                 (secondStepCell[r][c].getLevel() - GameBoard.getInstance().getCell(currentX-1+i, currentY-1+j).getLevel() >= - 3)))      // limit for the descent
                            {
                                cellPowerAvailable[index] = secondStepCell[r][c];
                                index++;
                            }
                        }
                    }
                }
            }
        }
        return cellPowerAvailable;
    }
}