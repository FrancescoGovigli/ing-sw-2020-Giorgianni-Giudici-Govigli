package it.polimi.ingsw.PSP42.client;

public interface ClientObserver {

    /**
     * Method used to set the scene layout for first player connected.
     */
    public abstract void updateWelcomeFirstPlayer();

    /**
     * Method used to set the scene layout for players that aren't first connected.
     */
    public abstract void updateWelcomeOtherPlayers();

    /**
     * Method used to wait until Player presses Play Button in Welcome Scene.
     */
    public abstract void updateConnectionStart();

    /**
     * Method used to set God's chosen scene layout.
     * @param listOfGods Gods available for choice
     */
    public abstract void updateGodSelection(Object listOfGods);

    /**
     * Method used to set Waiting scene layout.
     */
    public abstract void updateWaiting();

    /**
     * Method used to set label to inform player about his status.
     * @param o player status
     */
    public abstract void updateGameStatus(Object o);

    /**
     * Method used to set scene layout based on message received.
     * @param message received from Client
     */
    public abstract void updateGameMessage(Object message);

    /**
     * Method to print the current GameBoard situation on screen.
     * @param o gameBoard to show
     */
    public abstract void updateShow(Object o);
}
