package it.polimi.ingsw.PSP42;



public interface ViewObserver {
    /**
     * the methods updates the observer telling in particular the progression of the game (INIT ACTION)
     * @param o
     */
    public abstract void updateInit(Object o);

    /**
     * the methods updates the observer telling in particular the progression of the game (MOVE ACTION)
     */
    public abstract void updateMove(Object o);
    /**
     * the methods updates the observer telling in particular the progression of the game (BUILD ACTION)
     */
    public abstract void updateBuild(Object o);

    public abstract String updateCurrentPlayer();

    public abstract void updateEnd();

    public abstract void updateState(String s);
}
