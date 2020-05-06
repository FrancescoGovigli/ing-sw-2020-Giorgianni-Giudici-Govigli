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

    public boolean isStartedGame() {
        return startedGame;
    }

    private boolean startedGame;
    private int count;

    public Server(){
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

    public void run(){
        while (true){
            try{
                Socket client = serverSocket.accept();
                if (!startedGame) //SE IL GIOCO NON E' INIZIATO CREO THREAD PER IL NUOVO CLIENT
                    initNewClient(client);
                else { //ALTRIMENTI RIFIUTO
                    String string = (ServerMessage.gameInProgress);
                    NetworkVirtualView.send(client,string);
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

    /* ASSOCIAMO UN ID IN ORDINE DI CONNESSIONE AD OGNI CLIENT e
       PASSIAMO L'OGGETTO SERVER A CUI IL PLAYERHANDLER FA RIFERIMENTO
       IN MODO DA POTER GESTIRE LA CHIUSURA DELLA CONNESSIONE
     */
    public synchronized void initNewClient(Socket client){
        count++;
        PlayerHandler playerConnection = new PlayerHandler(client, count, this);
        Thread t = new Thread(playerConnection);
        t.start();
    }

    public synchronized void setNumberOfPlayer(int i){
        this.numberOfPlayer = i;
        numberOfPlayerSet = true;
    }

    /*
      FINCHE IL GIOCO NON E' COMINCIATO TUTTI I CLIENT CHE SI CONNETTONO VENGONO MESSI IN ATTESA DOPO CHE GLI E'
      STATO CHIESTO IL NOME, UNA VOLTA CHE IL PRIMO CLIENT AD ESSERSI CONNESSO INSERISCE IL NUMERO DI
      GIOCATORI I CLIENT IN ECCESSO VENGONO ESPULSI CON MESSAGGIO DI EXTRA CLIENT, GLI SI CHIUDE LA CONNESSIONE
      E IL GIOCO COMINCIA (startgame = true) e CREIAMO UN GAMETHREAD PER RUNNARE LA PARTITA.
     */

    public synchronized void waitingRoom(PlayerHandler p)  {
        //Devo dire che il giocatore che si disconnette con CLientid = 3 quello con Clientid a 4 va a 3.
        if (p.getClientID()<=numberOfPlayer) {
            waitingClients.add(p);
            System.out.println("Added player: " + p.getNickName());

            if (waitingClients.size() == numberOfPlayer && allPlayersAreReady())
                initNewGame();



            else if(allPlayersAreReady() && waitingClients.size()!=numberOfPlayer)
                NetworkVirtualView.sendToClient(p.getOut(),ServerMessage.waiting);
        }
        else {
            NetworkVirtualView.sendToClient(p.getOut(), ServerMessage.extraClient);
            p.closeConnection();

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

        waitingClients.sort((PlayerHandler z1, PlayerHandler z2) -> {
            if (z1.getClientID() > z2.getClientID())
                return 1;
            if (z1.getClientID() < z2.getClientID())
                return -1;
            return 0;
        });
        
        GameThread gameThread = new GameThread(waitingClients,numberOfPlayer);
        Thread gameT = new Thread(gameThread, "GameThread");
        gameT.start();


    }
}
