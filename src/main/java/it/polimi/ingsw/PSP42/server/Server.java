package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static final int SOCKET_PORT = 4000;
    private static ServerSocket serverSocket;
    private boolean gameStarted;
    private int connectionOrder;
    private int numberOfPlayer;
    private boolean numberOfPlayerSet;
    private ArrayList<Socket> connectedToServerClients = new ArrayList<>();
    private ArrayList<ClientHandler> waitingClients = new ArrayList<>();
    private Object waitingLock = new Object();

    /**
     * Constructor to set Server.
     */
    public Server() {
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            System.out.println("Server is running...");
            System.out.println("IP: " + serverSocket.getInetAddress().getHostAddress());
            System.out.println("Port: " + serverSocket.getLocalPort());
            this.gameStarted = false;
            this.connectionOrder = 0;
            this.numberOfPlayer = -1;
            this.numberOfPlayerSet = false;
        } catch (IOException e) {
            System.out.println("IOException in Server -> Server (Cannot open Server serverSocket)");
            System.exit(1);
        }
    }

    /**
     * Method to run the Server, it only manages new connections.
     * Server admits until the game has started.
     */
    public void run() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                if (!gameStarted) {
                    connectedToServerClients.add(client);
                    newClientInitialization(client);
                }
                else {
                    try {
                        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                        output.writeObject(ServerMessage.gameInProgress);
                        output.flush();
                    } catch (IOException e) {
                        System.out.println("IOException in Server -> run (else)");
                    }
                    client.close();
                }
            } catch (IOException e) {
                System.out.println("IOException in Server -> run");
            }
        }
    }

    /**
     * Method to associate a ServerGameThread to each connected client that will manage it until the game starts.
     * @param client client' socket
     */
    public synchronized void newClientInitialization(Socket client) {
        connectionOrder++;
        System.out.println("Connected to " + client.getRemoteSocketAddress());
        ServerGameThread serverGameThread = new ServerGameThread(this, client, connectionOrder);
        Thread thread = new Thread(serverGameThread);
        thread.start();
    }

    /**
     * Method to put each connected client on hold until the Server has reached the number of players needed
     * to start the game, the excess clients will be expelled (closing their connection).
     * The new game will be initialized by the last client who will satisfy the number of players.
     * @param sgt (ServerGameThread that assisted the client during the initialization phase)
     */
    public void waitingRoom(ServerGameThread sgt) {
        synchronized (waitingLock) {
            ClientHandler sgtClient = sgt.getManagedClient();
            sgtClient.setReadyToPlay(true);
            if (sgt.getOrderOfConnection() <= numberOfPlayer) {
                waitingClients.add(sgtClient);
                System.out.println(sgt.getClientNickname() + " player added to the Waiting Room");
                if (waitingClients.size() == numberOfPlayer)
                    newGameInitialization();
                else
                    sgt.send(sgtClient, ServerMessage.waiting);
            } else {
                sgt.send(sgtClient, ServerMessage.extraClient);
                sgtClient.clientInactivation();
                try {
                    Socket socket = sgtClient.getClientSocket();
                    connectedToServerClients.remove(socket);
                    socket.close();
                } catch (IOException e) {
                    System.out.println("IOException in Server -> waitingRoom (else)");
                }
            }
            waitingLock.notifyAll();
        }
    }


    /**
     * Method to verify that all waiting players are ready to play (they will be ready after entering their name).
     * @return true if they are ready, false otherwise
     */
    public synchronized boolean allPlayersAreReady() {
        for (ClientHandler clientHandler : ServerGameThread.getSgtClients()) {
            if (!clientHandler.isReadyToPlay() && clientHandler.isActive())
                return false;
        }
        return true;
    }

    /**
     * Method to start the game.
     * The Server takes note of the start of the game and delegates its execution to a ServerGameThread,
     * so the Server will be able to manage new possible connection requests.
     */
    public void newGameInitialization() {
        synchronized (waitingLock) {
            while (!allPlayersAreReady()) {
                try {
                    waitingLock.wait();
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException in Server -> newGameInitialization");
                }
            }
        }
        this.gameStarted = true;
        System.out.println(ServerMessage.GAME_START);
        ServerGameThread game = new ServerGameThread(this, (ArrayList<ClientHandler>) waitingClients.clone());
        game.startGame();
    }

    /**
     * Method to reset the Server.
     * It restores the default values to be able to create a new game and closes all open sockets.
     * @param cause (string identifying why the game should be reset)
     */
    public synchronized void reset(String cause) {
        gameStarted = false;
        connectionOrder = 0;
        numberOfPlayer = - 1;
        numberOfPlayerSet = false;
        for (Socket socket : connectedToServerClients) {
            try {
                System.out.println("Closing socket with IP: " + socket.getRemoteSocketAddress());
                socket.close();
            } catch (IOException e) {
                System.out.println("IOException in Server -> reset (ERROR closing socket " + socket.getRemoteSocketAddress() + ")");
            }
        }
        connectedToServerClients.clear();
        waitingClients.clear();
        ServerGameThread.reactivateConnectionState();
        System.out.println("Reset generated by " + cause);
    }

    /**
     * Method to get the number of players set.
     * @return numberOfPlayer
     */
    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    /**
     * Method to set the number of players.
     * @param numberOfPlayer
     */
    public synchronized void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
        numberOfPlayerSet = true;
    }

    /**
     * Method to check if the number of players has been set.
     * @return true if it has been set, false otherwise.
     */
    public boolean isNumberOfPlayerSet() {
        return numberOfPlayerSet;
    }

    /**
     * Method to check if the nickname entered is already taken by the players in the waiting room.
     * @param nickName (nickname to be verified)
     * @return true if the name is free (unique), false otherwise.
     */
    public boolean isNickNameUnique(String nickName) {
        for (int i = 0; i < waitingClients.size(); i++) {
            if (waitingClients.get(i).getNickName().equals(nickName))
                return false;
        }
        return true;
    }
}