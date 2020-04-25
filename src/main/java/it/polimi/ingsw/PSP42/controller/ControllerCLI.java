package it.polimi.ingsw.PSP42.controller;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;


import java.util.*;

public class ControllerCLI implements ViewObserver {
    private final GameBoard g;
    private final ViewCLI view;
    private int undoCount=0;

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
        if (view.getChoice().allFieldsNull()) {
            createGame(view.getNumPlayers());
            view.setActionDone(true);
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
        }
        if (! view.getChoice().allFieldsNull()) {
            Worker w = null;
            boolean check;
            //check what worker is setting initial position
            if (view.getChoice().getW() == 1)
                w = (g.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker1();
            if (view.getChoice().getW() == 2)
                w = (g.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker2();
            //handle initial position
            check = (g.getPlayers()).get(view.getChoice().getIdPlayer()).initialPosition(view.getChoice().getX(), view.getChoice().getY(), w);
            if (check) {
                view.setActionDone(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
        }
    }

    /**
     * Handles to call the method in the model to modify the state of the worker selected
     * @param o represent always the Choice done by the user
     */
    @Override
    public void updateMove(Object o) {
        Worker w = null;
        boolean check;
        //check what worker is moving
        if (view.getChoice().getW() == 1)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getW() == 2)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker2();
        //check if worker is able to move at least in one position
        if (g.atLeastOneMove(w)) {
            check = (g.getPlayers()).get(g.getCurrentPlayer()).move(view.getChoice().getX(), view.getChoice().getY(), w);
            if (check) {
                if (view.undoOption("NOWARNING")) {
                    g.getPlayers().get(g.getCurrentPlayer()).doUndoMove(w);
                    if (g.getGamePhase().equals("PREMOVE") || g.getGamePhase().equals("PREBUILD"))
                        view.setActionDone(true);
                    GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
                    return;
                }
                view.setActionDone(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
        }
        else {
            if (view.undoOption("WARNING")) {
                doUndoPower(w, getPrecedentGamePhase());
                view.setActionDone(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
            else {
                g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()),"MOVE");
                view.setActionDone(true);
            }
        }
    }

    /**
     * Handles to call the method in the model to modify the building state of the GameBoard
     * @param o represent always the Choice done by the user
     */
    @Override
    public void updateBuild(Object o) {
        Worker w = null;
        boolean check;
        //check what worker is building
        if (view.getChoice().getW() == 1)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getW() == 2)
            w = (g.getPlayers()).get(g.getCurrentPlayer()).getWorker2();
        //check if worker is able to build at least in one position
        if (g.atLeastOneBuild(w, view.getChoice().getLevel())) {
            check = (g.getPlayers()).get(g.getCurrentPlayer()).build(view.getChoice().getX(), view.getChoice().getY(), view.getChoice().getLevel(), w);
            if (check) {
                if (view.undoOption("NOWARNING")) {
                    (g.getPlayers()).get(g.getCurrentPlayer()).doUndoBuild(w);
                    if (g.getGamePhase().equals("PREMOVE") || g.getGamePhase().equals("PREBUILD"))
                        view.setActionDone(true);
                    GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
                    return;
                }
                view.setActionDone(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
        }
        else {
            if (view.undoOption("WARNING")) {
                doUndoPower(w, getPrecedentGamePhase());
                view.setActionDone(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
            else {
                g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()),"BUILD");
                view.setActionDone(true);
            }
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
        int playersIngame = 0;
        for (Player p:g.getPlayers()) {
            if (p.getPlayerState().equals("INGAME"))
                playersIngame++;
        }
        if(playersIngame == 1) {
            view.setTurnDone(true);
            view.setGameDone(true);
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
        //Devo controllare anche che andando in move io non mi sia bloccato in seguito ad un movimento in premove e in caso
        //dire che il giocatore ha perso e far giocare il player successivo
        //Devo fare perciò ad ogni mossa la worker available del worker selezionato perchè potrebbe diventare non più disponibile

        /*if(s.equals("PREMOVE") || s.equals("MOVE"))
            g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()), "PREMOVE");

        if(s.equals("PREBUILD") || s.equals("BUILD"))
            g.loseCondition(g.getPlayers().get(g.getCurrentPlayer()), "PREBUILD");*/

        if (g.getPlayers().get(g.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
            g.setGamePhase("END");
            view.setGameState("END");
            view.loseMessage(g.getPlayers().get(g.getCurrentPlayer()).getNickname());
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            return;
        }

        if((s.equals("MOVE") || s.equals("PREBUILD")) && g.getPlayers().get(g.getCurrentPlayer()).getPlayerState().equals("WIN")) {
            g.setGamePhase("END");
            view.setGameState("END");
            view.setTurnDone(true);
            view.setGameDone(true);
            return;
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
            if(g.getGamePhase().equals("END"))
                view.printEffect("ON", g.getPlayers().get(g.getCurrentPlayer()).getCard().effectON());
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


    public void doUndoPower(Worker w, String precedentPhase){
        int current = g.getCurrentPlayer();
        Player currentPlayer = g.getPlayers().get(current);
        String[][] whatToDo = currentPlayer.getCard().getWhatToDo();
        switch (precedentPhase) {
            case "PREMOVE":
                if(whatToDo[1][0].equals("EMPTY"))
                 break;
                for (int i = 0; i < whatToDo[1].length; i++) {
                   if(whatToDo[1][i].equals("MOVE"))
                       currentPlayer.doUndoMove(w);
                   if(whatToDo[1][i].equals("BUILD"))
                       currentPlayer.doUndoBuild(w);
                }
                break;
            case "MOVE":
                currentPlayer.doUndoMove(w);
                break;
            case "PREBUILD":
                if(whatToDo[3][0].equals("EMPTY"))
                    break;
                for (int i = 0; i < whatToDo[3].length; i++) {
                    if(whatToDo[3][i].equals("MOVE"))
                        currentPlayer.doUndoMove(w);
                    if(whatToDo[3][i].equals("BUILD"))
                        currentPlayer.doUndoMove(w);
                }
                break;
            case "BUILD":
                currentPlayer.doUndoBuild(w);
                break;
        }
    }

    public String getPrecedentGamePhase(){
        String precedentPhase=null;
        String phase = g.getGamePhase();
        switch (phase){
            case "START":
                precedentPhase = "END";
            case "PREMOVE":
                precedentPhase = "START";
            case "MOVE":
                precedentPhase = "PREMOVE";
            case "PREBUILD":
                precedentPhase = "MOVE";
            case "BUILD":
                precedentPhase = "PREBUILD";
            case "END":
                precedentPhase = "BUILD";
        }
        return precedentPhase;
    }

}

