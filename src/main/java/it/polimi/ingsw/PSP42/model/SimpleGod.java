package it.polimi.ingsw.PSP42.model;

public abstract class SimpleGod {
    protected Worker w1;
    protected Worker w2;
    public SimpleGod(Worker w1,Worker w2){
        this.w1 = w1;
        this.w2= w2;
    }

    /**
     * It's a generic method for all type of card that checks if an action that can be a move, build ecc.. can be done
     * with the addition of power characteristics
     * @param x the x-axis of the cell interested for the action
     * @param y the y-axis of the cell interested for the action
     * @param w the worker that has the functionality added
     * @return true/false if possible
     */
    public abstract boolean powerAvailable(int x, int y,Worker w);

    /**
     * It's a generic method for all type of card that after the check of validation of the action
     * applies the right functionality, which can be a move, build ecc..
     * with the addition of power characteristics
     * @param x the x-axis of the cell interested for the action
     * @param y the y-axis of the cell interested for the action
     * @param w the worker that has the functionality added
     */
    public abstract void setPower(int x,int y, Worker w) throws InvalidBuildException;







}
