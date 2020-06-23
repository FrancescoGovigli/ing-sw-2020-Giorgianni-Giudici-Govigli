package it.polimi.ingsw.PSP42.view;

import java.util.*;

import it.polimi.ingsw.PSP42.model.ModelObserver;
import it.polimi.ingsw.PSP42.server.*;

public class VirtualView implements ViewObservable, ModelObserver {

    private int numberOfPlayers;
    private int currentPlayerID;
    private Choice choice;
    private boolean actionCorrect;
    private boolean undoDone;
    private boolean powerApply;
    private boolean interrupted;
    private ArrayList<ViewObserver> obs = new ArrayList<>();
    private ArrayList<ClientHandler> playingClients;

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setCurrentPlayerID(int currentPlayerID) {
        this.currentPlayerID = currentPlayerID;
    }

    /**
     * Getter to obtain a player Choice.
     * Every choice (move or build) made by a player is saved in a Choice object.
     * @return choice
     */
    public Choice getChoice(){
        return choice;
    }

    /**
     * This method is used to set the boolean attribute actionCorrect,
     * which determines the end of an action done by the current player, so the GamePhase can move on.
     * @param value true if action is correct, false otherwise
     */
    public void setActionCorrect(boolean value){
        actionCorrect = value;
    }

    public boolean isUndoDone(){
        return undoDone;
    }

    public void setUndoDone(boolean value){
        undoDone = value;
    }

    public boolean isPowerApply(){
        return powerApply;
    }

