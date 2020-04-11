package it.polimi.ingsw.PSP42.model;

/**
 * TODO
 */
public class Minotaur extends SimpleGod{

    public Minotaur(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"NULL"};
        String[] preMove = {"NULL"};
        String[] move = {"move"};
        String[] preBuild = {"NULL"};
        String[] build = {"build"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        return false;
    }

    @Override
    public boolean powerMove(int x, int y, Worker w) {
        return false;
    }

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        return false;
    }

    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        return false;
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
