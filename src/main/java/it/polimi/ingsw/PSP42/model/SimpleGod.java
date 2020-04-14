package it.polimi.ingsw.PSP42.model;

public abstract class SimpleGod {

    boolean effectMove = false;
    Player effectPlayer = null;
    protected String[][] phase;
    protected Worker w1;
    protected Worker w2;

    public SimpleGod(Worker w1, Worker w2) {
        this.w1 = w1;
        this.w2 = w2;
        this.phase = setPhase();
    }

    public abstract String[][] setPhase();

    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (GameBoard.getInstance().moveAvailable(x, y, w))
            return true;
        return false;
    }

    public boolean powerMove(int x, int y, Worker w) {
        if (effectMove && ! effectPlayer.getCard().powerMoveAvailable(x, y, w))
            return false;
        if (powerMoveAvailable(x, y, w)) {
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (GameBoard.getInstance().buildAvailable(x, y, w))
            return true;
        return false;
    }

    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerBuildAvailable(x, y, level, w)) {
            w.buildBlock(x, y);
            return true;
        }
        return false;
    }

    public boolean powerEffectAvailable() {
        return false;
    }

    public boolean powerEffect() {
        return false;
    }

    public boolean powerInitialPosition(int x, int y, Worker w) {
        if (w.getCurrentX() == -1 && w.getCurrentY() == -1 &&   // if w is out of map and
            GameBoard.getInstance().getCell(x, y).getWorker() == null &&    // the initial cell is free and
            GameBoard.getInstance().getCell(x, y).getLevel() == 0) {        // the cell level is 0 (ground)
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    public String[][] getWhatToDo() {
        return phase;
    }
}