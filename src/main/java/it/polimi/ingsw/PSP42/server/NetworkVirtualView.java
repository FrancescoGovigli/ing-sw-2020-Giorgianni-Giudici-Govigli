package it.polimi.ingsw.PSP42.server;

import it.polimi.ingsw.PSP42.view.*;
import java.util.*;

public class NetworkVirtualView {

    private static ServerGameThread game;
    private static VirtualView view;

    public NetworkVirtualView(ServerGameThread sgt, VirtualView view) {
        game = sgt;
        this.view = view;
    }

    /**
     * Method used by VirtualView to send a message to the player
     * @param p (interested player)
     * @param message (object to send)
     */
    public static void sendToPlayer(ClientHandler p, Object message) {
        p.sendToClient(message);
    }

    /**
     * Method used by VirtualView to receive a message to the player, it contains a 20 sec timer,
     * the time within which a response is expected from the player concerned,
     * when it expires, it will communicate the absence of input to the player and reset the game
     * @param p (interested player)
     * @return returns what the player sent (null is possible)
     */
    public static Object readFromPlayer(ClientHandler p) {
        Object fromClient = null;
        final Object[] finalObj = {fromClient};
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (finalObj[0] == null) {
                    p.sendToClient("You input nothing. GAME OVER");
                    view.handleInterrupt();
                    game.resetGame("INTERRUPT");
                }
            }
        };
        timer.schedule(task, 20000);
        fromClient = p.readFromClient();
        timer.cancel();
        return fromClient;
    }
}
