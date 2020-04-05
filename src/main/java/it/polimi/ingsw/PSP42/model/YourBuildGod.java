package it.polimi.ingsw.PSP42.model;

public abstract class YourBuildGod extends SimpleGod {

    public YourBuildGod(Worker w1, Worker w2) {
        super(w1, w2);
    }

    /**
     * Used to know if it's possible to build in one position thanks to god's power.
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param w worker that want to build
     * @return true if it's possible to use build's power, false otherwise
     */
    public abstract boolean powerBuildAvailable(int x,int y,Worker w);

    /**
     * Used to build in one position thanks to god's power.
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param w worker that want to build
     */
    public abstract void buildPower(int x, int y,Worker w);

    /**

     /**
     * Method re-implemented by "powerBuildAvailable".
     * @param x the x-axis of the cell interested for the action
     * @param y the y-axis of the cell interested for the action
     * @param w the worker that has the functionality added
     */
    @Override
    public boolean powerAvailable(int x, int y,Worker w) {
        return this.powerBuildAvailable(x,y,w);
    }

    /**
     * Method re-implemented by "buildPower".
     * @param x the x-axis of the cell interested for the action
     * @param y the y-axis of the cell interested for the action
     * @param w the worker that has the functionality added
     */
    @Override
    public void setPower(int x, int y,Worker w) {
        this.buildPower(x,y,w);
    }
}
