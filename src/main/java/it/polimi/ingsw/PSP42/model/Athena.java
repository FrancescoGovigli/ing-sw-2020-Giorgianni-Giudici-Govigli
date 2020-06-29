package it.polimi.ingsw.PSP42.model;

import java.util.ArrayList;

/**
 * Thanks to this simple god if a player's worker, who has this god, steps up, other players workers can't.
 */
public class Athena extends SimpleGod {

    private int blockOpponentsStepUp = 0;
    private int counterPhase = 0;

    public Athena(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] START = {"EFFECT"};
        String[] PREMOVE = {"EMPTY"};
        String[] MOVE = {"MOVE"};
        String[] PREBUILD = {"EMPTY"};
        String[] BUILD = {"BUILD"};
        String[] END = {"EFFECT"};
        String[][] phase = {START, PREMOVE, MOVE, PREBUILD, BUILD, END};
        return phase;
    }

    /**
     * Used by Athena as standard moveAvailable method.
     * Used by opponent's workers, if they can't step up, to know if they can move in that position.
     * @param x x-axis position
     * @param y y-axis position
     * @param w worker who wants to move
     * @return true if worker's able to move, false otherwise
     */
    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        if (!(w.getPlayer().getCard() instanceof Athena) && blockOpponentsStepUp == 1)
            return powerMoveBlockedStepUpAvailable(x, y, w);
        return GameBoard.getInstance().moveAvailable(x, y, w);
    }

    /**
     * Used to know if Worker w can move in that position without step up.
     * @param x where worker wants to move on x-axis
     * @param y where worker wants to move on y-axis
     * @param w worker who wants to move
     * @return true if worker can, false otherwise
     */
    public boolean powerMoveBlockedStepUpAvailable(int x, int y, Worker w) {
        if (GameBoard.getInstance().getCell(x, y).getLevel() -
                GameBoard.getInstance().getCell(w.getCurrentX(), w.getCurrentY()).getLevel() <= 0 &&
                w.getPlayer().getCard().powerMoveAvailable(x, y, w))
            return true;
        return false;
    }

    /**
     * Used to move worker and to set "blockOpponentsStepUp" if Athena's worker step up.
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker who wants to move
     * @return true if worker was moved, false otherwise
     */
    @Override
    public boolean powerMove(int x, int y, Worker w) {
        for (Player player : playersWithEffect)
            if (player != null && !player.getCard().powerMoveAvailable(x, y, w))
                return false;
        if (powerMoveAvailable(x, y, w)) {
            counterPhase = 1;
            if (workerStepUp(x, y, w))
                blockOpponentsStepUp = 1;
            w.setPosition(x, y);
            return true;
        }
        return false;
    }

    /**
     * Check if worker steps up during his movement.
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
     * Check if power was activated.
     * @return true if other players were blocked, false otherwise
     */
    @Override
    public boolean powerEffectAvailable() {
        if (blockOpponentsStepUp == 1)
            return true;
        return false;
    }

    /**
     * If, at first iteration (counter = 0 = START),
     * effect was applied in precedent turn set effectMove false and effectPlayer with this player for other players.
     * Else if, at second iteration (counter = 1 = END),
     * effect was applied in this turn set effectMove true and effectPlayer with this player for other players.
     * @return true if the method was been used, false otherwise
     */
    @Override
    public boolean powerEffect() {
        if (counterPhase == 0) {
            if (powerEffectAvailable()) {
                for (Player player : GameBoard.getInstance().getPlayers()) {
                    player.getCard().playersWithEffect.remove(this.w1.getPlayer());
                }
                blockOpponentsStepUp = 0;
                return true;
            } else {
                return false;
            }
        }
        if (counterPhase == 1) {
            if (powerEffectAvailable()) {
                for (Player player : GameBoard.getInstance().getPlayers()) {
                    player.getCard().playersWithEffect.add(this.w1.getPlayer());
                }
                counterPhase = 0;
                return true;
            } else {
                counterPhase = 0;
                return false;
            }
        }
        return false;
    }

    public String effectON() {
        return "Until your next turn other players can't step up";
    }

    public String effectOFF() {
        return "Other players now can step up!\n";
    }

    @Override
    public ArrayList<Integer> getCurrentValues() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(blockOpponentsStepUp);
        values.add(counterPhase);
        return (ArrayList<Integer>) values.clone();
    }

    @Override
    public void reSetValues(ArrayList<Integer> valuesToRestore) {
        this.blockOpponentsStepUp = valuesToRestore.get(0);
        this.counterPhase = valuesToRestore.get(1);
    }
}
