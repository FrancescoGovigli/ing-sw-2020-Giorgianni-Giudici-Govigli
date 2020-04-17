package it.polimi.ingsw.PSP42.model;

/**
 * Thanks to this simple god if a player's worker, who have this god, step up, the workers of other players can't.
 */
public class Athena extends SimpleGod {

    private int counter = 0;
    private boolean blockOpponentsStepUp = false;

    public Athena(Worker w1, Worker w2) {
        super(w1, w2);
    }

    /**
     * Used to know in what phase we used effect.
     * @return 0 = Start, or 1 = End
     */
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Used to know if we used effect in this turn
     * @return true if Athena'll block opponents step up, false otherwise
     */
    public boolean getBlockOpponentsStepUp() {
        return blockOpponentsStepUp;
    }

    public void setBlockOpponentsStepUp(boolean blockOpponentsStepUp) {
        this.blockOpponentsStepUp = blockOpponentsStepUp;
    }

    @Override
    public String[][] setPhase() {
        String[] START = {"EFFECT"};
        String[] PREMOVE = {"NULL"};
        String[] MOVE = {"MOVE"};
        String[] PREBUILD = {"NULL"};
        String[] BUILD = {"BUILD"};
        String[] END = {"EFFECT"};
        String[][] phase = {START, PREMOVE, MOVE, PREBUILD, BUILD, END};
        return phase;
    }

    /**
     * Used by Athena as standard moveAvailable method.
     * Used by opponents' workers, if they can't step up, to know if they can move in that position.
     * @param x x-axis position
     * @param y y-axis position
     * @param w worker who want to move
     * @return true if worker's able to move, false otherwise
     */
    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if(getBlockOpponentsStepUp())
            return powerMoveBlockedStepUpAvailable(x, y, w);
        return GameBoard.getInstance().moveAvailable(x, y, w);
    }

    /**
     * Used to move worker and to set BlockOpponentsStepUp if necessary
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker
     * @return true if worker was moved, false otherwise
     */
    @Override
    public boolean powerMove(int x, int y, Worker w) {
        if(effectMove && !effectPlayer.getCard().powerMoveAvailable(x, y, w))
            return false;
        if(powerMoveAvailable(x, y, w)) {
            if (workerStepUp(x, y, w))
                setBlockOpponentsStepUp(true);
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    /**
     * Check if power was activated.
     * @return true if other players were blocked, false otherwise
     */
    @Override
    public boolean powerEffectAvailable() {
        return getBlockOpponentsStepUp();
    }

    /**
     * If, at first iteration (counter = 0 = START), effect was applied in precedent turn set levelUp true for other players.
     * Else if, at second iteration (counter = 1 = END), effect was applied in this turn set levelUp false for other players.
     * @return true if the method was been used, false otherwise
     */
    @Override
    public boolean powerEffect() {
        if (getCounter()==0) {
            if(powerEffectAvailable()) {
                for (Player player : GameBoard.getInstance().getPlayers()) {
                    player.getCard().effectMove = false;
                    player.getCard().effectPlayer = null;
                }
                setCounter(1);
                setBlockOpponentsStepUp(false);
                return true;
            } else {
                setCounter(1);
                return false;
            }
        } else {
            if (powerEffectAvailable()) {
                for (Player player : GameBoard.getInstance().getPlayers()) {
                    player.getCard().effectMove = true;
                    player.getCard().effectPlayer = this.w1.getPlayer();
                }
                setCounter(0);
                return true;
            } else {
                setCounter(0);
                return false;
            }
        }
    }

    /**
     * Check if worker, during his movement, step up.
     * @param x position on x-axis where worker is going
     * @param y position on y-axis where worker is going
     * @param w worker
     * @return true if worker step up, false otherwise
     */
    public boolean workerStepUp(int x, int y, Worker w) {
        if (GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getLevel() -
                GameBoard.getInstance().getCell(x, y).getLevel() == -1)
            return true;
        else
            return false;
    }

    /**
     * Used to know if Worker w can move in that position without step up.
     * @param x where worker wants to move on x-axis
     * @param y where worker wants to move on y-axis
     * @param w worker who wants to move
     * @return true if worker can, false otherwise
     */
    public boolean powerMoveBlockedStepUpAvailable(int x, int y, Worker w) {
        boolean condition = false;
        Cell[] c = adjacentCellBlockedStepUpMoveAvailable(w.getCurrentX(),w.getCurrentY());
        for (int i = 0; i < c.length && !condition; i++) // !condition used to stop for loop just condition becomes true
            if (c[i] != null && c[i].equals(GameBoard.getInstance().getCell(x,y))) {
                condition = true;
            }
        return condition;
    }

    /**
     * Used to know in what cells a worker is able to move without step up.
     * @param x starting position on x-axis
     * @param y starting position on y-axis
     * @return array of all available cells where worker can move
     */
    public Cell[] adjacentCellBlockedStepUpMoveAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&
                        (c[i][j].getLevel() <= GameBoard.getInstance().getCell(x, y).getLevel())) //cell at the same or lower level
                {
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;
    }
}