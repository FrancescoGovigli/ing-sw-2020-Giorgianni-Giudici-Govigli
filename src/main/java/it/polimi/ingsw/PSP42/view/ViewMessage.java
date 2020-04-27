package it.polimi.ingsw.PSP42.view;

public class ViewMessage {
    public static String numberOfPlayersMessage = "Insert the number of Players:";
    public static String workerMessage = "Choose worker to use (1 or 2):";
    public static String initialPositionMessage = "insert where to position your worker";
    public static String moveMessage = "Make your move (x,y):";
    public static String buildMessage = "Make your build (x,y) with Worker ";
    public static String LevelMessage = "Do you want to build a none default level (YES or NO)?";
    public static String waitMessage = "Wait for the other player's move!";
    public static String winMessage = "You won!";
    public static String loseMessage = "You lost!";
    public static String drawMessage = "Draw!";
    public static String wrongTurnMessage = "It is not your turn!";
    public static String occupiedCellMessage = "The chosen cell is not empty!";
    public static String applyPowerMessage = "Do you want to apply your power?(YES OR NO)";

    /**
     * Says that the current player has lost the game
     * @param s
     */
    public static void loseMessage(String s){
        System.out.println(s+ " " + ViewMessage.loseMessage);
    }
}
