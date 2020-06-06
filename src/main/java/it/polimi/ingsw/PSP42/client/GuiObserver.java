package it.polimi.ingsw.PSP42.client;

public interface GuiObserver {

    /**
     * Used to convert a GUI action into an input.
     * @param input as String
     */
    public abstract void fromGuiInput(String input);
}
