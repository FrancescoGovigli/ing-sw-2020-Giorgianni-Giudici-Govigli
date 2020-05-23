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

    public abstract void notifyWaiting();

    public abstract void notifyGodSelection(Object listOfGods);

    public abstract void notifyGameStatus(Object o);

    public abstract void notifyShow(Object o);

    public abstract void notifyGameMessage(Object message);

}
