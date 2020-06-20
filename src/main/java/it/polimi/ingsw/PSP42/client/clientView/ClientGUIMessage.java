package it.polimi.ingsw.PSP42.client.clientView;

import it.polimi.ingsw.PSP42.view.ViewMessage;

/**
 * Used to show the correct message on the GameBoard.
 */
public class ClientGUIMessage {

    public static String workerGUIMessage = "Choose your worker";
    public static String initialPositionGUIMessage = "Click where to place your worker";
    public static String moveGUIMessage = "Click where to move your worker";
    public static String buildGUIMessage = "Click where to build with your worker";
    public static String levelGUIMessage = "Click on Activate Button to build a specific level ";
    public static String insertLevelGUIMessage = "Click on the Dome to build it, otherwise click the standard building";
    public static String applyGUIPowerMessage = "Click on Power Button to apply your God's power ";
    public static String undoGUIMessage = "Click on UNDO Button to change your choice within 5 seconds";
    public static String notUndoGUIMessage = "UNDO wasn't done. Click on the board to continue";

    /**
     * Used to change message received from Server for the GUI.
     * @param message String contains the Server's message
     * @return correct GUI message
     */
    public static String translateMessage(String message) {
        if (message.contains(ViewMessage.workerMessage))
            return workerGUIMessage;
        else if (message.contains(ViewMessage.initialPositionMessage))
            return initialPositionGUIMessage;
        else if (message.contains(ViewMessage.moveMessage))
            return moveGUIMessage;
        else if (message.contains(ViewMessage.buildMessage))
            return buildGUIMessage;
        else if (message.contains(ViewMessage.levelMessage))
            return levelGUIMessage;
        else if (message.contains(ViewMessage.insertLevel))
            return insertLevelGUIMessage;
        else if (message.contains(ViewMessage.applyPowerMessage))
            return applyGUIPowerMessage;
        else if (message.contains("UNDO") && !(message.contains("input nothing")))
            return undoGUIMessage;
        else if (message.contains("You input nothing"))
            return notUndoGUIMessage;
        else
            return message;
    }
}
