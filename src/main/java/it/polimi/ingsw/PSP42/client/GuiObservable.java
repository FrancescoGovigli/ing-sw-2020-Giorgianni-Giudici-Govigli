package it.polimi.ingsw.PSP42.client;

public interface GuiObservable {

    /**
     * Used to inform ViewManager (observer) about a GUI choice done by Client.
     * @param input choice done
     */
    public abstract void informManagerInput(String input);
}
