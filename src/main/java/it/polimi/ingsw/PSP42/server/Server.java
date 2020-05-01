package it.polimi.ingsw.PSP42.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int SOCKET_PORT = 4040;
    private static ServerSocket serverSocket;
    private ArrayList<PlayerHandler> waitingClients = new ArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private int numberOfPlayer;
    private boolean numberOfPlayerSet;
    private boolean startedGame;
    private boolean firstConnected;

    public Server(){
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            System.out.println("Server is running...");
            System.out.println("IP: " + serverSocket.getInetAddress().getHostAddress());
            System.out.println("Port: " + serverSocket.getLocalPort());
            this.numberOfPlayer = -1;
            this.numberOfPlayerSet = false;
            this.startedGame = false;
            this.firstConnected = false;
        } catch (IOException e) {
            System.out.println("Cannot open Server serverSocket");
            System.exit(1);
        }
    }

    public void run(){
        while (true){
            try{
                Socket client = serverSocket.accept();
                NetworkVirtualView.sendToClient(client, ServerMessage.connectionDone);
                if (!startedGame) {
                    if (!firstConnected){
                        this.firstConnected = true;
                        NetworkVirtualView.sendToClient(client, ServerMessage.ableToPlay);
                        initNewClient(client);
                    }
                    else {
                        int i = 0;
                        while (!isNumberOfPlayerSet()) {
                            try {
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Waiting number of player setting... " + i*5 + " sec");
                            i++;
                        }
                        if (waitingClients.size() != numberOfPlayer) {
                            NetworkVirtualView.sendToClient(client, ServerMessage.ableToPlay);
                            initNewClient(client);
                        } else {
                            NetworkVirtualView.sendToClient(client, ServerMessage.extraClient);
                            client.close();
                        }
                    }
                }
                else {
                    String string = (ServerMessage.gameInProgress);
                    NetworkVirtualView.sendToClient(client, string);
                    client.close();
                }
            } catch (IOException e) {
                System.out.println("Connection Error!" + e.getMessage());
            }
        }
    }

    public boolean isNumberOfPlayerSet() {
        return numberOfPlayerSet;
    }

    public synchronized void initNewClient(Socket client){
        PlayerHandler playerConnection = new PlayerHandler(client, waitingClients.size() + 1, this);
        waitingClients.add(playerConnection);
        executor.submit(playerConnection);
    }

    public synchronized void setNumberOfPlayer(int i){
        this.numberOfPlayer = i;
        numberOfPlayerSet = true;
        notifyAll();
    }

    public synchronized void waitingRoom(){
        if (waitingClients.size() < numberOfPlayer) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Impossible waiting thread " + e.getMessage());
            }
        }
        else if (waitingClients.size() == numberOfPlayer && allPlayersAreReady()){
            initNewGame();
        }
    }

    public synchronized boolean allPlayersAreReady(){
        for (PlayerHandler p : waitingClients){
            if (!p.isReadyToPlay())
                return false;
        }
        return true;
    }

    public synchronized void initNewGame(){
        this.startedGame = true;
        System.out.println("Let's start!");
        GameThread gameThread = new GameThread(waitingClients);
        Thread gameT = new Thread(gameThread, "GameThread");
        gameT.start();

        try {
            wait();
        } catch (InterruptedException e) {
            System.out.println("Impossible setting in wait the last client connected to Server for playing " + e.getMessage());
        }

        // notifyAll();
        // send all values to Controller
        // At the end of game
        //this.startedGame = false;
    }
}
