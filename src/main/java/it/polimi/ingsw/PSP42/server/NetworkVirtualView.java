package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;

public class NetworkVirtualView {

    public static void sendToClient(PlayerHandler p,Object message){
        p.asyncSend(message);
    }


    public static Object receiveFromClient(PlayerHandler p) {
        return p.asyncRead();
    }
}