    /**
     * This method is used to set the boolean attribute powerApply,
     * which determines that the player is going to use the power.
     * @param value true if player wants to apply the power, false otherwise
     */
    public void setPowerApply(boolean value) {
        powerApply = value;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    /**
     * Constructor to set VirtualView.
     * @param playingClients number of player in game as ArrayList<ClientHandler>
     * @param numberOfPlayers number of player
     */
    public VirtualView(ArrayList<ClientHandler> playingClients, int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.currentPlayerID = 0;
        this.actionCorrect = false;
        this.playingClients = playingClients;
    }

    /**
     * Method that helps the Controller to ask for the players data during the creation of game.
     * The method uses a utility class called UserData which contains {Nickname, GodCard}.
     * @param set is an Array of string containing the set of Gods picked randomly, set.length = number of Players
     * @return ArrayList<UserData>  player names given through System.in
     */
    public ArrayList<UserData> getPlayerData(String[] set) {
        setActionCorrect(false);
        List<String> setOfCards = new LinkedList<String>(Arrays.asList(set));
        ArrayList<UserData> players = new ArrayList<>();
        for (int i = 0; i < playingClients.size(); i++) {
            noWriteForNotCurrentPlayers(i);
            boolean choiceDone = false;
            String selectedCard = null;
            while (!choiceDone && !interrupted) {
                sendToPlayerInGame(i, ViewMessage.selectCard + (i + 1));
                for (int j = 0; j < playingClients.size(); j++) {
                    if (j != i)
                        sendToPlayerInGame(j, ViewMessage.waitingOpponentPick);
                }
                sendToPlayerInGame(i, setOfCards);
                selectedCard = (String) readFromPlayerInGame(playingClients.get(i));
                if (!isReadFromPlayerInGameOK(selectedCard))
                    return null;
                if (setOfCards.contains(selectedCard.toUpperCase())) {
                    setOfCards.remove(selectedCard.toUpperCase());
                    choiceDone = true;
                }
            }
            for (int j = 0; j < playingClients.size(); j++) {
                if (j != i)
                    sendToPlayerInGame(j, playingClients.get(i).getNickName() + " picked " + selectedCard.toUpperCase() + "\n");
            }
            players.add(new UserData(playingClients.get(i).getNickName(), selectedCard.toUpperCase()));
            sendUserDataToPlayerInGame(new UserData(playingClients.get(i).getNickName(), selectedCard.toUpperCase()));
            setActionCorrect(false);
        }
        return players;
    }

    /**
     * Method used by the Controller to ask which worker to use during the turn.
     * @return worker, 1 if its the worker1 , 2 if its the worker2 of the player
     */
    public Integer getWorker() {
        Integer worker = null;
        boolean correct = false;
        while (!correct && !interrupted) {
            sendToPlayerInGame(currentPlayerID, ViewMessage.workerMessage);
            try {
                String input = (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
                if (!isReadFromPlayerInGameOK(input))
                    return null;
                worker =  Integer.parseInt(input);
                if (worker == 1 || worker == 2)
                    correct = true;
            }
            catch (InputMismatchException | NumberFormatException e) {
                sendToPlayerInGame(currentPlayerID, ErrorMessage.inputMessage + "\n");
            }
        }
        return worker;
    }

    /**
     * Handle method used to send to all players welcome message.
     */
    public void handleWelcomeMessage() {
        noWriteForNotCurrentPlayers(currentPlayerID);
        for (int i = 0; i < playingClients.size(); i++)
            sendToPlayerInGame(i, ViewMessage.welcome);
    }

    /**
     * Method used to ignore input from not current player.
     * @param currentPlayer current player id
     */
    public void noWriteForNotCurrentPlayers(Integer currentPlayer) {
        for (int i = 0; i < playingClients.size(); i++) {
            if (!interrupted) {
                if (i != currentPlayer)
                    sendToPlayerInGame(i, false);
                else
                    sendToPlayerInGame(i, true);
            }
        }
    }

    /**
     * Handle method used to initialize the GameBoard by ControllerHandler.
     */
    public void handleInit() {
        if (!interrupted) {
            choice = new Choice(null, null, null, null, null);
            notifyInit();
        }
    }

    /**
     * Handle method used to ask to user to insert coordinates for his 2 workers and notify observers to set this data.
     */
    public void handleInitialPosition() {
        for (int i = 0; i < numberOfPlayers; i++) {
            noWriteForNotCurrentPlayers(i);
            for (int j = 0; j < 2; j++) {
                String[] s = null;
                for (int k = 0; k < playingClients.size(); k++)
                    if (k != i)
                        sendToPlayerInGame(k, ViewMessage.waitingPositioning);
                while (!actionCorrect && !interrupted) {
                    sendToPlayerInGame(i, "Player " + (i + 1) + ", " + ViewMessage.initialPositionMessage + (j + 1) + " (digit x,y)");
                    try {
                        String input = (String) readFromPlayerInGame(playingClients.get(i));
                        if (!isReadFromPlayerInGameOK(input))
                            break;
                        s = input.split(",");
                        choice = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), j + 1, null, i);
                        notifyInit();
                    }
                    catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        sendToPlayerInGame(i, ErrorMessage.inputMessage + "\n");
                    }
                }
                setActionCorrect(false);
            }
        }
        setActionCorrect(false);
    }

    /**
     * Handle method that gives to view the information about all the action that a playerCard can perform.
     * @param s player nickname
     * @return array of String arrays with (START, PREMOVE, MOVE, PREBUILD, BUILD, END)
     */
    public String[][] handleWhatToDo(String s) {
        setActionCorrect(false);
        sendToPlayerInGame(currentPlayerID, "\n" + s + ", " + ViewMessage.yourTurnMessage + "\n");
        for (int i = 0; i < playingClients.size(); i++) {
            if (i != currentPlayerID)
                sendToPlayerInGame(i, playingClients.get(currentPlayerID).getNickName() + " is playing, " + ViewMessage.waitingYourTurn);
        }
        return notifyWhatToDo();
    }

    /**
     * Handle method used to ask to choose one of the 2 available workers of currentPlayer.
     * @return 1 = worker1, 2 = worker2
     */
    public Integer handleStart() {
        Integer worker = null;
        setActionCorrect(false);
        while (!actionCorrect && !interrupted) {
            worker = getWorker();
            notifyStart(worker);
        }
        return worker;
    }

    /**
     * Handle method to notify observer that has to handle a move action from player.
     * @param worker is an integer that tells which one of the two worker are selected by player
     */
    public void handleMove(Integer worker) {
        setActionCorrect(false);
        while (!actionCorrect && !interrupted) {
            String[] s;
            sendToPlayerInGame(currentPlayerID, ViewMessage.moveMessage);
            try {
                String input = (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
                if (!isReadFromPlayerInGameOK(input))
                    break;
                s = input.split(",");
                choice = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), worker, null, null);
                notifyMove();
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                sendToPlayerInGame(currentPlayerID, ErrorMessage.inputMessage + "\n");
            }
        }
    }

    /**
     * Handle method to notify observer that has to handle a build action from player.
     * @param worker is an integer that tells which one of the two worker are selected by player
     *               (its the same one called from the move action)
     */
    public void handleBuild(Integer worker) {
        int counter = 0;
        setActionCorrect(false);
        while (!actionCorrect && !interrupted) {
            try {
                if (counter == 0)
                    sendToPlayerInGame(currentPlayerID, ViewMessage.buildMessage + worker);
                String[] b;
                String build = (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
                if (!isReadFromPlayerInGameOK(build))
                    break;
                b = build.split(",");
                if (!build.equals("")) {
                    counter = 0;
                    sendToPlayerInGame(currentPlayerID, ViewMessage.levelMessage);
                    String answer = (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
                    if (!isReadFromPlayerInGameOK(answer))
                        break;
                    if (answer.toUpperCase().equals("YES")) {
                        sendToPlayerInGame(currentPlayerID, ViewMessage.insertLevel);
                        String input =  (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
                        if (!isReadFromPlayerInGameOK(input))
                            break;
                        Integer level =  Integer.parseInt(input);
                        choice = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, level, null);
                    }
                    else {
                        choice = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, 0, null);
                    }
                    notifyBuild();
                }
                else
                    counter = 1;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                sendToPlayerInGame(currentPlayerID, ErrorMessage.inputMessage + "\n");
            }
        }
    }

    /**
     * Method used to show/manage Undo function accessible to the player.
     * It has an internal timer and ask if the current player wants to apply Undo Option within 5 seconds.
     * @param warning its a string that can be only (NOWARNING, WARNING) and tells the player that if he doesn't
     *                use undo function he will lose the game
     * @return true if the undo is applied
     */
    public boolean undoOption(String warning) {
        final boolean[] value = {true};
        String str = "";
        final String[] finalStr = {str};
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (finalStr[0].equals("")) {
                    sendToPlayerInGame(currentPlayerID, "You input nothing. UNDO wasn't done...");
                    value[0] = false;
                }
            }
        };
        timer.schedule(task, 5000);
        if (warning.equals("WARNING"))
            sendToPlayerInGame(currentPlayerID, ErrorMessage.blockingMessage);
        sendToPlayerInGame(currentPlayerID, "Input a YES within 5 seconds for UNDO : ");
        String action = (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
        if (!isReadFromPlayerInGameOK(action))
            return false;
        timer.cancel();
        if (value[0] == false || (!action.toUpperCase().equals("YES")) || action.equals("")) {
            sendToPlayerInGame(currentPlayerID, "UNDO is not applied");
            undoDone = false;
            return false;
        }
        if (action.toUpperCase().equals("YES")) {
            sendToPlayerInGame(currentPlayerID, "UNDO is applied");
            undoDone = true;
            return true;
        }
        else
            return false;
    }

    /**
     * Handle method to notify the observers about an effect, that involves all players, has been applied (not move/build effect).
     */
    public void handleEffect(){
        notifyEffect();
    }

    /**
     * Method used to send to players that an effect started or ended.
     * @param s start or end effect
     * @param effect effect type
     */
    public void printEffect(String s, String effect) {
        if (s.equals("ON"))
            sendToPlayerInGame(currentPlayerID, ViewMessage.godPowerStart + effect);
        if (s.equals("OFF"))
            sendToPlayerInGame(currentPlayerID, ViewMessage.godPowerEnd + effect);
    }

    /**
     * Handle method to notify to controller to change next currentPlayer.
     */
    public void handleEnd() {
        setActionCorrect(false);
        powerApply = false;
        notifyEnd();
    }

    /**
     * Handle method used to send to all players win message.
     * @param winner player
     */
    public void handleWinner(String winner) {
        for (int i = 0; i <playingClients.size(); i++) {
            if (i != currentPlayerID)
                sendToPlayerInGame(i, winner + " " + ViewMessage.winMessage);
            else
                sendToPlayerInGame(currentPlayerID, winner + " " + ViewMessage.personalWinMessage);
        }
    }

    /**
     * Used to send to all players lose message.
     * @param loser player
     */
    public void handleLoser(String loser) {
        for (int i = 0; i < playingClients.size(); i++) {
            if (playingClients.get(i).getNickName().equals(loser))
                sendToPlayerInGame(i, loser + " " + ViewMessage.personalLoseMessage);
            else
                sendToPlayerInGame(i, loser + " " + ViewMessage.loseMessage);
        }
    }

    /**
     * Handle method used to notify to controller the game interruption.
     */
    public void handleInterrupt() {
        interrupted = true;
        notifyInterrupt();
    }

    /**
     * Utility method to call the correct handle method.
     * @param s String (MOVE, BUILD, EFFECT) that identify corresponding handle method to call
     * @param worker integer that identifies the worker who will do move/build action
     */
    public void callPowerFunction(String s, Integer worker) {
        boolean askUndo = false;
        switch (s) {
            case "MOVE":
                while (!askUndo && !interrupted) {
                    sendToPlayerInGame(currentPlayerID, s + " POWER : " + ViewMessage.applyPowerMessage);
                    String input = (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
                    if (!isReadFromPlayerInGameOK(input))
                        break;
                    if (input.toUpperCase().equals("YES")) {
                        handleMove(worker);
                        setPowerApply(true);
                        askUndo = true;
                    }
                    else {
                        if (!undoOption("NOWARNING"))
                            askUndo = true;
                        else
                            setActionCorrect(false);
                    }
                }
                break;
            case "BUILD":
                while (!askUndo && !interrupted) {
                    sendToPlayerInGame(currentPlayerID, s + " POWER : " + ViewMessage.applyPowerMessage);
                    String input = (String) readFromPlayerInGame(playingClients.get(currentPlayerID));
                    if (!isReadFromPlayerInGameOK(input))
                        break;
                    if (input.toUpperCase().equals("YES")) {
                        handleBuild(worker);
                        setPowerApply(true);
                        askUndo = true;
                    }
                    else {
                        if (!undoOption("NOWARNING"))
                            askUndo = true;
                        else
                            setActionCorrect(false);
                    }
                }
                break;
            case "EFFECT":
                handleEffect();
                break;
        }
    }

    /**
     * Used to send to all players the update GameBoard after a player action.
     * @param object GameBoard
     * @param string used to inform other players about the current player action
     */
    @Override
    public void updateBoard(Object object, String string) {
        for (int i = 0; i < playingClients.size(); i++) {
            if (!string.equals("INIT")) {
                if (i != currentPlayerID)
                    sendToPlayerInGame(i, playingClients.get(currentPlayerID).getNickName() + " did his move");
            }
            sendToPlayerInGame(i, object);
        }
    }

    /**
     * Method used to inform player in game.
     * @param clientToSend player id
     * @param message Object
     */
    public void sendToPlayerInGame(Integer clientToSend, Object message) {
        if (!interrupted)
            NetworkVirtualView.sendToPlayer(playingClients.get(clientToSend), message);
    }

    /**
     * Method used to receive from player in game.
     * @param player ClientHandler
     * @return fromPlayer (what current player sent)
     */
    public Object readFromPlayerInGame(ClientHandler player) {
        return NetworkVirtualView.readFromPlayer(player);
    }

    /**
     * Method to verify that what has been received by the Client is correct,
     * if it is incorrect game is interrupted.
     * @param object to be verified
     * @return true if the object exists (it isn't null), false otherwise
     */
    private boolean isReadFromPlayerInGameOK(Object object) {
        if (object == null)
            return false;
        return true;
    }

    /**
     * Method used to send UserData to player in game.
     * It is useful for clients to know other players data.
     * @param data UserData
     */
    public void sendUserDataToPlayerInGame(UserData data) {
        if (!interrupted) {
            for (int i = 0; i < playingClients.size(); i++)
                sendToPlayerInGame(i, data);
        }
    }

    /**
     * Add an observer to the View's observer list
     * @param ob ViewObserver
     */
    @Override
    public void attach(ViewObserver ob) {
        obs.add(ob);
    }

    /**
     * Notifies all observers about game initialization by view.
     */
    @Override
    public void notifyInit() {
        for (int i = 0; i < obs.size(); i++)
            obs.get(i).updateInit();
    }

    /**
     * Notifies all observers about the action to do during turn.
     * @return matrix contains all possible action
     */
    @Override
    public String[][] notifyWhatToDo() {
        return obs.get(0).updateWhatToDo();
    }

    /**
     * Notifies all observers that user start his turn.
     * @param i worker id chosen by player (1 = worker1 or 2 = worker2)
     */
    @Override
    public void notifyStart(Integer i) {
        obs.get(0).updateStart(i);
    }

    /**
     * Notifies all observers that user selected a move coordinate.
     */
    @Override
    public void notifyMove() {
        for (int i = 0; i < obs.size(); i++)
            obs.get(i).updateMove();
    }

    /**
     * Notifies all observers that user selected a build coordinate.
     */
    @Override
    public void notifyBuild() {
        for (int i = 0; i < obs.size(); i++)
            obs.get(i).updateBuild();
    }

    /**
     * Notifies all observers that an effect was activated.
     */
    @Override
    public void notifyEffect() {
        obs.get(0).updateEffect();
    }

    /**
     * Notifies all observers that user end his turn.
     */
    @Override
    public void notifyEnd() {
        obs.get(0).updateEnd();
    }

    /**
     * Notifies all observers that game was interrupted.
     */
    @Override
    public void notifyInterrupt() {
        obs.get(0).updateInterrupt();
    }
}
