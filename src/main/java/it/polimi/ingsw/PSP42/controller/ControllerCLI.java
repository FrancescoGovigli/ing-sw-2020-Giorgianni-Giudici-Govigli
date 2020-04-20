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
        ArrayList<UserData> data = view.getPlayerdata(pickCards(numPlayer));
        for (int i = 0; i < view.getNumPlayers(); i++) {
            players.add(new Player(data.get(i).getNickname(), i + 1,data.get(i).getAge(),data.get(i).getCardChoosed()));
        }
        /*Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                int value=0;
                if (o2.getAge() < o1.getAge())
                    value = - 1;
                else if (o2.getAge() == o1.getAge())
                    value = 0;
                else if (o2.getAge() > o1.getAge())
                    value = 1;
                return value;
            }
        });*/
        g.setPlayers(players);
        g.setGamePhase("START");
    }

    /**
     * Handles the initialization of the game.
     * @param o uses Object to identify the Choice of the user. If all the its attributes are null
     *          no choice is done and its the creationgame invoked. If there a user choice it will set
     *          the initial position of the workers
     */
    @Override
    public void updateInit(Object o) {
        if (view.getChoice().allFieldsNull()){
            createGame(view.getNumPlayers());
            view.setActionDone(true);
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
        }
        if (!view.getChoice().allFieldsNull()) {
            Worker w = null;
            //DEVO CHIEDERE QUALE WORKER VUOLE
            if (view.getChoice().getW() == 1)
                w = (g.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker1();
            if (view.getChoice().getW() == 2)
                w = (g.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker2();
            boolean check = (g.getPlayers()).get(view.getChoice().getIdPlayer()).initialPosition(view.getChoice().getX().intValue(), view.getChoice().getY().intValue(), w);
            if(check) {
                view.setActionDone(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
        }
    }
    /*TODO DEVO GESTIRE IL WORKER NON AVAILABLE RICHIEDI IN CASO, SE LA MOSSA è ERRATA RICHIEDO INPUT
     */

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
        boolean check = (g.getPlayers()).get(g.getCurrentPlayer()).move(view.getChoice().getX().intValue(),view.getChoice().getY().intValue(),w);
        if(check) {
            view.setActionDone(true);
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
        }
        if(g.getPlayers().get(g.getCurrentPlayer()).getPlayerState().equals("WIN")) {
            view.setTurnDone(true);
            view.setGameDone(true);
        }
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
        boolean check = (g.getPlayers()).get(g.getCurrentPlayer()).build(view.getChoice().getX().intValue(),view.getChoice().getY().intValue(),view.getChoice().getLevel(),w);
        if(check) {
            view.setActionDone(true);
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
        }
    }

    @Override
    public String updateCurrentPlayer() {
        Worker w1 = g.getPlayers().get(g.getCurrentPlayer()).getWorker1();
        Worker w2 = g.getPlayers().get(g.getCurrentPlayer()).getWorker2();
        boolean check1 = g.workerAvailable(w1);
        boolean check2 = g.workerAvailable(w2);
        if(!check1 && !check2) {
            g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()),"START");
            if (g.getPlayers().get(g.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
                view.setGameState("END");
                view.loseMessage(g.getPlayers().get(g.getCurrentPlayer()).getNickname());
            }
        }
        return  g.getPlayers().get(g.getCurrentPlayer()).getNickname();
    }

    @Override
    public void updateEnd() {
        int num = view.getNumPlayers();
        int curr = g.getCurrentPlayer();
        if(curr+1<num) {
            if (g.getPlayers().get(curr + 1).getPlayerState().equals("LOSE")) {
                if (curr + 2 < num)
                    g.setCurrentPlayer(curr + 2);
                else
                    g.setCurrentPlayer(0);
            }
            else
                g.setCurrentPlayer(curr+1);
        }
        else {
            if (g.getPlayers().get(0).getPlayerState().equals("LOSE"))
                g.setCurrentPlayer(1);
            else
                g.setCurrentPlayer(0);
        }
    }

    @Override
    public void updateState(String s) {
        if(s.equals("PREMOVE")) {
            g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()), "PREMOVE");
            if (g.getPlayers().get(g.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
                g.setGamePhase("END");
                view.setGameState("END");
                view.loseMessage(g.getPlayers().get(g.getCurrentPlayer()).getNickname());
                return;
            }
        }
        if(s.equals("PREBUILD")) {
            g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()), "PREBUILD");
            if (g.getPlayers().get(g.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
                g.setGamePhase("END");
                view.setGameState("END");
                view.loseMessage(g.getPlayers().get(g.getCurrentPlayer()).getNickname());
                return;
            }
        }
        g.setGamePhase(s);
        view.setGameState(s);
    }

    @Override
    public String[][] updateWhatToDo() {
        int current = g.getCurrentPlayer();
        return g.getPlayers().get(current).checkWhatTodo();
    }

    @Override
    public int updateStart() {
        int choice = view.getWorker();
        Worker w=null;
        if (choice == 1)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker1();
        if (choice == 2)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker2();
        boolean check = w.getAvailable();
        if (check)
            view.setActionDone(true);
        return choice;
    }

    @Override
    public void updateEffect() {
        if (g.getPlayers().get(g.getCurrentPlayer()).effect()) {
        //se vero e sei in END allora messaggio "Potere attivato"
            if(g.getGamePhase().equals("END"))
                view.printEffect("ON", g.getPlayers().get(g.getCurrentPlayer()).getCard().effectON());
        //se vero e sei in START allora messaggio "Potere disattivato"
            if(g.getGamePhase().equals("START"))
                view.printEffect("OFF", g.getPlayers().get(g.getCurrentPlayer()).getCard().effectOFF());
        }
        view.setActionDone(true);
        //GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
    }

    public String[] pickCards(int numPlayers){
        Random rand = new Random();
        String[] set = DeckOfGods.possibleGods();
        String[] randomPick = new String[numPlayers];
        ArrayList<Integer> pickedRand = new ArrayList<>();

        for (int i = 0; i < numPlayers; i++) {
            int randomIndex = rand.nextInt(set.length);
            if(!pickedRand.contains(randomIndex)) {
                pickedRand.add(randomIndex);
                randomPick[i] = set[randomIndex];
            }
            else
                i--;
        }
        return randomPick;
    }

}

