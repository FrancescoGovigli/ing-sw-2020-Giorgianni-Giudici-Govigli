package it.polimi.ingsw.PSP42.controller;

import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.util.*;

public class ControllerHandler {

    private final GameBoard gameBoard;
    private final VirtualView view;
    private final ControllerCLI mainController;

    public ControllerHandler(GameBoard g, VirtualView v,ControllerCLI c) {
        gameBoard = g;
        view = v;
        mainController = c;
    }

    /**
     * Handles the initialization of the game.
     * @param o uses Object to identify the Choice of the user. If all the its attributes are null
     *          no choice is done and its the creationgame invoked. If there a user choice it will set
     *          the initial position of the workers
     */
    public void controlInit(Object o) {
        if (view.getChoice().allFieldsNull()) {
            mainController.createGame(view.getNumPlayers());
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
        }
        if (! view.getChoice().allFieldsNull()) {
            Worker w = null;
            boolean check;
            //check what worker is setting initial position
            if (view.getChoice().getWorker() == 1)
                w = (gameBoard.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker1();
            if (view.getChoice().getWorker() == 2)
                w = (gameBoard.getPlayers()).get(view.getChoice().getIdPlayer()).getWorker2();
            //handle initial position
            check = (gameBoard.getPlayers()).get(view.getChoice().getIdPlayer()).initialPosition(view.getChoice().getX(), view.getChoice().getY(), w);
            if (check) {
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
        }
    }
    /**
     * Handles to call the method in the model to modify the state of the worker selected
     * @param o represent always the Choice done by the user
     */
    public void controlMove(Object o) {
        Worker w = null;
        boolean check;
        //check what worker is moving
        if (view.getChoice().getWorker() == 1)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getWorker() == 2)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker2();
        //check if worker is able to move at least in one position
        if (gameBoard.atLeastOneMove(w)) {
            check = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).move(view.getChoice().getX(), view.getChoice().getY(), w);
            if (check) {
                if (view.undoOption("NOWARNING")) {
                    gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).doUndoMove(w);
                    if (gameBoard.getGamePhase().equals("PREMOVE") || gameBoard.getGamePhase().equals("PREBUILD"))
                        view.setActionCorrect(true);
                    GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
                    return;
                }
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
        }
        //THIS CASE OCCURS ONLY IF THE PLAYER LOSES IF NO UNDO IS DONE
        else {
            if (view.undoOption("WARNING")) {
                controlUndoPower(w, getPreviousGamePhase());
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
            else {
                gameBoard.loseCondition(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()),"MOVE");
                view.setActionCorrect(true);
            }
        }
    }

    /**
     * Handles to call the method in the model to modify the building state of the GameBoard
     * @param o represent always the Choice done by the user
     */

