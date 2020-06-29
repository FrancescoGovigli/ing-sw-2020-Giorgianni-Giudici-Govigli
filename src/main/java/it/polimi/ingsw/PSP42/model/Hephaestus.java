package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

/**
 * This god allows a worker to build twice in the same position in one turn.
 */
public class Hephaestus extends SimpleGod {

    private Cell precedentCell;
    private int buildNum = 0;

    public Hephaestus (Worker w1, Worker w2) {
        super(w1, w2);
    }

    /**
     * Set standard actions in phase + first build in gamePhase "PREBUILD" and second in "BUILD".
     * @return Game phase for Hephaestus as an array of Strings' arrays
     */
    @Override
    public String[][] setPhase() {
        String[] START = {"EMPTY"};
        String[] PREMOVE = {"EMPTY"};
        String[] MOVE = {"MOVE"};
        String[] PREBUILD = {"BUILD"};
        String[] BUILD = {"BUILD"};
        String[] END = {"EMPTY"};
        String[][] phase = {START, PREMOVE, MOVE, PREBUILD, BUILD, END};
        return phase;
    }

    /**
     * Used to set and keep in memory which was the precedent cell where worker has built.
     * @param precedentCell first Cell where worker has build
     */
    public void setPrecedentCell(Cell precedentCell) {
        this.precedentCell = precedentCell;
    }

    public Cell getPrecedentCell() {
        return precedentCell;
    }

    /**
     * Standard move with a reset function of attribute "buildNum" and "precedentCell" used in "powerBuild".
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker who wants to move
     * @return true if worker moved, false otherwise
     */
    public boolean powerMove(int x, int y, Worker w) {
        for (Player player : playersWithEffect)
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        if (powerMoveAvailable(x, y, w)) {
            setPrecedentCell(null);
            buildNum = 0;
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    /**
     * Used to verify if it's possible to build in that position.
     * In first place as a standard "buildAvailable",
     * second time ensuring that second build is in same place of before.
     * @param x position on the x-axis (if method called twice this parameter changed)
     * @param y position on the y-axis (if method called twice this parameter changed)
     * @param w worker that want to build
     * @return true if worker can build, false otherwise
     */
    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (buildNum == 0 && GameBoard.getInstance().buildAvailable(x, y, w))
            return true;
        if (buildNum == 1 &&
                GameBoard.getInstance().buildAvailable(x, y, w) &&
                    GameBoard.getInstance().getCell(x, y).equals(getPrecedentCell()) &&
                        GameBoard.getInstance().getCell(x, y).getLevel() != 3)
            return true;
        return false;
    }

    /**
     * Used to build once or twice in (x,y).
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker
     * @return true if worker builds, false otherwise
     */
    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (buildNum == 0 && powerBuildAvailable(x, y, level, w)) {
            w.buildBlock(x, y);
            buildNum = 1;
            setPrecedentCell(GameBoard.getInstance().getCell(x, y));
            return true;
        }
        if (buildNum == 1 && powerBuildAvailable(x, y, level, w)) {
            w.buildBlock(x, y);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Integer> getCurrentValues() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(buildNum);
        return (ArrayList<Integer>) values.clone();
    }

    @Override
    public void reSetValues(ArrayList<Integer> valuesToRestore) {
        this.buildNum = valuesToRestore.get(0);
    }
}
