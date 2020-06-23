package it.polimi.ingsw.PSP42.model;

/**
 * Simple god that allows one worker to build a dome even if the level of the cell isn't 3.
 */
public class Atlas extends SimpleGod {

    public Atlas(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] START = {"EMPTY"};
        String[] PREMOVE = {"EMPTY"};
        String[] MOVE = {"MOVE"};
        String[] PREBUILD = {"EMPTY"};
        String[] BUILD = {"BUILD"};
        String[] END = {"EMPTY"};
        String[][] phase = {START, PREMOVE, MOVE, PREBUILD, BUILD, END};
        return phase;
    }

    /**
     * Used to build a dome (cell level 4) or next level in a cell of the game board.
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker who build thanks Atlas' power
     */
    @Override
    public boolean powerBuild(int x, int y, int level, Worker w) {
        if (powerBuildAvailable(x, y, level, w)) {
            if (level == 4)
                GameBoard.getInstance().getCell(x, y).setSpecificCellLevel(level);
            else
                GameBoard.getInstance().getCell(x, y).setCellLevel();
            return true;
        }
        return false;
    }
}
