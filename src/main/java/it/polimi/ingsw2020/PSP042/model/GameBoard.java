package it.polimi.ingsw2020.PSP042.model;
import java.util.ArrayList;

public class GameBoard {
    private Cell[][] board = new Cell[5][5];
    private ArrayList<Player> players;
    private int currentPlayer;
    private static GameBoard instance = null;

    /**
     * Constructor to initialize the board of dimension 5x5
     */
    private GameBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++)
                this.board[i][j] = new Cell();
        }
        this.players = null;
        this.currentPlayer = 0;
    }

    /**
     * Method to return the unique instance of the board
     * @return instance
     */
    public static GameBoard getInstance() {
        if (instance == null)
            instance = new GameBoard();
        return instance;
    }

    /**
     * Method to return the object Cell specified by the Cartesian coordinates (x, y)
     * @param x
     * @param y
     * @return Cell
     */
    public Cell getCell(int x, int y) {
        return this.board[x][y];
    }

    /**
     * Method to obtain a sub-matrix with cells surrounded the specified cell
     * @param x (x coordinate of the specified cell)
     * @param y (y coordinate of the specified cell)
     * @return c[][] (sub-matrix with center in x, y)
     */
    public Cell[][] submatrixGenerator(int x, int y) {
        Cell[][] c = new Cell[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if ((y-1 == -1 && j == 0) || (y+1 == 5 && j == 2) ||
                    (x-1 == -1 && i == 0) || (x+1 == 5 && i == 2))
                    c[i][j] = null;
                else
                    c[i][j] = board[i][j];
        return c;
    }

    /**
     * Method to obtain the available cell around your position (x,y)
     * @param x (x coordinate of your position)
     * @param y (y coordinate of your position)
     * @return adjCellMoveAvailable[] (array which contains all the possible cell where move)
     */
    public Cell[] adjacentCellMoveAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if ((c[i][j] == null || c[i][j].getWorker() == null) &&     // c cell is out of map or there isn't a worker
                    (c[i][j].getLevel() != 4) &&                            // is not 4th level
                    (c[i][j].getLevel() - board[x][y].getLevel()) <= 1 ||   // one gap level on ascent
                    (c[i][j].getLevel() - board[x][y].getLevel()) >= - 3)   // limit for the descent
                {
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;
    }

    /**
     * Used to know if worker w can be moved in (x,y) position
     * @param x (x coordinate of your position)
     * @param y (y coordinate of your position)
     * @param w (your worker)
     * @return true if worker can be moved, false otherwise
     */
    public boolean moveAvailable(int x, int y, Worker w) {
        boolean condition = false;
        Cell[] c = adjacentCellMoveAvailable(w.getCurrentX(),w.getCurrentY());
        for(int i=0; i < c.length; i++)
            if(c[i] != null && c[i].equals(getCell(x,y)))
                condition = true;
        return condition;
    }

    /**
     * Method to obtain a list of cell where the worker can build
     * @param x (x coordinate of your position)
     * @param y (y coordinate of your position)
     * @return adjCellBuildAvailable[] (array which contains all the possible cell where build)
     */
    public Cell[] adjacentCellBuildAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellBuildAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where build
        Cell[][] c = submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if ((c[i][j] == null || c[i][j].getWorker() == null) &&     // c cell is out of map or there isn't a worker
                    (c[i][j].getLevel() != 4))                              // is not 4th level
                {
                    adjCellBuildAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellBuildAvailable;
    }

    /**
     * Used to check if it's possible build in (x,y) position with worker w
     * @param x
     * @param y
     * @param w worker
     * @return true if worker can build, false otherwise
     */
    public boolean buildAvailable(int x, int y, Worker w) {
        boolean condition = false;
        Cell[] c = adjacentCellBuildAvailable(w.getCurrentX(), w.getCurrentY());
        for(int i = 0; i < c.length; i++)
            if(c[i] != null && c[i].equals(getCell(x,y)))
                condition = true;
        return condition;
    }

    /**
     * Used to know which workers are available
     * @param w worker
     * @return true if worker can be used, false otherwise
     */
    public boolean workerAvailable(Worker w) {
        Cell[] c = adjacentCellMoveAvailable(w.getCurrentX(), w.getCurrentY());
        if(c[0] == null)
            w.setAvailable(false);
        else
            w.setAvailable(true);
        return w.getAvailable();
    }
}