package it.polimi.ingsw.PSP42.model;
import it.polimi.ingsw.PSP42.*;

import java.util.ArrayList;

public class GameBoard implements ModelObservable {
    private Cell[][] board = new Cell[5][5];
    private ArrayList<ModelObserver> obs = new ArrayList<ModelObserver>();
    private ArrayList<Player> players;
    private int currentPlayer = 0;
    private static GameBoard instance = null;
    /*TODO added Status*/ private Turn gamePhase = Turn.valueOf("START");

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

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void reset(){
        instance=null;
    }

    public void setCurrentPlayer(int x){
        currentPlayer=x;
    }

    /**
     * Used to set only one time all the players playing when initializing the game
     * @param players
     */
    public void setPlayers(ArrayList<Player> players) {
        if(this.players ==null)
         this.players = players;
    }

    /**
     *
     * @return
     */
    public ArrayList<Player> getPlayers(){
        return (ArrayList<Player>) players.clone();
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
     * @return c (sub-matrix with center in x, y)
     */
    public Cell[][] submatrixGenerator(int x, int y) {
        Cell[][] c = new Cell[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if ((y-1 == -1 && j == 0) || (y+1 == 5 && j == 2) ||
                    (x-1 == -1 && i == 0) || (x+1 == 5 && i == 2))
                    c[i][j] = null;
                else
                    c[i][j] = board[x-1+i][y-1+j];
        return c;
    }

    /**
     * Method to obtain the available cell around your position (x,y)
     * @param x (x coordinate of your position)
     * @param y (y coordinate of your position)
     * @return adjCellMoveAvailable (array which contains all the possible cell where move)
     */
    public Cell[] adjacentCellMoveAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                                          // c cell isn't out of map and
                    (c[i][j].getWorker() == null) &&                            // there isn't a worker and
                    (c[i][j].getLevel() != 4) &&                                // is not 4th level and
                    ((c[i][j].getLevel() - board[x][y].getLevel() <= 1) &&      // one gap level on ascent and
                    (c[i][j].getLevel() - board[x][y].getLevel() >= - 3)))      // limit for the descent
                {
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;
    }

    /**
     * Method to know if a worker can be moved in (x,y) position
     * @param x (x coordinate of where you would like to go)
     * @param y (y coordinate of where you would like to go)
     * @param w (worker who would like to move)
     * @return true if worker can be moved, false otherwise
     */
    public boolean moveAvailable(int x, int y, Worker w) {
        boolean condition = false;
        Cell[] c = adjacentCellMoveAvailable(w.getCurrentX(),w.getCurrentY());
        for (int i = 0; i < c.length && !condition; i++) // !condition used to stop for loop just condition becomes true
            if (c[i] != null && c[i].equals(getCell(x,y))) {
                condition = true;
                winCondition(x, y, w);
            }
        return condition;
    }

    /**
     * Method to obtain a list of cell where the worker can build
     * @param x (x coordinate of your position)
     * @param y (y coordinate of your position)
     * @return adjCellBuildAvailable (array which contains all the possible cell where build)
     */
    public Cell[] adjacentCellBuildAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellBuildAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where build
        Cell[][] c = submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                  // c cell isn't out of map and
                    (c[i][j].getWorker() == null) &&    // there isn't a worker and
                    (c[i][j].getLevel() != 4))          // is not 4th level
                {
                    adjCellBuildAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellBuildAvailable;
    }

    /**
     * Method to check if it's possible build in a position
     * @param x (x coordinate of where you would like to build)
     * @param y (y coordinate of where you would like to build)
     * @param w (worker who would like to build)
     * @return true if worker can build, false otherwise
     */
    public boolean buildAvailable(int x, int y, Worker w) {
        boolean condition = false;
        Cell[] c = adjacentCellBuildAvailable(w.getCurrentX(), w.getCurrentY());
        for (int i = 0; i < c.length && !condition; i++) // !condition used to stop for loop just condition becomes true
            if (c[i] != null && c[i].equals(getCell(x,y)))
                condition = true;
        return condition;
    }

    /**
     * Method to know which workers are available
     * @param w (worker to be verified)
     * @return true if worker can be used, false otherwise
     */
    public boolean workerAvailable(Worker w) {
        Cell[] c = adjacentCellMoveAvailable(w.getCurrentX(), w.getCurrentY());
        if (c[0] == null)
            w.setAvailable(false);
        else
            w.setAvailable(true);
        return w.getAvailable();
    }

    //TODO check if it's correct with the new implementation
    /**
     * Method to verify if the player are still in the game or not,
     * if both of his workers are not available the player's status will be changed to "LOSE"
     * @param p (player to be verified)
     */
    public void loseCondition(Player p, String phase) {
        switch (phase) {
            case "START": {
                if (!p.getWorker1().getAvailable() && !p.getWorker2().getAvailable()) {
                    p.getWorker1().unSetPosition();
                    p.getWorker2().unSetPosition();
                    p.setPlayerState("LOSE");
                }
                break;
            }
            case "PREMOVE": {
                Cell[] moveCells1 = GameBoard.getInstance().adjacentCellMoveAvailable(p.getWorker1().getCurrentX(), p.getWorker1().getCurrentY());
                Cell[] moveCells2 = GameBoard.getInstance().adjacentCellMoveAvailable(p.getWorker2().getCurrentX(), p.getWorker2().getCurrentY());
                if (moveCells1[0] == null && moveCells2[0] == null) {
                    p.getWorker1().unSetPosition();
                    p.getWorker2().unSetPosition();
                    p.setPlayerState("LOSE");
                }
                break;
            }
            case "PREBUILD": {
                Cell[] buildCells1 = GameBoard.getInstance().adjacentCellBuildAvailable(p.getWorker1().getCurrentX(), p.getWorker1().getCurrentY());
                Cell[] buildCells2 = GameBoard.getInstance().adjacentCellBuildAvailable(p.getWorker2().getCurrentX(), p.getWorker2().getCurrentY());
                if (buildCells1[0] == null && buildCells2[0] == null) {
                    p.getWorker1().unSetPosition();
                    p.getWorker2().unSetPosition();
                    p.setPlayerState("LOSE");
                }
                break;
            }
        }
    }

    /**
     * Method to verify if the worker who has moved has won
     * @param x (x coordinate of the future position of the worker)
     * @param y (y coordinate of the future position of the worker)
     * @param w (worker on moving)
     */
    public void winCondition(int x, int y, Worker w){
        if (getCell(w.getCurrentX(), w.getCurrentY()).getLevel() == 2 &&    // worker w on level 2
            getCell(x, y).getLevel() == 3) {                                // next position on level 3
            w.getPlayer().setPlayerState("WIN");
        }
    }


    //PATTER OBSERVER

    /**
     * Add an observer to the Model's observer list
     * @param ob
     */
    @Override
    public void attach(ModelObserver ob) {
        obs.add(ob);
    }

    /**
     * Removes an observer to the Model's observer list
     * @param ob
     */
    @Override
    public void detach(ModelObserver ob) {
      obs.remove(ob);
    }

    /**
     * Notifies the observers who need update
     */
    @Override
    public void notifyObservers(Object o) {
        for (int i = 0; i <obs.size(); i++) {
            obs.get(i).updateBoard(gamePhase.toString());
        }
    }
}