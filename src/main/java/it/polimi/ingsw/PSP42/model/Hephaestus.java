package it.polimi.ingsw.PSP42.model;

/**
 * TODO
 */
public class Hephaestus extends SimpleGod {

    private int counter = 1;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Hephaestus (Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"NULL"};
        String[] preMove = {"NULL"};
        String[] move = {"move"};
        String[] preBuild = {"build"};
        String[] build = {"build"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        return GameBoard.getInstance().moveAvailable(x, y, w);
    }

    @Override
    public boolean powerMove(int x, int y, Worker w) {
            //TODO
    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        return GameBoard.getInstance().buildAvailable(x, y, w);
    }

    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if(getCounter()==1) {
            w.buildBlock(x, y);
            //if player want to build again in the same place
            setCounter(2);
            w.getPlayer().build(x, y, level, w);
        }
        else {
            w.buildBlock(x, y);
            setCounter(1);
        }
    }

    @Override
    public boolean powerEffectAvailable() {
        return false;
    }

    @Override
    public boolean powerEffect() {
        return false;
    }

    @Override
    public String[][] getWhatToDo() {
        return phase;
    }
}