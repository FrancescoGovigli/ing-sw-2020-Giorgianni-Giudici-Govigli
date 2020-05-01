package it.polimi.ingsw.PSP42.server;

import java.io.IOException;
import java.net.Socket;

public class PlayerHandler implements Runnable{
    private Socket client;
    private int clientID;
    private Server server;
    private boolean readyToPlay;

    PlayerHandler(Socket client, int clientID, Server server){
        this.client = client;
        this.clientID = clientID;
        this.server = server;
        this.readyToPlay = false;
    }

    public Socket getClient() {
        return client;
    }

    public int getClientID() {
        return clientID;
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    @Override
    public synchronized void run() {
        System.out.println("Connected to " + client.getInetAddress());
        settingClient();
        // Restarting point after notifyAll() in playNewGame()
        try {
            NetworkVirtualView.sendToClient(client, ServerMessage.endGame);
            System.out.println("Connection dropped " + client.getInetAddress());
            client.close();
        } catch (IOException e) {
            System.out.println("run() in PlayerClient: IOException e");
            e.printStackTrace();
        }
    }

    private synchronized int chooseNumberOfPlayer(String name){
        String initial = (name + ", please enter the number of players: ");
        NetworkVirtualView.sendToClient(client, initial);
        return Integer.parseInt(NetworkVirtualView.receiveFromClient(client).toString());
    }

    private synchronized void settingClient(){
        String name;
        NetworkVirtualView.sendToClient(client, "Welcome player " + clientID + " insert your name: ");
        name = (String) NetworkVirtualView.receiveFromClient(client);
        System.out.println("Added player: " + name);
        if (clientID == 1){
            int i = chooseNumberOfPlayer(name);
            System.out.println("Number of players received is: " + i);
            server.setNumberOfPlayer(i);
        }
        this.readyToPlay = true;
        while (!server.isNumberOfPlayerSet()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Waiting error! " + e.getMessage());
            }
        }
        server.waitingRoom();
    }
}
