package it.polimi.ingsw.PSP42.controller;

import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.util.*;

public class Controller implements ViewObserver {

    private final GameBoard g;
    private final VirtualView view;
    private final ControllerHandler handler;
    private String gameState;
    private boolean gameDone;
    private boolean turnDone;

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String s){
        gameState = s;
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

    /**
     * Constructor to set Controller.
     * @param model gameBoard
     * @param v virtualView
     */
    public Controller(GameBoard model, VirtualView v) {
        g = model;
        view = v;
        handler = new ControllerHandler(g, v, this);
    }

    /**
     * This method sets new player to the GameBoard and asks the View to set the Player names.
     * @param numPlayer
     */
    public void createGame(int numPlayer) {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<UserData> data = view.getPlayerData(handler.pickCards(numPlayer));
        if (data != null) {
            for (int i = 0; i < view.getNumberOfPlayers(); i++) {
                players.add(new Player(data.get(i).getNickname(), i + 1, data.get(i).getCardChosen()));
            }
            g.setPlayers(players);
        }
        g.setGamePhase("START");
        gameState = "START";
    }

    /**
     * The method is used to start the game and handle a turn.
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
                        manageGamePhase(whatToDo, 0, worker);
                        handler.controlNextState("PREMOVE");
                        worker = view.handleStart();
                        break;
                    case "PREMOVE":
                        manageGamePhase(whatToDo, 1, worker);
                        handler.controlNextState("MOVE");
                        break;
                    case "MOVE":
                        view.handleMove(worker);
                        handler.controlNextState("PREBUILD");
                        break;
                    case "PREBUILD":
                        manageGamePhase(whatToDo, 3, worker);
                        handler.controlNextState("BUILD");
                        break;
                    case "BUILD":
                        view.handleBuild(worker);
                        handler.controlNextState("END");
                        break;
                    case "END":
                        manageGamePhase(whatToDo, 5, worker);
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
     * Method to show the right action message based on the turn phase.
     * @param whatToDo contains all possible actions during the turn
     * @param phaseInt  0 = START, 1 = PREMOVE, 3 = PREBUILD, 5 = END
     * @param worker 1 = worker1, 2 = worker2
     */
    private void manageGamePhase(String[][] whatToDo, int phaseInt, Integer worker) {
        if (!whatToDo[phaseInt][0].equals("EMPTY"))
            for (int i = 0; i < whatToDo[phaseInt].length; i++) {
                String move = whatToDo[phaseInt][i];
                view.callPowerFunction(move, worker);
            }
    }

    /**
     * Used to initialize the game.
     * @see ControllerHandler -> controlInit()
     */
    @Override
    public void updateInit() {
        handler.controlInit();
    }

    /**
     * Every Card knows the Actions that are going to happen during the Player's turn.
     * The method gives the values to the View to know which action to call and in which phase of the turn.
     * @return actions to do during the Player's turn
     */
    @Override
    public String[][] updateWhatToDo() {
        return handler.controlWhatToDo();
    }

    /**
     * Update method to choose the right worker only if it is available during the starting phase of turn.
     * @param i worker id chosen by the player (1 = worker1 or 2 = worker2)
     */
    @Override
    public void updateStart(Integer i) {
        handler.controlStart(i);
    }

    /**
     * Used to call the method in the model to modify the worker's position on GameBoard.
     * @see ControllerHandler -> controlMove()
     */
    @Override
    public void updateMove() {
        handler.controlMove();
    }

    /**
     * Used to call the method in the model to modify the building' state of GameBoard.
     * @see ControllerHandler -> controlBuild()
     */
    @Override
    public void updateBuild() {
        handler.controlBuild();
    }

    /**
     * Used to communicate to View to Print an effect that was applied.
     */
    @Override
    public void updateEffect() {
        handler.controlEffect();
    }

    /**
     * Used to set the new current player due to the end of former current player.
     * The new currentPlayer value will be the next of the new turn only if the Player has not lost yet.
     */
    @Override
    public void updateEnd() {
        handler.controlEnd();
    }

    /**
     * Used to set param to end game.
     */
    @Override
    public void updateInterrupt() {
        gameDone = true;
        turnDone = true;
    }
}
