package it.polimi.ingsw.PSP42.model;

public abstract class YourMoveGod extends SimpleGod {

    public YourMoveGod(Worker w1, Worker w2){
        super(w1, w2);
    }

    /**
     * Used to know if it's possible to move in a position thanks to god's power.
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker that want to move
     * @return true if worker is able to move thanks god's power, false otherwise
     */
    public abstract boolean powerMoveAvailable(int x, int y, Worker w);

    /**
     * Used to set worker's position thanks god's power
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker that want to move
     */
    public abstract void setPowerPosition(int x, int y, Worker w);

    /**
     * Method re-implemented by "powerMoveAvailable".
     * @param x the x-axis of the cell interested for the action
     * @param y the y-axis of the cell interested for the action
     * @param w the worker that has the functionality added
     */
    @Override
    public boolean powerAvailable(int x, int y, Worker w) {
        return this.powerMoveAvailable(x, y, w);
    }

    /**
     * Method re-implemented by "setPowerPosition".
     * @param x the x-axis of the cell interested for the action
     * @param y the y-axis of the cell interested for the action
     * @param w the worker that has the functionality added
     */
    @Override
    public void setPower(int x, int y, Worker w) {
        this.setPowerPosition(x, y, w);
    }
}