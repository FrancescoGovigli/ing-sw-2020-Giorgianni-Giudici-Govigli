package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;

public class PlayerHandler {

    private Socket client;
    private int clientID;
    private boolean readyToPlay;
    private ObjectOutputStream out;
    private BufferedReader input;
    private boolean active = true;
    private String nickName;

    public Socket getClient() {
        return client;
    }

    public int getClientID() {
        return clientID;
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public BufferedReader getInput() {
        return input;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Constructor for the following object which will be managed by the ServerGameThread
     * (PlayerHandler contains all the information necessary for the server and the SGT
     * to correctly manage the interactions with the client (connection closure included))
     * @param client
     * @param clientID
     */
    public PlayerHandler(Socket client, int clientID) {
        this.client = client;
        this.clientID = clientID;
        this.readyToPlay = false;
        this.active = true;
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send an object to the client
     * @param message (object to send)
     */
    public Thread asyncSend(Object message) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(message);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Method to receive an object from the client
     * @return (received object)
     */
    public Object asyncRead() {
        final String[] str = new String[1];
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str[0] = input.readLine();
                    System.out.println("[FROM CLIENT] :" + str[0]);
                } catch (Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str[0];
    }

    public void closeConnection(){
        active = false;
    }
}
