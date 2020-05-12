package it.polimi.ingsw.PSP42.server;

import it.polimi.ingsw.PSP42.view.*;

import java.util.*;

public class NetworkVirtualView {
    private static ServerGameThread game;
    private static VirtualView view;

    public NetworkVirtualView(ServerGameThread sgt,VirtualView view){
        game = sgt;
        this.view = view;
    }
    public static void sendToClient(PlayerHandler p,Object message){
        p.asyncSend(message);
    }

    //HO UN ATTRIBUTO DELL'OGGETTO GAME E SE SCADE IL TIMER DI 10sec ALLORA MANDA MESSAGGIO DI
    //INTERRUPT E RISETTA IL GIOCO
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