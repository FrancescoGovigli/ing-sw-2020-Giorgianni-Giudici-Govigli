package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

/**
 * Thanks this simple god a worker is able to move in a cell occupied by an opponent worker,
 * if the opponent worker can be pushed away in a free cell in same direction of the movement.
 */
public class Minotaur extends SimpleGod {

    private int minotaurX = -1;
    private int minotaurY = -1;
    private int opponentPrecedentX = -1;
    private int opponentPrecedentY = -1;
    private int moveNumber = 0;

    public Minotaur(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] START = {"EMPTY"};
        String[] PREMOVE = {"EMPTY"};
        String[] MOVE = {"MOVE"};
        String[] PREBUILD = {"EMPTY"};
        String[] BUILD = {"BUILD"};
        String[] END = {"EMPTY"};
        String[][] phase = {START, PREMOVE, MOVE, PREBUILD, BUILD, END};
        return phase;
    }

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
     * Used to move "minotaur" worker in a cell and to push opponent's worker away, if he is along the way.
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker
     * @return true if worker moved, false otherwise
     */
    @Override
    public boolean powerMove(int x, int y, Worker w) {
        for (Player player : playersWithEffect) {
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        }
        if (powerMoveAvailable(x, y, w)) {
            moveNumber = 1;
            Worker opponentWorker = GameBoard.getInstance().getCell(x, y).getWorker();
            if (opponentWorker != null) {
                minotaurX = w.getCurrentX();
                minotaurY = w.getCurrentY();
                opponentPrecedentX = x;
                opponentPrecedentY = y;
                int deltaX = x - w.getCurrentX();
                int deltaY = y - w.getCurrentY();
                int newOpponentX = x + deltaX;
                int newOpponentY = y + deltaY;
                Cell newOpponentCell = getNewOpponentCell(newOpponentX, newOpponentY);
                if (newOpponentCell != null &&
                        newOpponentCell.getWorker() == null &&
                            newOpponentCell.getLevel() != 4) {
                    opponentWorker.setPosition(newOpponentX, newOpponentY);
                    w.setPosition(x, y);
                    return true;
                }
            }
            else {
                w.setPosition(x, y);
                minotaurX = -1;
                minotaurY = -1;
                opponentPrecedentX = -1;
                opponentPrecedentY = -1;
                return true;
            }
        }
        return false;
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
        moveNumber = 0;
        if (powerBuildAvailable(x, y, level, w)) {
            w.buildBlock(x, y);
            return true;
        }
        return false;
    }

    /**
     * Used to know in what cells worker is able to move thanks to minotaur power.
     * @param x position on x-axis
     * @param y position on y-axis
     * @return array of cells where it's possible to move
     */
    public Cell[] adjacentCellMovePowerAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {   //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                                      // c cell isn't out of map and and
                        (c[i][j].getWorker() == null ||                         // (there is no worker in the cell or
                            c[i][j].getWorker().getPlayer() != GameBoard.getInstance().getCell(x, y).getWorker().getPlayer()) &&   // worker to be push away is not of the same player) and
                                (c[i][j].getLevel() != 4) &&                            // is not 4th level and
                                    ((c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() <= 1) &&    // one gap level on ascent and
                                        (c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() >= - 3))) {   // limit for the descent
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;
    }

    /**
     * Used to verify if a cell is in the game board
     * @param x position on x-axis of the cell
     * @param y position on x-axis of the cell
     * @return cell(x,y) if it is in the game board, null otherwise
     */
    public Cell getNewOpponentCell(int x, int y) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
            return GameBoard.getInstance().getCell(x, y);
        else
            return null;
    }

    @Override
    public ArrayList<Integer> getCurrentValues() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(minotaurX);
        values.add(minotaurY);
        values.add(opponentPrecedentX);
        values.add(opponentPrecedentY);
        values.add(moveNumber);
        return (ArrayList<Integer>) values.clone();
    }

    @Override
    public void reSetValues(ArrayList<Integer> valuesToRestore) {
        if (minotaurX != -1 && minotaurY != -1 && opponentPrecedentX != -1 && opponentPrecedentY != -1 && moveNumber == 1) {
            int deltaX = opponentPrecedentX - minotaurX;
            int deltaY = opponentPrecedentY - minotaurY;
            Worker opponent = GameBoard.getInstance().getCell(opponentPrecedentX + deltaX, opponentPrecedentY + deltaY).getWorker();
            opponent.setPosition(opponentPrecedentX, opponentPrecedentY);
        }
        this.minotaurX = valuesToRestore.get(0);
        this.minotaurY = valuesToRestore.get(1);
        this.opponentPrecedentX = valuesToRestore.get(2);
        this.opponentPrecedentY = valuesToRestore.get(3);
        this.moveNumber = valuesToRestore.get(4);
    }
}