    public void controlBuild(Object o) {
        Worker w = null;
        boolean check;
        //check what worker is building
        if (view.getChoice().getWorker() == 1)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker1();
        if (view.getChoice().getWorker() == 2)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker2();
        //check if worker is able to build at least in one position
        if (gameBoard.atLeastOneBuild(w, view.getChoice().getLevel())) {
            check = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).build(view.getChoice().getX(), view.getChoice().getY(), view.getChoice().getLevel(), w);
            if (check) {
                if (view.undoOption("NOWARNING")) {
                    (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).doUndoBuild(w);
                    if (gameBoard.getGamePhase().equals("PREMOVE") || gameBoard.getGamePhase().equals("PREBUILD"))
                        view.setActionCorrect(true);
                    GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
                    return;
                }
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
        }
        else {
            if (view.undoOption("WARNING")) {
                controlUndoPower(w, getPreviousGamePhase());
                view.setActionCorrect(true);
                GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            }
            else {
                gameBoard.loseCondition(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()),"BUILD");
                view.setActionCorrect(true);
            }
        }
    }

    /**
     * Takes the 2 workers of the player and checks if the player has lost due to the opponents.
     * If the player has not lost the method returns his nickname. If the current player is the only one remaining
     * on the board he directly wins the Game
     * @return nickname of the Player
     */

    public String controlCurrentPlayer() {
        mainController.setTurnDone(false);
        Worker w1 = gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getWorker1();
        Worker w2 = gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getWorker2();
        //SET THE WORKER AVAILABLE IF THE WORKER CAN MOVE OR NOT AVAILABLE IF BLOCKED
        gameBoard.workerAvailable(w1);
        gameBoard.workerAvailable(w2);
        gameBoard.loseCondition(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()),"START");
        if (gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getPlayerState().equals("LOSE")) {
            ViewMessage.loseMessage(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getNickname());
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
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
        return  gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getNickname();
    }

    /**
     * Sets the new current player checking the arraylist of the gameboard.
     * The new currentPlayer value will be the next of the new turn only if the Player has not lost yet
     */

    public void controlEnd() {
        mainController.setTurnDone(true);
        int num = view.getNumPlayers();
        int curr = gameBoard.getCurrentPlayer();
        if(curr+1<num) {
            if (gameBoard.getPlayers().get(curr + 1).getPlayerState().equals("LOSE")) {
                if (curr + 2 < num)
                    gameBoard.setCurrentPlayer(curr + 2);
                else
                    gameBoard.setCurrentPlayer(0);
            }
            else
                gameBoard.setCurrentPlayer(curr+1);
        }
        else {
            if (gameBoard.getPlayers().get(0).getPlayerState().equals("LOSE"))
                gameBoard.setCurrentPlayer(1);
            else
                gameBoard.setCurrentPlayer(0);
        }
    }

    /**
     * Updates the new Phase of the game.
     * It checks if after every phase if the player has the LOSE value or if the player has won. In this cases the Phase
     * will be set at the END of turn. If the player WIN the GameDone value of the View will be set to true.
     * @param s
     */
    public void controlNextState(String s) {

        mainController.setActionDone(false);
        if(view.isUndoDone() && (mainController.getGameState().equals("PREMOVE") || (mainController.getGameState().equals("PREBUILD"))))
            mainController.setGameState(mainController.getGameState());
        else if(view.isUndoDone() && view.isPowerApply() && (mainController.getGameState().equals("MOVE") || mainController.getGameState().equals("BUILD"))){
            if(mainController.getGameState().equals("MOVE"))
                mainController.setGameState("PREMOVE");
            if(mainController.getGameState().equals("BUILD"))
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
            ViewMessage.loseMessage(gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getNickname());
            GameBoard.getInstance().notifyObservers(FakeCell.getGameBoardCopy());
            return;
        }

        if((s.equals("MOVE") || s.equals("PREBUILD")) && gameBoard.getPlayers().get(gameBoard.getCurrentPlayer()).getPlayerState().equals("WIN")) {
            gameBoard.setGamePhase("END");
            mainController.setGameState("END");
            mainController.setTurnDone(true);
            mainController.setGameDone(true);
            return;
        }

        gameBoard.setGamePhase(s);
    }

    /**
     * Every Card know the Action that are going to happen during the Player, who has the specific Card
     * The method gives the values to the View to know which action to call and in with phase of the turn.
     * @return
     */
    public String[][] controlWhatToDo() {
        int current = gameBoard.getCurrentPlayer();
        return gameBoard.getPlayers().get(current).checkWhatToDo();
    }

    /**
     * Update method to choose the right worker only if available during the starting phase of turn
     * @return 1 == worker1, 2 == worker2;
     */
    public int controlStart(Integer i) {
        int choice = i;
        Worker w = null;
        if (choice == 1)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker1();
        if (choice == 2)
            w = (gameBoard.getPlayers()).get(gameBoard.getCurrentPlayer()).getWorker2();
        boolean check = w.getAvailable();
        if (check)
            view.setActionCorrect(true);
        else
            view.setActionCorrect(false);
        return choice;
    }

    /**
     * Says the view to Print that effect (not explicit power) is applied
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
     * Generic function to call all undoAction referred to the previousPhase for worker w selected.
     * @param w worker of the current turn
     * @param previousPhase
     */
    public void controlUndoPower(Worker w, String previousPhase){
        int current = gameBoard.getCurrentPlayer();
        Player currentPlayer = gameBoard.getPlayers().get(current);
        String[][] whatToDo = currentPlayer.getCard().getWhatToDo();
        switch (previousPhase) {
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
                        currentPlayer.doUndoBuild(w);
                }
                break;
            case "BUILD":
                currentPlayer.doUndoBuild(w);
                break;
        }
    }

    /**
     * Utility method to know, which was the previous Phase of the Turn
     * @return
     */
    public String getPreviousGamePhase(){
        String previousPhase=null;
        String phase = gameBoard.getGamePhase();
        switch (phase){
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
     * Pick a set of cards randomly string[].length = numPlayers
     * @param numPlayers
     * @return randomPick
     */
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
