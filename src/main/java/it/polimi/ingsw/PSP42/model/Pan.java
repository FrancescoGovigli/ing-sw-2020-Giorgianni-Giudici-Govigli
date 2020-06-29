package it.polimi.ingsw.PSP42.model;

/**
 * Simple god that wins if it drops by at least 2 levels.
 */
public class Pan extends SimpleGod {

    public Pan(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"EMPTY"};
        String[] preMove = {"EMPTY"};
        String[] move = {"MOVE"};
        String[] preBuild = {"EMPTY"};
        String[] build = {"BUILD"};
        String[] end = {"EMPTY"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }

    /**
     * Method used to move the worker in cell (x,y) and checking if he wins doing this move.
     * @param x (x coordinate of where you would like to go)
     * @param y (y coordinate of where you would like to go)
     * @param w (worker who would like to move)
     * @return true if worker can be moved, false otherwise
     */
    @Override
    public boolean powerMove(int x, int y, Worker w) {
        for (Player player : playersWithEffect) {
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        }
        if (powerMoveAvailable(x, y, w)) {
            if (GameBoard.getInstance().getCell(x, y).getLevel() -  //where he wants to go minus
                GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getLevel() <= -2)    //where is the worker
                w.getPlayer().setPlayerState("WIN");
            w.setPosition(x, y);
            return true;
        }
        return false;
    }
}
