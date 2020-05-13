package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int SOCKET_PORT = 4000;
    private static ServerSocket serverSocket;
    private ArrayList<PlayerHandler> waitingClients = new ArrayList<>();
    private int numberOfPlayer;
    private boolean numberOfPlayerSet;
    private boolean startedGame;
    private int count;

    public Server() {
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            System.out.println("Server is running...");
            System.out.println("IP: " + serverSocket.getInetAddress().getHostAddress());
            System.out.println("Port: " + serverSocket.getLocalPort());
            this.numberOfPlayer = -1;
            this.numberOfPlayerSet = false;
            this.startedGame = false;
            this.count = 0;
        } catch (IOException e) {
            System.out.println("Cannot open Server serverSocket");
            System.exit(1);
        }
    }

    /**
     * Method to run the server, it only manages new connections
     */
    public void run() {
        while (true){
            try{
                Socket client = serverSocket.accept();
                if (!startedGame) {
                    initNewClient(client);
                }
                else {
                    try {
                        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                        output.writeObject(ServerMessage.gameInProgress);
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    client.close();
                }
            } catch (IOException e) {
                System.out.println("Connection Error!" + e.getMessage());
            }
        }
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public boolean isNumberOfPlayerSet() {
        return numberOfPlayerSet;
    }

    public synchronized void setNumberOfPlayer(int i) {
        this.numberOfPlayer = i;
        numberOfPlayerSet = true;
    }

    /**
     * Method to associate an ServerGameThread to each connected client that will manage it until the game starts
     * @param client
     */
    public synchronized void initNewClient(Socket client) {
        count++;
        System.out.println("Connected to " + client.getInetAddress());
        ServerGameThread server = new ServerGameThread(this, client, count);
        Thread t = new Thread(server);
        t.start();
    }

    /**
     * Method to put each connected client on hold until the server has reached the number of players needed
     * to start the game, the excess clients will be expelled (closing their connection)
     * @param sgt
     */
    public synchronized void waitingRoom(ServerGameThread sgt) {
        if (sgt.getOrderOfConnection() <= numberOfPlayer) {
            waitingClients.add(sgt.getPlayerConnection());
            System.out.println("Added player: " + sgt.getClientNickname());
            if (waitingClients.size() == numberOfPlayer && allPlayersAreReady())
                initNewGame();
            else if (allPlayersAreReady() && waitingClients.size() != numberOfPlayer)
                sgt.asyncClientSend(ServerMessage.waiting);
        }
        else {
            sgt.asyncClientSend(ServerMessage.extraClient);
            sgt.getPlayerConnection().closeConnection();
        }
    }

    /**
     * Method to verify that all waiting players are ready to play (they will be ready after entering their name)
     * @return true if they are ready, false otherwise
     */
    public synchronized boolean allPlayersAreReady() {
        for (PlayerHandler playerHandler : waitingClients){
            if (!playerHandler.isReadyToPlay())
                return false;
        }
        return true;
    }

    /**
     * Method for starting the game
     */
    public synchronized void initNewGame() {
        this.startedGame = true;
        System.out.println("Let's start!");
        ServerGameThread game = new ServerGameThread(waitingClients, this);
        game.startGame();
    }

    /**
     * Method to reset the server
     * @param s (string identifying why the game should be reset)
     */
    public void reset(String s) {
        startedGame = false;
        numberOfPlayer = -1;
        numberOfPlayerSet = false;
        count = 0;
        waitingClients.clear();
        if(s.equals("END"))
            System.out.println("GAME ended correctly");
        else
            System.out.println("GAME has been interrupted");
    }
}
