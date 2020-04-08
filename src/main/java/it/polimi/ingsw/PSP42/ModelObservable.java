package it.polimi.ingsw.PSP42;

public interface ModelObservable {
    /**
     * Add an observer to the Model's observer list
     * @param ob
     */
    public void attach(ModelObserver ob);

    /**
     * Removes an observer to the Model's observer list
     * @param ob
     */
    public void detach(ModelObserver ob);

    /**
     * notifies the observers who need update
     */
    public abstract void notifyObservers(Object o);


}
