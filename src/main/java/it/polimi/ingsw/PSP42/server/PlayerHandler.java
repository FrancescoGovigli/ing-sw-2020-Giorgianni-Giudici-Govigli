package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class PlayerHandler implements Runnable{
    private Socket client;
    private int clientID;
    private Server server;
    private boolean readyToPlay;
    private ObjectOutputStream out;

    public BufferedReader getInput() {
        return input;
    }

    private BufferedReader input;

    public ObjectOutputStream getOut() {
        return out;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active=true;
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
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


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
        try {
            //NetworkVirtualView.sendToClient(this, "Welcome!\nWhat is your name?");
            settingClient();
            //String read = input.next();
            //nickName = read;
            //server.waitingRoom(this);
            while (isActive()) {
                if(!server.isStartedGame()) {
                    try {
                        input.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //notify(read);
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int chooseNumberOfPlayer(String name){
        String initial = (name + ", please enter the number of players: ");
        NetworkVirtualView.sendToClient(getOut(), initial);
        return Integer.parseInt(NetworkVirtualView.receiveFromClient(input).toString());
    }

    private void settingClient(){

        if(clientID==1)
            NetworkVirtualView.sendToClient(getOut(), "Welcome player " + clientID + " insert your name: ");
        else
            NetworkVirtualView.sendToClient(getOut(), "Welcome player " + clientID +" you are waiting the FIRST PLAYER to set the number of players, insert your name: ");
        nickName = (String) NetworkVirtualView.receiveFromClient(input);

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

    public Thread asyncSend(Object message){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkVirtualView.sendToClient(getOut(),message);
            }
        });
        t.start();
        return t;
    }

    public Object asyncRead(){
        final Object[] o = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                o[0] = NetworkVirtualView.receiveFromClient(getInput());
            }
        }).start();
        return o[0];
    }
}
