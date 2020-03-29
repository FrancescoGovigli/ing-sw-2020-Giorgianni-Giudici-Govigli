package it.polimi.ingsw2020.PSP042.model;

public class GameBoard {
    private static Cell[][] board;
    // Cell[][] board;
    Player player;
    int currentPlayer;
    public static GameBoard instance = null;

    /**
     * Constructor to initialize the board of dimension 5x5
      */
    private GameBoard() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                this.board[i][j] = new Cell();
        this.player = null;
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
    public static Cell getCell(int x, int y) {
        return board[x][y];
    }

    /**
     * Function to obtain the available cell around your position (x,y)
     * (it return an array which contains all the possible cell)
     * @param x
     * @param y
     * @return Cell[]
     */
    public static Cell[] adjacentCellAvailable(int x, int y) {
        Cell[] adjCell = {null, null, null, null, null, null, null};
        int index = 0;
        int startRow, stopRow;
        int startCol, stopCol;
        switch (x) {    //setting conditions 1st for
            case 0:
                startRow = x;
                stopRow = x + 1;
                break;
            case 4:
                startRow = x - 1;
                stopRow = x;
                break;
            default:
                startRow = x - 1;
                stopRow = x + 1;
                break;
        }
        switch (y) {    //setting conditions 2nd for
            case 0:
                startCol = y;
                stopCol = y + 1;
                break;
            case 4:
                startCol = y - 1;
                stopCol = y;
                break;
            default:
                startCol = y - 1;
                stopCol = y + 1;
                break;
        }
        for (int i = startRow ; i <= stopRow; i++) {    //search around the cell(x,y)
            for (int j = startCol; j <= stopCol; j++) {
                //if there isn't a worker and level is not 4 and 1 level gap
                if ((board[i][j].getWorker() == null) && (board[i][j].getLevel() != 4) &&
                        (1 == (board[i][j].getLevel() - board[x][y].getLevel()))){
                    adjCell[index] = board[i][j];
                    index++;
                }
            }
        }
        return adjCell;
    }
}