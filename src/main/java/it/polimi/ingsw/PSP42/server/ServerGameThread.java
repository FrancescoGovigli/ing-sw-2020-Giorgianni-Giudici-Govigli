package it.polimi.ingsw.PSP42.server;

import it.polimi.ingsw.PSP42.controller.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.net.*;
import java.util.*;

public class ServerGameThread implements Runnable{

        private ArrayList<PlayerHandler> playingClients = new ArrayList<>();
        private Server server;
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




        public ServerGameThread(Server server, Socket client, Integer orderOfConnection){
            this.server = server;
            this.playerConnection = new PlayerHandler(client, orderOfConnection);
        }

        public ServerGameThread(ArrayList<PlayerHandler> playingClients, Server server){
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
            GameBoard model = GameBoard.getInstance();
            if(playingClients ==null)
                System.out.println("lista null");
            else
             System.out.println(playingClients.size());
            for (PlayerHandler p : playingClients)
                System.out.println(p.getNickName());
            System.out.println(server.getNumberOfPlayer());
            VirtualView view = new VirtualView(playingClients,server.getNumberOfPlayer());
            ControllerCLI controller = new ControllerCLI(model,view);
            view.attach(controller);
            model.attach(view);
            controller.runGame();
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

