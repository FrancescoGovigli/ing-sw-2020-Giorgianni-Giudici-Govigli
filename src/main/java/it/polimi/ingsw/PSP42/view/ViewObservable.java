package it.polimi.ingsw.PSP42.view;

public interface ViewObservable {

    /**
     * Add an observer to the View's observer list.
     * @param ob viewObserver
     */
    public abstract void attach(ViewObserver ob);

    /**
     * Notifies all observers about game initialization by view.
     */
    public abstract void notifyInit();

    /**
     * Notifies all observers about the action to do during turn.
     * @return matrix contains all possible action
     */
    public abstract String[][] notifyWhatToDo();

    /**
     * Notifies all observers that user start his turn.
     * @param i worker id chosen by player (1 = worker1 or 2 = worker2)
     */
    public abstract void notifyStart(Integer i);

    /**
     * Notifies all observers that user selected a move coordinate.
     */
    public abstract void notifyMove();

    /**
     * Notifies all observers that user selected a build coordinate.
     */
    public abstract void notifyBuild();

    /**
     * Notifies all observers that an effect was activated.
     */
    public abstract void notifyEffect();

    /**
     * Notifies all observers that user end his turn.
     */
    public abstract void notifyEnd();

    /**
     * Notifies all observers that game was interrupted.
     */
    public abstract void notifyInterrupt();
}