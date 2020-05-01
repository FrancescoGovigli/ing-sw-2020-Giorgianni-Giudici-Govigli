package it.polimi.ingsw.PSP42.server;

import java.util.ArrayList;

public class GameThread implements Runnable{
    private ArrayList<PlayerHandler> playingClients;

    public GameThread(ArrayList<PlayerHandler> playingClients){
        this.playingClients = playingClients;
    }

    @Override
    public void run() {
        System.out.println("Hi, i'm GameThread!");

    }
}
