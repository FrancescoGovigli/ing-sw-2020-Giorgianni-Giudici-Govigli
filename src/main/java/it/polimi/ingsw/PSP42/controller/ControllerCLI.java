package it.polimi.ingsw.PSP42.controller;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;


import java.util.*;

public class ControllerCLI implements ViewObserver {
    private final GameBoard g;
    private final ViewCLI view;

    public ControllerCLI(GameBoard model, ViewCLI v) {
        g = model;
        view = v;
    }

    /**
     * This method sets new player to the Gameboard and asks the View to set the Player names
     * @param numPlayer
     */
    public void createGame(int numPlayer) {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<UserData> data = view.getPlayerdata();
        for (int i = 0; i < view.getNumPlayers(); i++) {
            players.add(new Player(data.get(i).getNickname(), i + 1,data.get(i).getAge()));
        }
        g.setPlayers(players);
    }

    /**
     * Handles the initialization of the game.
     * @param o uses Object to identify the Choice of the user. If all the its attributes are null
     *          no choice is done and its the creationgame invoked. If there a user choice it will set
     *          the initial position of the workers
     */
    @Override
    public void updateInit(Object o) {

        if (view.getChoice().allFieldsNull())
            createGame(view.getNumPlayers());
        if (! view.getChoice().allFieldsNull()) {
            Worker w = null;
            //DEVO CHIEDERE QUALE WORKER VUOLE
            if (view.getChoice().getW() == 1)
                w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker1();
            if (view.getChoice().getW() == 2)
                w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker2();

            (g.getPlayers()).get(g.getCurrentPlayer()).initialPosition(view.getChoice().getX().intValue(), view.getChoice().getY().intValue(), w);
        }
    }

    /**
     * Handles to call the method in the model to modify the state of the worker selected
     * @param o represent always the Choice done by the user
     */
    @Override
    public void updateMove(Object o) {
        Worker w=null;
        if (view.getChoice().getW() == 1)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getW() == 2)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker2();

        (g.getPlayers()).get(g.getCurrentPlayer()).move(view.getChoice().getX().intValue(),view.getChoice().getY().intValue(),w);

    }
    /**
     * Handles to call the method in the model to modify the building state of the GameBoard
     * @param o represent always the Choice done by the user
     */
    @Override
    public void updateBuild(Object o) {
        Worker w=null;
        if (view.getChoice().getW() == 1)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getW() == 2)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker2();

        (g.getPlayers()).get(g.getCurrentPlayer()).build(view.getChoice().getX().intValue(),view.getChoice().getY().intValue(),view.getChoice().getLevel(),w);
    }

    @Override
    public String updateCurrentPlayer() {

        return  g.getPlayers().get(g.getCurrentPlayer()).getNickname();
    }

    @Override
    public void updateEnd() {
        int num = view.getNumPlayers();
        int curr = g.getCurrentPlayer();
        if(curr+1<=num)
            g.setCurrentPlayer(curr+1);
        else
            g.setCurrentPlayer(0);
    }

    @Override
    public void updateState(String s) {
        if(s.equals("START") && g.getPlayers().get(g.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
            //g.setGameState("END");
            view.setGameState("END");
            return;
        }

        if(s.equals("PREMOVE"))
            g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()),"PREMOVE");

        if(s.equals("PREBUILD"))
            g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()),"PREBUILD");


        //g.setGameState(s);
        view.setGameState(s);
    }

}

