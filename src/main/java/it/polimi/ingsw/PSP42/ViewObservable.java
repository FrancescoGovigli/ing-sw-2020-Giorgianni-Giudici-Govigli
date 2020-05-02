package it.polimi.ingsw.PSP42;

import javax.swing.text.*;

public interface ViewObservable {
    /**
     * Add an observer to the View's observer list
     * @param ob
     */
    public void attach(ViewObserver ob);

    /**
     * Removes an observer to the View's observer list
     * @param ob
     */
    public void detach(ViewObserver ob);

    /**
     * notifies all observers that the view is initializing the game
     */
    public abstract void notifyInit(Object o);

    /**
     * notifies all observers that the user selected a move coordinate
     * @param o
     */
    public abstract void notifyMove(Object o);

    /**
     * notifies all observers that the user selected a build coordinate
     * @param o
     */
    public abstract void notifyBuild(Object o);

    public abstract void notifyEnd();

    public abstract String[][] notifyWhatToDo();

    public int notifyStart(Integer i);

    public abstract void notifyEffect();
}
