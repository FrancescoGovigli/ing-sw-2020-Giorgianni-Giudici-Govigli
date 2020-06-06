package it.polimi.ingsw.PSP42.view;

public interface ViewObserver {

    /**
     * Used to update the observers communicate them the progression of the game (INIT ACTION).
     */
    public abstract void updateInit();

    /**
     * Used to update the observers communicate them the action to do during turn.
     * @return matrix contains all possible action
     */
    public abstract String[][] updateWhatToDo();

    /**
     * Used to update the observers communicate them the start of a turn.
     * @param i worker id chosen by player (1 = worker1 or 2 = worker2)
     */
    public abstract void updateStart(Integer i);

    /**
     * Used to update the observers communicate them the progression of the game (MOVE ACTION).
     */
    public abstract void updateMove();

    /**
     * Used to update the observers communicate them the progression of the game (BUILD ACTION).
     */
    public abstract void updateBuild();

    /**
     * Used to update the observers communicate them the activation of an effect.
     */
    public abstract void updateEffect();

    /**
     * Used to update the observers communicate them the end of current player turn.
     */
    public abstract void updateEnd();

    /**
     * Used to update the observers communicate them the game interruption.
     */
    public abstract void updateInterrupt();
}
