package it.polimi.ingsw.PSP42.model;

/**
 * Simple god that allowed a worker to move in a cell occupied by another worker, switching their position.
 */
public class Apollo extends SimpleGod {

    public Apollo(Worker w1,Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"NULL"};
        String[] preMove = {"NULL"};
        String[] move = {"MOVE"};
        String[] preBuild = {"NULL"};
        String[] build = {"BUILD"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
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
        Cell[] adj = this.adjacentCellMovePowerAvailable(w.getCurrentX(), w.getCurrentY());
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
    public boolean powerMove(int x, int y, Worker w) {
        for (Player effectPlayer : effectPlayers) {
            if (effectPlayer != null && !effectPlayer.getCard().powerMoveAvailable(x, y, w))
                return false;
        }
        if (powerMoveAvailable(x, y, w)){
            Worker toSwap = GameBoard.getInstance().getCell(x, y).getWorker();
            if(toSwap!=null) {
                int tempPosX = w.getCurrentX();
                int tempPosY = w.getCurrentY();
                toSwap.unSetPosition();
                w.setPosition(x, y);
                toSwap.setPosition(tempPosX, tempPosY);
            }
            else
                w.setPosition(x,y);
            return true;
        }
        return false;
    }

    /**
     * Used to know where the worker can move even if the cell is occupied by another worker.
     * @param x starting position on the x-axis
     * @param y starting position on the y-axis
     * @return a position's array of all the possible moves
     */
    public Cell[] adjacentCellMovePowerAvailable(int x, int y){
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {   //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                                      // c cell isn't out of map and and
                    (c[i][j].getWorker() == null ||                         // (there is no worker in the cell or
                            c[i][j].getWorker().getPlayer() != GameBoard.getInstance().getCell(x, y).getWorker().getPlayer()) &&   // worker to be exchanged is not of the same player) and
                    (c[i][j].getLevel() != 4) &&                            // is not 4th level and
                    ((c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() <= 1) &&    // one gap level on ascent and
                     (c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() >= - 3)))   // limit for the descent
                {
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;
    }
}