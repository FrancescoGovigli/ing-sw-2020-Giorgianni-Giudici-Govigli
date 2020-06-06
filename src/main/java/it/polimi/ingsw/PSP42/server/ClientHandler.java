package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;

public class ClientHandler {

    private Socket clientSocket;
    private int clientID;
    private boolean readyToPlay;
    private ObjectOutputStream out;
    private BufferedReader input;
    private boolean active = true;
    private String nickName;

    public Socket getClientSocket() {
        return clientSocket;
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

    private void setActive(boolean active) {
        this.active = active;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Constructor for the following object which will be managed by the ServerGameThread.
     * (PlayerHandler contains all the information necessary for the Server and the SGT
     * to correctly manage the interactions with the Client (connection closure included))
     * @param clientSocket client' socket
     * @param clientID client's ID
     */
    public ClientHandler(Socket clientSocket, int clientID) {
        this.clientSocket = clientSocket;
        this.clientID = clientID;
        this.readyToPlay = false;
        this.active = true;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException in ClientHandler -> ClientHandler");
        }
    }

    /**
     * Method to send an object to client.
     * If an error occurs, the client is inactivated.
     * @param message (object to send)
     */
    public void sendToClient(Object message) {
        if (isActive()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        out.writeObject(message);
                        out.flush();
                    } catch (IOException e) {
                        clientInactivation();
                    }
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException in ClientHandler -> sendToClient");
            }
        }
    }

    /**
     * Method to receive an object from client.
     * If an error occurs, the client is inactivated.
     * @return (received object)
     */
    public Object readFromClient() {
        final String[] str = new String[1];
        Thread t = new Thread(() -> {
            try {
                str[0] = input.readLine();
                System.out.println("[FROM CLIENT] : " + str[0]);
            } catch (Exception e){
                clientInactivation();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException in ClientHandler -> readFromClient");
        }
        return str[0];
    }

    /**
     * Method to inactivate the client.
     */
    public void clientInactivation() {
        setActive(false);
        try {
            getOut().reset();
            getInput().reset();
        } catch (IOException e) {
            System.out.println("IOException in ClientHandler -> clientInactivation");
        }
    }
}
