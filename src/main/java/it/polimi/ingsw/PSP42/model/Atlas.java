package it.polimi.ingsw.PSP42.model;

/**
 * Simple god that allowed one worker to build a dome even if the level of the cell isn't 3.
 */
public class Atlas extends SimpleGod {

    public Atlas(Worker w1, Worker w2) {
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
        return GameBoard.getInstance().moveAvailable(x, y, w);
    }

    @Override
    public boolean powerMove(int x, int y, Worker w) {
        w.setPosition(x, y);
    }

    /**
     * Used to know if it's possible to build thanks Atlas' power in one position.
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker used to build
     * @return true if worker can build in that position, false otherwise
     */
    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        return GameBoard.getInstance().buildAvailable(x, y, w);
    }

    /**
     * Used to build a dome (cell level 4) in a cell of the game board.
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker who build thanks Atlas' power
     */
    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        /*
        ControllerCLI con = new ControllerCLI();
        String c = con.whatLevel();   // method in controller that ask at the player what level want to build
        */
        GameBoard.getInstance().getCell(x, y).setSpecificCellLevel(level);
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