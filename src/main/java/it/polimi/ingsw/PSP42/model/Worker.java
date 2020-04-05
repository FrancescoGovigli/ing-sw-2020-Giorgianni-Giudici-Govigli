package it.polimi.ingsw.PSP42.model;

/**
 * @author Luca Giudici
 */
public class Worker {

    private int currentX;
    private int currentY;
    private Player player;
    private boolean available;

    /**
     * Set Cartesian position, reference to Player and declare that the worker can be moved.
     * Used in Player's constructor.
     * @see Player
     * @param player reference to Player that instantiate two Workers
     */
    public Worker(int x, int y, Player player) {
        this.currentX = x;
        this.currentY = y;
        this.player = player;
        this.available = true;
    }

    /**
     * Used to obtain worker's position on x-axis.
     * @return current position on x-axis
     */
    public int getCurrentX() {
        return this.currentX;
    }

    /**
     * Used to obtain worker's position on y-axis.
     * @return current position on y-axis
     */
    public int getCurrentY() {
        return this.currentY;
    }

    /**
     * Set Cartesian position. Used in Player's method: "setPositionWorker".
     * @see Player
     * @param x position on x-axis
     * @param y position on y-axis
     */
    public void setPosition(int x, int y) {
        if(this.currentX != -1 && this.currentY != -1)
            GameBoard.getInstance().getCell(this.currentX, this.currentY).unSetWorker();
        this.currentX = x;
        this.currentY = y;
        GameBoard.getInstance().getCell(x, y).setWorker(this);
    }

    /**
     * Method to know which player used this worker.
     * @return worker's player reference
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Used to know if worker can be moved.
     * @return true if worker can be moved, false otherwise
     */
    public boolean getAvailable() {
        return this.available;
    }

    /**
     * Used to set if worker is able to move.
     * @param available true if worker is able to move, false otherwise
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Used by Player to decide where to build.
     * @see Player
     * @param x block's position on x-axis
     * @param y block's position on y-axis
     */
    public void buildBlock(int x, int y) {
        GameBoard.getInstance().getCell(x, y).setCellLevel();
    }
}