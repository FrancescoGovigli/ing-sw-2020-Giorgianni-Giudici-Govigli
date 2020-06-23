package it.polimi.ingsw.PSP42.model;

public interface ModelObservable {
    /**
     * Add an observer to the Model's observer list.
     * @param ob ModelObserver
     */
    public void attach(ModelObserver ob);

    /**
     * Removes an observer to the Model's observer list.
     * @param ob ModelObserver
     */
    public void detach(ModelObserver ob);

    /**
     * Notifies the observers who needs update.
     * @param o Object
     * @param s String
     */
    public abstract void notifyObservers(Object o, String s);
}
