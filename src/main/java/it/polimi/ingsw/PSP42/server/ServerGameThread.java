package it.polimi.ingsw.PSP42.server;

import it.polimi.ingsw.PSP42.controller.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerGameThread implements Runnable{

    private ArrayList<PlayerHandler> playingClients = new ArrayList<>();
    private Server server;
    private GameBoard model;
    private ControllerCLI controller;
    private PlayerHandler playerConnection;

    public PlayerHandler getPlayerConnection() {
        return playerConnection;
    }

    public String getClientNickname(){
        return playerConnection.getNickName();
    }

    public int getOrderOfConnection(){
        return playerConnection.getClientID();
    }




    public ServerGameThread(Server server,Socket client, Integer orderOfConnection){
        this.server = server;
        this.playerConnection = new PlayerHandler(client, orderOfConnection);
    }

    public ServerGameThread(ArrayList<PlayerHandler> playingClients,Server server){
        this.playingClients = playingClients;
        this.server = server;
    }



    public void startGame() {
        playingClients.sort((PlayerHandler z1, PlayerHandler z2) -> {
            if (z1.getClientID() > z2.getClientID())
                return 1;
            if (z1.getClientID() < z2.getClientID())
                return -1;
            return 0;
        });

        System.out.println("The Game is started");
        model = GameBoard.getInstance();
        VirtualView view = new VirtualView(playingClients,server.getNumberOfPlayer());
        NetworkVirtualView net = new NetworkVirtualView(this,view);
        controller = new ControllerCLI(model,view);
        view.attach(controller);
        model.attach(view);
        controller.runGame();
        if(!view.isInterrupted())
            resetGame("END");

    }
    // E' UN METODO CHE SERVE PER RESETTARE IL MODEL E IL SERVER PER POTER ACCETTARE NUOVE
    // CONNESSIONI. SE s == END allora IL GIOCO E' FINITO, SE s == INTERRUPT allora il GIOCO
    // E' STATO INTERROTTO.
    public void resetGame(String s){
        model.reset();
        if(s.equals("END")) {
            for (PlayerHandler p : playingClients) {
                p.asyncSend(ServerMessage.endGame);
                try {
                    p.getClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server.reset("END");
        }
        else if(s.equals("INTERRUPT")){
            for (PlayerHandler p : playingClients) {
                p.asyncSend(ServerMessage.inactivityEnd);
                try {
                    p.getClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server.reset("INTERRUPT");
        }

    }


    @Override
    public void run() {
        settingClient();
    }

    public void settingClient(){
        playerConnection.asyncSend("You entered the Game!"+ " \uD83D\uDE0A \n");
        if(playerConnection.getClientID()==1)
            playerConnection.asyncSend("Welcome player " + playerConnection.getClientID() + " insert your name: ");
        else
            playerConnection.asyncSend("Welcome player " + playerConnection.getClientID() +" you are waiting the FIRST PLAYER to set the number of players, insert your name: ");
        //Devo garantire univocità nickname perciò se esiste gia dovrò mandare una nuova richiesta
        String nick = (String) playerConnection.asyncRead();
        playerConnection.setNickName(nick);

        if (playerConnection.getClientID() == 1){
            int i = chooseNumberOfPlayer(nick);
            System.out.println("Number of players received is: " + i);
            server.setNumberOfPlayer(i);
        }
        playerConnection.setReadyToPlay(true);

        while (!server.isNumberOfPlayerSet()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Waiting error! " + e.getMessage());
            }
        }
        server.waitingRoom(this);

    }

    private int chooseNumberOfPlayer(String name){
        boolean correctChoice = false;
        Integer choice=null;
        String initial = (name + ", please enter the number of players: ");
        playerConnection.asyncSend(initial);
        while(!correctChoice) {
            if(choice!=null){
                initial = (name + ", please enter a correct value of players (2 or 3): ");
                playerConnection.asyncSend(initial);
            }
            choice = Integer.parseInt(playerConnection.asyncRead().toString());
            if (choice == 2 || choice == 3)
                correctChoice = true;
        }
        return choice;

    }

    public void asyncClientSend(Object message){
        playerConnection.asyncSend(message);
    }
}
