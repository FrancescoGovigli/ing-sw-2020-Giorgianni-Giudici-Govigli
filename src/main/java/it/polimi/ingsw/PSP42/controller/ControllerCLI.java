package it.polimi.ingsw.PSP42.controller;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.util.*;

/**
 * @author Francesco Govigli
 */
public class ControllerCLI implements ViewObserver {
    
    private final GameBoard g;
    private final VirtualView view;
    private final ControllerHandler handler;
    private String gameState;
    private boolean gameDone;
    private boolean turnDone;
    private boolean actionDone;

    public ControllerCLI(GameBoard model, VirtualView v) {
        g = model;
        view = v;
        handler = new ControllerHandler(g, v, this);
    }

    public void setGameState(String s){
        gameState = s;
    }

    public String getGameState() {
        return gameState;
    }

    /**
     * This method sets new player to the Gameboard and asks the View to set the Player names
     * @param numPlayer
     */
    public void createGame(int numPlayer) {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<UserData> data = view.getPlayerData(handler.pickCards(numPlayer));
        if (data!=null) {
            for (int i = 0; i < view.getNumPlayers(); i++) {
                players.add(new Player(data.get(i).getNickname(), i + 1, data.get(i).getCardChoosed()));
            }
            g.setPlayers(players);
        }
        g.setGamePhase("START");
        gameState = "START";
    }

    /**
     * * The method used to start the game and handle a turn
     */
    public void runGame() {
        view.handleWelcomeMessage();
        view.handleInit();
        view.handleInitialPosition();

        while (!isGameDone()) {
            Integer worker = null;
            String[][] whatToDo = null;
            String name = handler.controlCurrentPlayer();
            while (!isTurnDone()) {
                if (gameState.equals("START")) {
                    whatToDo = view.handleWhatToDo(name);
                }
                switch (gameState) {
                    case "START":
                        while (!isActionDone()) {
                            if (whatToDo[0][0].equals("EMPTY"))
                                setActionDone(true);
                            else {
                                for (int i = 0; i < whatToDo[0].length; i++) {
                                    String move = whatToDo[0][i];
                                    if(view.callPowerFunction(move, worker))
                                        setActionDone(true);
                                    else
                                        setActionDone(false);
                                }
                            }
                        }
                        handler.controlNextState("PREMOVE");
                        worker = view.handleStart();
                        break;
                    case "PREMOVE":
                        if (whatToDo[1][0].equals("EMPTY"))
                            setActionDone(true);
                        else {
                            for (int i = 0; i < whatToDo[1].length; i++) {
                                String move = whatToDo[1][i];
                                if(view.callPowerFunction(move, worker))
                                    setActionDone(true);
                                else
                                    setActionDone(false);
                            }
                        }
                        handler.controlNextState("MOVE");
                        break;
                    case "MOVE":
                        view.handleMove(worker);
                        handler.controlNextState("PREBUILD");
                        break;
                    case "PREBUILD":
                        if (whatToDo[3][0].equals("EMPTY"))
                            setActionDone(true);
                        else {
                            for (int i = 0; i < whatToDo[3].length; i++) {
                                String move = whatToDo[3][i];
                                if (view.callPowerFunction(move, worker))
                                    setActionDone(true);
                                else
                                    setActionDone(false);
                            }
                        }
                        handler.controlNextState("BUILD");
                        break;
                    case "BUILD":
                        view.handleBuild(worker);
                        handler.controlNextState("END");
                        break;
                    case "END":
                        if (whatToDo[5][0].equals("EMPTY"))
                            setActionDone(true);
                        else {
                            for (int i = 0; i < whatToDo[5].length; i++) {
                                String move = whatToDo[5][i];
                                if(view.callPowerFunction(move, worker))
                                    setActionDone(true);
                                else
                                    setActionDone(false);
                            }
                        }
                        view.handleEnd();
                        handler.controlNextState("START");
                        break;
                }
            }
        }
        if (!view.isInterrupted()) {
            String winner = handler.controlCurrentPlayer();
            view.handleWinner(winner);
        }
    }

    /**
     * Handles the initialization of the game.
     * @param o uses Object to identify the Choice of the user. If all the its attributes are null
     *          no choice is done and its the creationgame invoked. If there a user choice it will set
     *          the initial position of the workers
     */
    @Override
    public void updateInit(Object o) {
        handler.controlInit(o);
    }

    @Override
    public void updateMove(Object o){
        handler.controlMove(o);
    }

    /**
     * Handles to call the method in the model to modify the building state of the GameBoard
     * @param o represent always the Choice done by the user
     */
    @Override
    public void updateBuild(Object o) {
        handler.controlBuild(o);
    }

    /**
     * Sets the new current player checking the arraylist of the gameboard.
     * The new currentPlayer value will be the next of the new turn only if the Player has not lost yet
     */
    @Override
    public void updateEnd() {
        handler.controlEnd();
    }

    /**
     * Every Card know the Action that are going to happen during the Player, who has the specific Card
     * The method gives the values to the View to know which action to call and in with phase of the turn.
     * @return
     */
    @Override
    public String[][] updateWhatToDo() {
        return handler.controlWhatToDo();
    }

    /**
     * Update method to choose the right worker only if available during the starting phase of turn
     * @return 1 == worker1, 2==worker2;
     */
    @Override
    public Integer updateStart(Integer i) {
        return handler.controlStart(i);
    }

    /**
     * Says the view to Print that effect(not explicit power) is applied
     */
    @Override
    public void updateEffect() {
        handler.controlEffect();
    }

    @Override
    public void updateInterrupt() {
        gameDone = true;
        turnDone = true;
    }

    public boolean isGameDone() {
        return gameDone;
    }

    public void setGameDone(boolean gameDone) {
        this.gameDone = gameDone;
    }

    public boolean isTurnDone() {
        return turnDone;
    }

    public void setTurnDone(boolean turnDone) {
        this.turnDone = turnDone;
    }

    public boolean isActionDone() {
        return actionDone;
    }

    public void setActionDone(boolean actionDone) {
        this.actionDone = actionDone;
    }
}
