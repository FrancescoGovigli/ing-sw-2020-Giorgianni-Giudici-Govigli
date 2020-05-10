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
            this.count=0;
        } catch (IOException e) {
            System.out.println("Cannot open Server serverSocket");
            System.exit(1);
        }
    }

    //GESTISCE SOLO LE NUOVE CONNESSIONI (vedi commento sopra metodo waitingRoom())
    /**
     * Method to run the server, it only manages new connections
     */
    public void run() {
        while (true){
            try{
                Socket client = serverSocket.accept();
                if (!startedGame) {
                    //SE IL GIOCO NON E' INIZIATO CREO THREAD PER IL NUOVO CLIENT
                    initNewClient(client);
                }
                else { //ALTRIMENTI RIFIUTO
                    try {
                        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                        output.writeObject(ServerMessage.gameInProgress);
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //NetworkVirtualView.send(client,string);
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

    /* ASSOCIAMO UN ID IN ORDINE DI CONNESSIONE AD OGNI CLIENT e
       PASSIAMO L'OGGETTO SERVER A CUI IL PLAYERHANDLER FA RIFERIMENTO
       IN MODO DA POTER GESTIRE LA CHIUSURA DELLA CONNESSIONE
     */
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

    /* FINCHE IL GIOCO NON E' COMINCIATO TUTTI I CLIENT CHE SI CONNETTONO VENGONO MESSI IN ATTESA DOPO CHE GLI E'
       STATO CHIESTO IL NOME, UNA VOLTA CHE IL PRIMO CLIENT AD ESSERSI CONNESSO INSERISCE IL NUMERO DI
       GIOCATORI I CLIENT IN ECCESSO VENGONO ESPULSI CON MESSAGGIO DI EXTRA CLIENT, GLI SI CHIUDE LA CONNESSIONE
       E IL GIOCO COMINCIA (startgame = true) e CREIAMO UN GAMETHREAD PER RUNNARE LA PARTITA.
     */
    /**
     * Method to put each connected client on hold until the server has reached the number of players needed
     * to start the game, the excess clients will be expelled (closing their connection)
     * @param sgt
     */
    public synchronized void waitingRoom(ServerGameThread sgt)  {
        //Devo dire che il giocatore che si disconnette con Clientid = 3 quello con Clientid a 4 va a 3.
        if (sgt.getOrderOfConnection() <= numberOfPlayer) {
            waitingClients.add(sgt.getPlayerConnection());
            System.out.println("Added player: " + sgt.getClientNickname());

            if (waitingClients.size() == numberOfPlayer && allPlayersAreReady())
                initNewGame();

            else if(allPlayersAreReady() && waitingClients.size() != numberOfPlayer)
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
}
