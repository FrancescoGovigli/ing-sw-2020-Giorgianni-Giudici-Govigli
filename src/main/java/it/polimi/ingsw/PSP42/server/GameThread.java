package it.polimi.ingsw.PSP42.server;

import it.polimi.ingsw.PSP42.controller.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.util.*;

public class GameThread implements Runnable{
        private ArrayList<PlayerHandler> playingClients;
        private int numberOfPlayers;

        public GameThread(ArrayList<PlayerHandler> playingClients,int numberOfPlayers){
            this.playingClients = playingClients;
            this.numberOfPlayers = numberOfPlayers;
        }

        @Override
        public void run() {
            System.out.println("The Game is started");
            GameBoard model = GameBoard.getInstance();
            VirtualView view = new VirtualView(playingClients,numberOfPlayers);
            ControllerCLI controller = new ControllerCLI(model,view);
            view.attach(controller);
            model.attach(view);
            controller.runGame();
        }
    }

