package it.polimi.ingsw.PSP42;

public interface ClientObserver {

    public abstract void updateWelcomeFirstPlayer();

    public abstract void updateWelcomeOtherPlayers();

    public abstract void updateConnectionStart();

    public abstract void updateGodSelection(Object listOfGods);

    public abstract void updateWaiting();

    public abstract void updateExistingNickName();

    public abstract void updateShow(Object o);

    public abstract void updateGameMessage(Object message);
}
