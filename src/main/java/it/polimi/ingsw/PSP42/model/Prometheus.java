package it.polimi.ingsw.PSP42.model;

/**
 * Simple God who can build before moving if he does not move up a level while moving
 */
public class Prometheus extends SimpleGod{

    private int buildNum = 0;
    private int moveNum = 0;

    public Prometheus(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"EMPTY"};
        String[] preMove = {"BUILD"};
        String[] move = {"MOVE"};
        String[] preBuild = {"EMPTY"};
        String[] build = {"BUILD"};
        String[] end = {"EMPTY"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    /**
     * Method used to move the worker in cell (x,y) and checking if he building before
     * @param x (x coordinate of where you would like to go)
     * @param y (y coordinate of where you would like to go)
     * @param w (worker who would like to move)
     * @return true if worker can be moved, false otherwise
     */
    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (buildNum == 0 && GameBoard.getInstance().moveAvailable(x, y, w))    // if building pre-move was not done
            return true;
        if (buildNum != 0 &&    // if building pre-move was done &&
            (GameBoard.getInstance().getCell(x, y).getLevel() ==    // (the level of the future cell is the same as the current one
             GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getLevel()) &&   // as the current one) &&
            GameBoard.getInstance().moveAvailable(x, y, w)) // the future cell is available
            return true;
        return false;
    }

    /**
     * Method used to move the worker in cell (x,y)
     * @param x (x coordinate of where you would like to go)
     * @param y (y coordinate of where you would like to go)
     * @param w (worker who would like to move)
     * @return true if worker can be moved, false otherwise
     */
    @Override
    public boolean powerMove(int x, int y, Worker w) {
        for (Player effectPlayer : effectPlayers) {
            if (effectPlayer != null && !effectPlayer.getCard().powerMoveAvailable(x, y, w))
                return false;
        }
        if (powerMoveAvailable(x, y, w)) {
            w.setPosition(x, y);
            buildNum = 0;
            moveNum = 1;
            return true;
        }
        return false;
    }

    /**
     * Method to check if it's possible build in a position and counting the building times in a turn
     * @param x (x coordinate of where you would like to build)
     * @param y (y coordinate of where you would like to build)
     * @param w (worker who would like to build)
     * @return true if worker can build, false otherwise
     */
    @Override
    public boolean powerBuildAvailable(int x, int y, int level, Worker w) {
        if (GameBoard.getInstance().buildAvailable(x, y, w)) {
            if (moveNum == 0)
                buildNum = 1;
            if (moveNum == 1) {
                moveNum = 0;
            }
            return true;
        }
        return false;
    }
}