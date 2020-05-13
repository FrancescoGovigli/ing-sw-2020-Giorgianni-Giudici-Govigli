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
     * Method used by VirtualView to send a message to the client
     * @param p (interested client)
     * @param message (object to send)
     */
    public static void sendToClient(PlayerHandler p, Object message) {
        p.asyncSend(message);
    }

    /**
     * Method used by VirtualView to receive a message to the client, it contains a 20 sec timer,
     * the time within which a response is expected from the client concerned,
     * when it expires, it will communicate the absence of input to the client and reset the game
     * @param p (interested client)
     * @return returns what the client sent (null is possible)
     */
    public static Object receiveFromClient(PlayerHandler p) {
        Object fromClient = null;
        final Object[] finalObj = {fromClient};
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (finalObj[0] == null) {
                    p.asyncSend("You input nothing. GAME OVER");
                    view.handleInterrupt();
                    game.resetGame("INTERRUPT");
                }
            }
        };
        timer.schedule(task, 20000);
        fromClient = p.asyncRead();
        timer.cancel();
        return fromClient;
    }
}
