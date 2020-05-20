package it.polimi.ingsw.PSP42.server;

import it.polimi.ingsw.PSP42.controller.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerGameThread implements Runnable {

    private ArrayList<ClientHandler> playingClients = new ArrayList<>();
    private Server server;
    private GameBoard model;
    private ControllerCLI controller;
    private ClientHandler playerConnection;

    public ClientHandler getPlayerConnection() {
        return playerConnection;
    }

    public String getClientNickname(){
        return playerConnection.getNickName();
    }

    public int getOrderOfConnection(){
        return playerConnection.getClientID();
    }

    /**
     * Constructor for the thread that will manage the client until the game starts
     * @param server
     * @param client
     * @param orderOfConnection
     */
    public ServerGameThread(Server server, Socket client, Integer orderOfConnection) {
        this.server = server;
        this.playerConnection = new ClientHandler(client, orderOfConnection);
    }

    /**
     * Constructor for the thread that will create the game
     * @param playingClients
     * @param server
     */
    public ServerGameThread(ArrayList<ClientHandler> playingClients, Server server) {
        this.playingClients = playingClients;
        this.server = server;
    }

    /**
     * Method to create all the components necessary to play and finally launch the game
     */
    public void startGame() {
        playingClients.sort((ClientHandler z1, ClientHandler z2) -> {
            if (z1.getClientID() > z2.getClientID())
                return 1;
            if (z1.getClientID() < z2.getClientID())
                return -1;
            return 0;
        });
        System.out.println("The Game is started");
        model = GameBoard.getInstance();
        VirtualView view = new VirtualView(playingClients, server.getNumberOfPlayer());
        NetworkVirtualView net = new NetworkVirtualView(this, view);
        controller = new ControllerCLI(model, view);
        view.attach(controller);
        model.attach(view);
        controller.runGame();
        if(!view.isInterrupted())
            resetGame("END");
    }


    /**
     * Method to reset the game (model and server, to be able to accept new connections)
     * @param s (string identifying why the game should be reset)
     */
    public void resetGame(String s) {
        model.reset();
        if (s.equals("END")) {
            for (ClientHandler p : playingClients) {
                p.sendToClient(ServerMessage.endGame);
                try {
                    p.getClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server.reset("END");
        }
        else if (s.equals("INTERRUPT")) {
            for (ClientHandler p : playingClients) {
                p.sendToClient(ServerMessage.inactivityEnd);
                try {
                    p.getClient().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server.reset("INTERRUPT");
        }

    }

    /**
     * Method executed by threads that manage clients
     */
    @Override
    public void run() {
        settingClient();
    }

    /**
     * Method to welcome the client, ask for his name (and the number of players if he is the first to connect),
     * make him ready to play and put him on hold until the server has reached the number of players needed to play
     */
    public void settingClient() {
        playerConnection.sendToClient("You entered the Game!" + " \uD83D\uDE0A \n");
        if (playerConnection.getClientID() == 1)
            playerConnection.sendToClient("Welcome player " + playerConnection.getClientID() + " insert your name: ");
        else
            playerConnection.sendToClient("Welcome player " + playerConnection.getClientID() + " you are waiting the FIRST PLAYER to set the number of players, insert your name: ");
        //TODO guarantee uniqueness of the name
        String nick = (String) playerConnection.readFromClient();
        playerConnection.setNickName(nick);

        if (playerConnection.getClientID() == 1) {
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

    /**
     * Method for setting the number of players (executed by the first connected client)
     * @param name of who will perform the setting
     * @return choice (the set number)
     */
    private int chooseNumberOfPlayer(String name) {
        boolean correctChoice = false;
        Integer choice = null;
        String initial = (name + ", please enter the number of players: ");
        playerConnection.sendToClient(initial);
        while (!correctChoice) {
            if (choice != null) {
                initial = (name + ", please enter a correct value of players (2 or 3): ");
                playerConnection.sendToClient(initial);
            }
            choice = Integer.parseInt(playerConnection.readFromClient().toString());
            if (choice == 2 || choice == 3)
                correctChoice = true;
        }
        return choice;
    }

    /**
     * Method to send an object to a possible future player, that is in waiting to play
     * @param message (object to send)
     */
    public void sendToWaitingClient(Object message){
        playerConnection.sendToClient(message);
    }
}
