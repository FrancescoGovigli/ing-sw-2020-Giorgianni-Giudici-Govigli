package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;

public class PlayerHandler implements Runnable{
    private Socket client;
    private int clientID;
    private Server server;
    private boolean readyToPlay;
    private boolean active;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    PlayerHandler(Socket client, int clientID, Server server){
        this.client = client;
        this.clientID = clientID;
        this.server = server;
        this.readyToPlay = false;
        this.active = true;
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
    public void run() {
        System.out.println("Connected to " + client.getInetAddress());
        settingClient();
        while(active) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Restarting point after notifyAll() in playNewGame()
        try {
            NetworkVirtualView.sendToClient(client, ServerMessage.connectionClosed);
            System.out.println("Connection dropped " + client.getInetAddress());
            client.close();
        } catch (IOException e) {
            System.out.println("run() in PlayerClient: IOException e");
            e.printStackTrace();
        }
    }

    private int chooseNumberOfPlayer(String name){
        String initial = (name + ", please enter the number of players: ");
        NetworkVirtualView.sendToClient(client, initial);
        return Integer.parseInt(NetworkVirtualView.receiveFromClient(client).toString());
    }

    private void settingClient(){

        if(clientID==1)
            NetworkVirtualView.sendToClient(client, "Welcome player " + clientID + " insert your name: ");
        else
            NetworkVirtualView.sendToClient(client, "Welcome player " + clientID +" you are waiting the FIRST PLAYER to set the number of players, insert your name: ");
        nickName = (String) NetworkVirtualView.receiveFromClient(client);
        if (clientID == 1){
            int i = chooseNumberOfPlayer(nickName);
            System.out.println("Number of players received is: " + i);
            server.setNumberOfPlayer(i);
        }
        this.readyToPlay = true;
        while (!server.isNumberOfPlayerSet()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Waiting error! " + e.getMessage());
            }
        }
        server.waitingRoom(this);
    }

    public void closeConnection(){
            active = false;
    }

    public void asyncSend(Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkVirtualView.sendToClient(getClient(),message);
            }
        }).start();
    }
}
