package it.polimi.ingsw.PSP42.server;

public class ServerMessage {

    // Server
    public static String GAME_START = ("GAME START");
    public static String END = ("END");
    public static String INACTIVITY = ("INACTIVITY");
    public static String DISCONNECTION = ("DISCONNECTION");
    // ServerGameThread
    public static String enteredGame = ("You entered the Game!" + " \uD83D\uDE0A \n");
    public static String gameEnd = ("End of Game");
    public static String inactivityEnd = ("Game interrupted due to inactivity of a player");
    public static String disconnectionEnd = ("A player has disconnected");
    public static String gameInProgress = ("Game in progress, try again later...");
    public static String extraClient = ("Sorry the Game has just been created, you are an Extra client");
    public static String waiting = ("You are waiting other Players to connect...");
    public static String nameNotFree = ("Name already taken, choose another nickname");
}
