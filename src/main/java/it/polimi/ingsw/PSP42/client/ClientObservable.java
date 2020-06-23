package it.polimi.ingsw.PSP42.client;

public interface ClientObservable {
    /**
     * Add an observer to the Model's observer list
     * @param ob
     */
    public void attach(ClientObserver ob);

    /**
     * Used to notify Client observer when first player is added.
     */
    public abstract void notifyWelcomeFirstPlayer();

    /**
     * Used to notify Client observer when an other player is added.
     */
    public abstract void notifyWelcomeOtherPlayers();

    /**
     * Used to notify Client that he is not connected to the Server until he presses play.
     */
    public abstract void notifyConnectionStart();

    /**
     * Used to notify a player to wait his turn.
     */
    public abstract void notifyWaiting();

    /**
     * Used to notify a player the possibility to choose his god.
     * @param listOfGods possible god to choose from the list
     */
    public abstract void notifyGodSelection(Object listOfGods);

    /**
     * Used to notify a player the status of game.
     * @param o status message
     */
    public abstract void notifyGameStatus(Object o);

    /**
     * Used to notify a player the updating of the gameBoard.
     * @param o gameBoard updated
     */
    public abstract void notifyShow(Object o);

    /**
     * Used to notify a player a game message.
     * @param message notified message
     */
    public abstract void notifyGameMessage(Object message);
}
