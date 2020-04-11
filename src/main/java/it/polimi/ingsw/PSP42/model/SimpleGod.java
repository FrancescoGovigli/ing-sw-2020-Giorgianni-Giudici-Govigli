package it.polimi.ingsw.PSP42.model;

public abstract class SimpleGod {

    boolean levelUp = true;
    protected String[][] phase;
    protected Worker w1;
    protected Worker w2;

    public SimpleGod(Worker w1,Worker w2){
        this.w1 = w1;
        this.w2= w2;
        this.phase = setPhase();
    }

    public abstract String[][] setPhase();

    public abstract boolean powerMoveAvailable(int x,int y, Worker w);

    public abstract boolean powerMove(int x, int y, Worker w);

    public abstract boolean powerBuildAvailable(int x, int y, int level, Worker w);

    public abstract boolean powerBuild(int x, int y, int level, Worker w);

    public abstract boolean powerEffectAvailable();

    public abstract boolean powerEffect();

    public abstract String[][] getWhatToDo();
}