package it.polimi.ingsw2020.PSP042.model;
import java.util.ArrayList;

public class GameBoard {
    private Cell[][] board;
    private ArrayList<Player> players;
    private int currentPlayer;
    public static GameBoard instance = null;

    /**
     * Constructor to initialize the board of dimension 5x5
     */
    private GameBoard() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                this.board[i][j] = new Cell();
        this.players = null;
        this.currentPlayer = 0;
    }

    /**
     * Function to return the unique instance of the board
     * @return instance
     */
    public static GameBoard getInstance() {
        if (instance == null)
            instance = new GameBoard();
        return instance;
    }

    /**
     * Function to return the object Cell specified by the Cartesian coordinates (x, y)
     * @param x
     * @param y
     * @return Cell
     */
    public Cell getCell(int x, int y) {
        return this.board[x][y];
    }

    /**
     * Auxiliary method to calculate the correct Row and Column index for adjacentCell... methods for Cell(x,y)
     * @param x
     * @param y
     * @return an array which position are:
     * a[0] is startRow, a[1] is stopRow, a[2] is startCol, a[3] is stopCol
     */
    private int[] setIndexRowCol(int x, int y) {
        int[] a = {0,0,0,0};
        switch (x) {    //setting conditions 1st for
            case 0:     //first row
                a[0] = x;
                a[1] = x + 1;
                break;
            case 4:     //last row
                a[0] = x - 1;
                a[1] = x;
                break;
            default:
                a[0] = x - 1;
                a[1] = x + 1;
                break;
        }
        switch (y) {    //setting conditions 2nd for
            case 0:     //first column
                a[2] = y;
                a[3] = y + 1;
                break;
            case 4:     //last column
                a[2] = y - 1;
                a[3] = y;
                break;
            default:
                a[2] = y - 1;
                a[3] = y + 1;
                break;
        }
        return a;
    }

    /**
     * Function to obtain the available cell around your position (x,y)
     * (it return an array which contains all the possible cell)
     * @param x
     * @param y
     * @return Cell[]
     */
    public Cell[] adjacentCellMoveAvailable(int x, int y) {
        Cell[] adjCell = {null, null, null, null, null, null, null, null};
        int index = 0;
        int[] a = setIndexRowCol(x, y);
        for (int i = a[0] ; i <= a[1]; i++) {    //search around the cell(x,y)
            for (int j = a[2]; j <= a[3]; j++) {
                //if there isn't a worker and level is not 4 and 1 level gap
                if ((board[i][j].getWorker() == null) && (board[i][j].getLevel() != 4) &&
                        (1 <= (board[i][j].getLevel() - board[x][y].getLevel()))){
                    adjCell[index] = board[i][j];
                    index++;
                }
            }
        }
        return adjCell;
    }

    /**
     * Used to know if worker w can be moved in (x,y) position
     * @param x
     * @param y
     * @param w
     * @return true if worker can be moved, false otherwise
     */
    public boolean moveAvailable(int x, int y, Worker w) {
        boolean condition = false;
        Cell[] c = adjacentCellMoveAvailable(w.getCurrentX(),w.getCurrentY());
        for(int i=0; i < c.length; i++)
            if(c[i] == getCell(x,y))
                condition = true;
        return condition;
    }

    /**
     * Used to obtain a list of cell where the worker can build
     * @param x
     * @param y
     * @return
     */
    public Cell[] adjacentCellBuildAvailable(int x, int y) {
        Cell[] adjCell = {null, null, null, null, null, null, null, null};
        int index = 0;
        int[] a = setIndexRowCol(x, y);
        for (int i = a[0]; i <= a[1]; i++) {    //search around the cell(x,y)
            for (int j = a[2]; j <= a[3]; j++) {
                //if there isn't a worker and level is not 4
                if ((board[i][j].getWorker() == null) && (board[i][j].getLevel() != 4)) {
                    adjCell[index] = board[i][j];
                    index++;
                }
            }
        }
        return adjCell;
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
            if(c[i] == getCell(x,y))
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