package it.polimi.ingsw.PSP42;

public interface ClientObservable {
    /**
     * Add an observer to the Model's observer list
     * @param ob
     */
    public void attach(ClientObserver ob);

    public abstract void notifyWelcomeFirstPlayer();

    public abstract void notifyWelcomeOtherPlayers();

    public abstract void notifyConnectionStart();

    public abstract void notifyGodSelection(Object listOfGods);
}
