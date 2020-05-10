package it.polimi.ingsw.PSP42.server;

public class NetworkVirtualView {

    /**
     * Method used by VirtualView to send a message to the client
     * @param p (interested client)
     * @param message (object to send)
     */
    public static void sendToClient(PlayerHandler p, Object message){
        p.asyncSend(message);
    }

    /**
     * Method used by VirtualView to receive a message to the client
     * @param p (interested client)
     * @return
     */
    public static Object receiveFromClient(PlayerHandler p) {
        return p.asyncRead();
    }
}
