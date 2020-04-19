package it.polimi.ingsw.PSP42.model;

/**
 * This god allowed a worker to build twice in the same position
 */
public class Hephaestus extends SimpleGod {

    private Cell precedentCell;
    private int counter = 1;

    public Cell getPrecedentCell() {
        return precedentCell;
    }

    /**
     * Used to set and keep in memory which was the precedent cell.
     * @param precedentCell first Cell where worker has build
     */
    public void setPrecedentCell(Cell precedentCell) {
        this.precedentCell = precedentCell;
    }

    public int getCounter() {
        return counter;
    }

    /**
     * Used to keep count how much time worker wants to build.
     * @param counter is 1 or 2
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Hephaestus (Worker w1, Worker w2) {
        super(w1, w2);
    }

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

    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (getCounter() == 1) {
            if (GameBoard.getInstance().buildAvailable(x, y, w)) {
                this.setPrecedentCell(GameBoard.getInstance().getCell(x, y));
                return true;
            }
            else
                return false;
        }
        else
            return this.powerSecondBuildAvailable(x, y);
    }

    /**
     * Used to build once or twice in (x,y).
     * @param x position on x-axis
     * @param y position on y-axis
     * @param level
     * @param w worker
     * @return true if worker builds, false otherwise
     */
    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerBuildAvailable(x, y, level, w)) {
            if(getCounter() == 1) {
                w.buildBlock(x, y);
                setCounter(2);
                return true;
            } else {
                w.buildBlock(x, y);
                setCounter(1);
                setPrecedentCell(null);
                return true;
            }
        }
        else {
            setCounter(1);
            setPrecedentCell(null);
            return false;
        }
    }

    /**
     * Used to know if second building is in the same place of first.
     * @param x position on x-axis
     * @param y position on y-axis
     * @return true if second building is in the same place of before, false otherwise
     */
    public boolean powerSecondBuildAvailable(int x, int y) {
        if(GameBoard.getInstance().getCell(x, y).equals(getPrecedentCell()))
            return true;
        return false;
    }
}