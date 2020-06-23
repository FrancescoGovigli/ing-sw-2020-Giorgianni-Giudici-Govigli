package it.polimi.ingsw.PSP42.controller;

import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.util.*;

public class ControllerHandler {

    private final GameBoard gameBoard;
    private final VirtualView view;
    private final Controller mainController;

    /**
     * Constructor to set ControllerHandler
     * @param g gameBoard
     * @param v virtualView
     * @param c controller
     */
    public ControllerHandler(GameBoard g, VirtualView v, Controller c) {
        gameBoard = g;
        view = v;
        mainController = c;
    }

    /**
     * Update method used to handle game initialization.
     * It checks user Choice.
     * If all Choice's attributes are null no choice is done and creationGame is invoked.
     * If a user choice was done it will set the initial position of the workers.
     */
    public void controlInit() {
        if (view.getChoice().allFieldsNull()) {
            mainController.createGame(view.getNumberOfPlayers());
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "INIT");
        }
        else {
            Worker w = null;
            boolean check;
            // check what worker is setting initial position
            if (view.getChoice().getWorker() == 1)
                w = (gameBoard.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker1();
            if (view.getChoice().getWorker() == 2)
                w = (gameBoard.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker2();
            // handle initial position
            check = (gameBoard.getPlayers()).get(view.getChoice().getIdPlayer()).initialPosition(view.getChoice().getX(), view.getChoice().getY(), w);
            if (check) {
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "INIT");
            }
        }
    }

