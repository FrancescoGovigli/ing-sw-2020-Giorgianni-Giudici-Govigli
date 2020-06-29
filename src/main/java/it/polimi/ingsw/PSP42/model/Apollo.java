package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

/**
 * Simple god that allows a worker to move in a cell occupied by another worker, switching their positions.
 */
public class Apollo extends SimpleGod {

    private int apolloX = -1;
    private int apolloY = -1;
    private int opponentX = -1;
    private int opponentY = -1;
    private int moveNum = 0;

    public Apollo(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"EMPTY"};
        String[] preMove = {"EMPTY"};
        String[] move = {"MOVE"};
        String[] preBuild = {"EMPTY"};
        String[] build = {"BUILD"};
        String[] end = {"EMPTY"};
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
            if (GameBoard.getInstance().getCell(x, y).equals(adj[i])) {
                GameBoard.getInstance().winCondition(x, y, w);
                return true;
            }
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
        for (Player player : playersWithEffect) {
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        }
        if (powerMoveAvailable(x, y, w)){
            moveNum = 1;
            Worker toSwap = GameBoard.getInstance().getCell(x, y).getWorker();
            if (toSwap != null) {
                int tempPosX = w.getCurrentX();
                int tempPosY = w.getCurrentY();
                toSwap.unSetPosition();
                w.setPosition(x, y);
                toSwap.setPosition(tempPosX, tempPosY);
                // UNDO
                apolloX = tempPosX; // position of the worker before the swap
                apolloY = tempPosY;
                opponentX = x;      // position of the opposing worker before the swap
                opponentY = y;
            }
            else {
                w.setPosition(x, y);
                // UNDO
                apolloX = -1;   // swap not done, then restore the variables to their default values
                apolloY = -1;
                opponentX = -1;
                opponentY = -1;
            }
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

    /**
     * "Standard" power build, as in "SimpleGod", plus setting of param "moveNum" for Undo functionalities.
     * @param x position on x-axis where worker wants to build
     * @param y position on y-axis where worker wants to build
     * @param level block level to build
     * @param w worker
     * @return true if worker builds, false otherwise
     */
    public boolean powerBuild(int x, int y, int level, Worker w) {
        moveNum = 0;
        if (powerBuildAvailable(x, y, level, w)) {
            w.buildBlock(x, y);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Integer> getCurrentValues() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(apolloX);
        values.add(apolloY);
        values.add(opponentX);
        values.add(opponentY);
        values.add(moveNum);
        return (ArrayList<Integer>) values.clone();
    }

    @Override
    public void reSetValues(ArrayList<Integer> valuesToRestore) {
        if (apolloX != -1 && apolloY != -1 && opponentX != -1 && opponentY != -1 && moveNum == 1){  // if swap was done
            // it is necessary execute the swap undo as follows, because the default method is not able to to that
            // (the previous cell of Apollo is not free, but it is occupied by the opponent worker (toSwap))
            Worker opponent = GameBoard.getInstance().getCell(apolloX, apolloY).getWorker();
            Worker apollo = GameBoard.getInstance().getCell(opponentX, opponentY).getWorker();
            opponent.unSetPosition();
            apollo.setPosition(apolloX, apolloY);
            opponent.setPosition(opponentX, opponentY);
        }
        this.apolloX = valuesToRestore.get(0);
        this.apolloY = valuesToRestore.get(1);
        this.opponentX = valuesToRestore.get(2);
        this.opponentY = valuesToRestore.get(3);
        this.moveNum = valuesToRestore.get(4);
    }
}
