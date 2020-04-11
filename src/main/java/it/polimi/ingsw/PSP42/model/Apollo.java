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
        String[] move = {"move"};
        String[] preBuild = {"NULL"};
        String[] build = {"build"};
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
    public boolean powerMove(int x, int y, Worker w) {
        Worker toSwap = null;
        int tempPosX = 0;
        int tempPosY = 0;
        if (powerMoveAvailable(x, y, w)){
            toSwap = GameBoard.getInstance().getCell(x, y).getWorker();
            tempPosX = toSwap.getCurrentX();
            tempPosY = toSwap.getCurrentY();
            toSwap.setPosition(w.getCurrentX(), w.getCurrentY());
            w.setPosition(tempPosX, tempPosY);
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
        for (int i = 0; i < 3; i++) {   //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                                      // c cell isn't out of map and and
                    (c[i][j].getWorker() == null ||                         // (there is no worker in the cell or
                            c[i][j].getWorker().getPlayer() != w.getPlayer()) &&   // worker to be exchanged is not of the same player) and
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