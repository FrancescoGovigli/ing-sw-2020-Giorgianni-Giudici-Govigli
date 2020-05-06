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
    private BufferedReader input;

    public BufferedReader getInput() {
        return input;
    }

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

    public int getClientID() {
        return clientID;
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    /*CHIAMA SETTING CLIENT() CHE RICHIEDE IL NOME E IL NUMERO DI GIOCATORI SE
      CLIENT ID = 1 (IL PRIMO CONNESSO) E IL THREAD RIMANE ATTIVO FINO A CHE IL CLIENT E' CONNESSO
     */

    //TODO SE E' IN GIOCO FINCHE NON PERDE ACTIVE = TRUE E FIN TANTO CHE NON VIENE INILIZIALIZZATA
    //TODO LA PARTITA. CREO UN THREAD DI SOLA LETTURA CHE RIMANE IN WHILE. SETTING CLIENT() VA NEL SERVER
    //TODO CHOOSENUM() NEL SERVER ANCHE CLOSE CONNECTION().

    @Override
    public void run() {
        System.out.println("Connected to " + client.getInetAddress());
        try {
            settingClient();
            //t0.join()
            while (isActive()) {
                //read();
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private int chooseNumberOfPlayer(String name){
        boolean correctChoice = false;
        Integer choice=null;
        String initial = (name + ", please enter the number of players: ");
        NetworkVirtualView.sendToClient(getOut(), initial);
        while(!correctChoice) {
            if(choice!=null){
                initial = (name + ", please enter a correct value of players (2 or 3): ");
                NetworkVirtualView.sendToClient(getOut(), initial);
            }
            choice = Integer.parseInt(NetworkVirtualView.receiveFromClient(input).toString());
            if (choice == 2 || choice == 3)
                correctChoice = true;
        }
        return choice;

    }

    private void settingClient(){
        NetworkVirtualView.sendToClient(getOut(),"You entered the Game!"+ " \uD83D\uDE0A \n");
        if(clientID==1)
            NetworkVirtualView.sendToClient(getOut(), "Welcome player " + clientID + " insert your name: ");
        else
            NetworkVirtualView.sendToClient(getOut(), "Welcome player " + clientID +" you are waiting the FIRST PLAYER to set the number of players, insert your name: ");
        //Devo garantire univocità nickname perciò se esiste gia dovrò mandare una nuova richiesta
        nickName = (String) NetworkVirtualView.receiveFromClient(input);

        if (clientID == 1){
            int i = chooseNumberOfPlayer(nickName);
            System.out.println("Number of players received is: " + i);
            server.setNumberOfPlayer(i);
        }
        this.readyToPlay = true;

        while (!server.isNumberOfPlayerSet()) {
            try {
                Thread.sleep(1000);
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

}
