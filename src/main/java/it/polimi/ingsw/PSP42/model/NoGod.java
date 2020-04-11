package it.polimi.ingsw.PSP42.model;

public class NoGod extends SimpleGod{

    public NoGod(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public void godHashMap() {

    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        return GameBoard.getInstance().moveAvailable(x, y, w);
    }

    @Override
    public void powerMove(int x, int y, Worker w) {
        w.setPosition(x, y);
    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        return GameBoard.getInstance().buildAvailable(x, y, w);
    }

    @Override
    public void powerBuild(int x, int y, int level, Worker w) {
        w.buildBlock(x, y);
    }

    @Override
    public boolean powerEffectAvailable() {
        return false;
    }

    @Override
    public void powerEffect() {

    }
}
