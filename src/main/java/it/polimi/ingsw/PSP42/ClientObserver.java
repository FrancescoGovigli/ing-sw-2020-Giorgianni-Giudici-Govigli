package it.polimi.ingsw.PSP42;

public interface ClientObserver {

    public abstract void updateWelcomeFirstPlayer();

    public abstract void updateWelcomeOtherPlayers();

    public abstract void updateConnectionStart();

    public abstract void updateGodSelection(Object listOfGods);
}
