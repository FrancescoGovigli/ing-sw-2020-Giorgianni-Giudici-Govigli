package it.polimi.ingsw.PSP42.server;

public class NetworkVirtualView {

    private static ServerGameThread game;

    /**
     * Method for assigning the ServerGameThread.
     * Used by the ServerGameThread who will manage the game.
     * @param sgt ServerGameThread
     */
    public static void assignSGT(ServerGameThread sgt) {
        game = sgt;
    }

    /**
     * Method used by VirtualView to send a object to the player.
     * @param clientHandler (interested player)
     * @param object (object to send)
     */
    public static void sendToPlayer(ClientHandler clientHandler, Object object) {
        game.send(clientHandler, object);
    }

    /**
     * Method used by VirtualView to receive a message from the player.
     * If the player is no longer available, the game will be reset.
     * @param clientHandler (interested player)
     * @return returns what the player sent (null is possible)
     */
    public static Object readFromPlayer(ClientHandler clientHandler) {
        if (!game.isConnectionAvailable()) {
            game.resetGame();
            return null;
        }
        return game.read(clientHandler);
    }
}
