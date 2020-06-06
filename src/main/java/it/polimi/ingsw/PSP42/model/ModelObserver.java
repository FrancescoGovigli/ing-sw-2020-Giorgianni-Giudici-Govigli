package it.polimi.ingsw.PSP42.model;

public interface ModelObserver {

    /**
     * Used to send to all players the update GameBoard after a player action.
     * @param object GameBoard
     * @param string used to inform other players about the current player action
     */
    public abstract void updateBoard(Object object, String string);
}