    /**
     * Update method to call model's method that modifies the state of the worker selected, checking user Choice.
     */
    public void controlMove() {
        Worker w = null;
        boolean check;
        // check what worker is moving
        if (view.getChoice().getWorker() == 1)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getWorker() == 2)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker2();
        // check if worker is able to move at least in one position
        if (gameBoard.atLeastOneMove(w)) {
            check = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).move(view.getChoice().getX(), view.getChoice().getY(), w);
            if (check) {
                if (view.undoOption("NOWARNING")) {
                    gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).doUndoMove(w);
                    if (gameBoard.getGamePhase().equals("PREMOVE") || gameBoard.getGamePhase().equals("PREBUILD"))
                        view.setActionCorrect(true);
                    GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
                    return;
                }
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
            }
        }
        // this case occurs only if the player loses because player's Undo was not done
        else {
            if (view.undoOption("WARNING")) {
                controlUndoPower(w, getPreviousGamePhase());
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
            }
            else {
                gameBoard.loseCondition(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()), "MOVE");
                view.setActionCorrect(true);
            }
        }
    }

    /**
     * Update method to call model's method that modifies the state of the building selected, checking user Choice.
     */
    public void controlBuild() {
        Worker w = null;
        boolean check;
        // check what worker is building
        if (view.getChoice().getWorker() == 1)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getWorker() == 2)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker2();
        // check if worker is able to build at least in one position
        if (gameBoard.atLeastOneBuild(w, view.getChoice().getLevel())) {
            check = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).build(view.getChoice().getX(), view.getChoice().getY(), view.getChoice().getLevel(), w);
            if (check) {
                if (view.undoOption("NOWARNING")) {
                    (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).doUndoBuild(w);
                    if (gameBoard.getGamePhase().equals("PREMOVE") || gameBoard.getGamePhase().equals("PREBUILD"))
                        view.setActionCorrect(true);
                    GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
                    return;
                }
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
            }
        }
        else {
            if (view.undoOption("WARNING")) {
                controlUndoPower(w, getPreviousGamePhase());
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
            }
            else {
                gameBoard.loseCondition(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()), "BUILD");
                view.setActionCorrect(true);
            }
        }
    }

    /**
     * Update method that takes the 2 player's workers and checks if player has lost due to opponents' turn.
     * If the player has not lost the method returns his nickname.
     * If the current player is the only one remained on the board he directly wins the Game.
     * @return nickname of the Player
     */
    public String controlCurrentPlayer() {
        mainController.setTurnDone(false);
        Worker w1 = gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getWorker1();
        Worker w2 = gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getWorker2();
        // set the worker available if the worker can move or not available if it is blocked
        gameBoard.workerAvailable(w1);
        gameBoard.workerAvailable(w2);
        gameBoard.loseCondition(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()), "START");
        if (gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
            view.handleLoser(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getNickname());
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
            view.handleEnd();
            controlNextState("START");
        }
        int playersInGame = 0;
        for (Player p: gameBoard.getPlayers()) {
            if (p.getPlayerState().equals("INGAME"))
                playersInGame++;
        }
        if(playersInGame == 1) {
            mainController.setTurnDone(true);
            mainController.setGameDone(true);
        }
        view.setCurrentPlayerID(gameBoard.getCurrentPlayer());
        view.noWriteForNotCurrentPlayers(gameBoard.getCurrentPlayer());
        return gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getNickname();
    }

    /**
     * Update method to set new current player checking the arraylist of the gameBoard.
     * The new currentPlayer value will be the next of the new turn only if the Player has not lost yet.
     */
    public void controlEnd() {
        mainController.setTurnDone(true);
        int num = view.getNumberOfPlayers();
        int curr = gameBoard.getCurrentPlayer();
        if (curr + 1 < num) {
            if (gameBoard.getPlayers().get(curr + 1).getPlayerState().equals("LOSE")) {
                if (curr + 2 < num)
                    gameBoard.setCurrentPlayer(curr + 2);
                else
                    gameBoard.setCurrentPlayer(0);
            }
            else
                gameBoard.setCurrentPlayer(curr + 1);
        }
        else {
            if (gameBoard.getPlayers().get(0).getPlayerState().equals("LOSE"))
                gameBoard.setCurrentPlayer(1);
            else
                gameBoard.setCurrentPlayer(0);
        }
    }

    /**
     * Update method to update the new game Phase.
     * It checks, after every phase, if the player has lost or has won.
     * In this cases the game Phase will be set at the END of turn.
     * If the player wins, the View's GameDone value will be set true.
     * @param s string that identify the next game Phase
     */
    public void controlNextState(String s) {
        if (view.isUndoDone() && (mainController.getGameState().equals("PREMOVE") || (mainController.getGameState().equals("PREBUILD"))))
            mainController.setGameState(mainController.getGameState());
        else if (view.isUndoDone() && view.isPowerApply() && (mainController.getGameState().equals("MOVE") || mainController.getGameState().equals("BUILD"))) {
            if (mainController.getGameState().equals("MOVE"))
                mainController.setGameState("PREMOVE");
            if (mainController.getGameState().equals("BUILD"))
                mainController.setGameState("PREBUILD");
            view.setPowerApply(false);
        }
        else
            mainController.setGameState(s);
        view.setUndoDone(false);
        view.setActionCorrect(false);
        if (gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
            gameBoard.setGamePhase("END");
            mainController.setGameState("END");
            view.handleLoser(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getNickname());
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy(), "NOINIT");
            return;
        }
        if ((s.equals("MOVE") || s.equals("PREBUILD")) && gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getPlayerState().equals("WIN")) {
            gameBoard.setGamePhase("END");
            mainController.setGameState("END");
            mainController.setTurnDone(true);
            mainController.setGameDone(true);
            return;
        }
        gameBoard.setGamePhase(s);
    }

    /**
     * Update method to obtain all the possible actions during a turn.
     * These actions are contained in every Card.
     * @return matrix of strings that contains all possible actions during the turn
     */
    public String[][] controlWhatToDo() {
        int current = gameBoard.getCurrentPlayer();
        return gameBoard.getPlayers().get(current).checkWhatToDo();
    }

    /**
     * Update method to choose the right worker only if it is available during the starting phase of turn.
     * @param choice worker id choose by player
     */
    public void controlStart(Integer choice) {
        Worker w = null;
        Boolean check;
        if(choice == null)
            return;
        else if (choice == 1)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker1();
        else if (choice == 2)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker2();
        check = w.getAvailable();
        if (check)
            view.setActionCorrect(true);
        else
            view.setActionCorrect(false);
    }

    /**
     * Update method used to communicate to View to Print an effect that was applied.
     */
    public void controlEffect() {
        if (gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).effect()) {
            if(gameBoard.getGamePhase().equals("END"))
                view.printEffect("ON", gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getCard().effectON());
            if(gameBoard.getGamePhase().equals("START"))
                view.printEffect("OFF", gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getCard().effectOFF());
        }
        view.setActionCorrect(true);
    }

    /**
     * Method to call all undoAction referred to previousPhase for the worker selected.
     * @param worker worker of the current turn
     * @param previousPhase previous game phase
     */
    public void controlUndoPower(Worker worker, String previousPhase) {
        int current = gameBoard.getCurrentPlayer();
        Player currentPlayer = gameBoard.getPlayers().get(current);
        String[][] whatToDo = currentPlayer.getCard().getWhatToDo();
        switch (previousPhase) {
            case "PREMOVE":
                manageUndoPower(whatToDo, 1, currentPlayer, worker);
                break;
            case "MOVE":
                currentPlayer.doUndoMove(worker);
                break;
            case "PREBUILD":
                manageUndoPower(whatToDo, 3, currentPlayer, worker);
                break;
            case "BUILD":
                currentPlayer.doUndoBuild(worker);
                break;
        }
    }

    /**
     * Method to do the right Undo based on phase turn action.
     * @param whatToDo contains all possible actions during the turn
     * @param phaseInt 1 = PREMOVE, 3 = PREBUILD
     * @param worker Worker
     */
    private void manageUndoPower(String[][] whatToDo, int phaseInt, Player currentPlayer, Worker worker) {
        if (!whatToDo[phaseInt][0].equals("EMPTY"))
            for (int i = 0; i < whatToDo[phaseInt].length; i++) {
                if (whatToDo[phaseInt][i].equals("MOVE"))
                    currentPlayer.doUndoMove(worker);
                else if (whatToDo[phaseInt][i].equals("BUILD"))
                    currentPlayer.doUndoBuild(worker);
            }
    }

    /**
     * Utility method to know which was the previous Phase turn.
     * @return previousPhase
     */
    public String getPreviousGamePhase() {
        String previousPhase = null;
        String phase = gameBoard.getGamePhase();
        switch (phase) {
            case "START":
                previousPhase = "END";
                break;
            case "PREMOVE":
                previousPhase = "START";
                break;
            case "MOVE":
                previousPhase = "PREMOVE";
                break;
            case "PREBUILD":
                previousPhase = "MOVE";
                break;
            case "BUILD":
                previousPhase = "PREBUILD";
                break;
            case "END":
                previousPhase = "BUILD";
                break;
        }
        return previousPhase;
    }

    /**
     * Method to pick a random set of cards.
     * @param numPlayers number of players
     * @return randomPick which length is equal to numPlayers
     */
    public String[] pickCards(int numPlayers) {
        Random random = new Random();
        String[] set = DeckOfGods.possibleGods();
        String[] randomPick = new String[numPlayers];
        ArrayList<Integer> pickedRand = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            int randomIndex = random.nextInt(set.length);
            if (!pickedRand.contains(randomIndex)) {
                pickedRand.add(randomIndex);
                randomPick[i] = set[randomIndex];
            }
            else
                i--;
        }
        return randomPick;
    }
}
