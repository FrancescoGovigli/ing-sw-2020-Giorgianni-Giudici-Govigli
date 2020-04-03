package it.polimi.ingsw.PSP42.model;

public abstract class YourMoveGod extends SimpleGod {

    public YourMoveGod(Worker w1,Worker w2){
        super(w1,w2);
    }

    public abstract boolean powerMoveAvailable(int x,int y,Worker w);
    public abstract void setPowerPosition(int x, int y,Worker w);

    @Override
    public boolean powerAvailable(int x, int y,Worker w) {
        return this.powerMoveAvailable(x,y,w);
    }

    @Override
    public void setPower(int x, int y,Worker w) {
        this.setPowerPosition(x,y,w);
    }
}
