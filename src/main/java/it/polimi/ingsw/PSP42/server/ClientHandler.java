package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;

public class ClientHandler {

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
    public ClientHandler(Socket client, int clientID) {
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
     * Method to send an object to client
     * It returns a thread because in the initialization phase it is invoked by every client that passes the accept(),
     * while in the course of the game it will be called only for the player in turn
     * @param message (object to send)
     */
    public Thread sendToClient(Object message) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("scrivo al client: " + clientID);
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
     * Method to receive an object from client
     * @return (received object)
     */
    public Object readFromClient() {
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
