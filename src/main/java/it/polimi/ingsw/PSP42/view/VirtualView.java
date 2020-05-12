package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import it.polimi.ingsw.PSP42.model.FakeCell;
import it.polimi.ingsw.PSP42.server.*;

/**
 * @author Francesco Govigli
 */
public class VirtualView implements ViewObservable, ModelObserver {
    private boolean actionCorrect;
    private boolean undoDone;
    private boolean powerApply;
    private ArrayList<ViewObserver> obs = new ArrayList<>();
    private ArrayList<PlayerHandler> playingClients;
    private int numPlayers;
    private Choice choice;
    private int currentPlayerID;
    private boolean interrupted;

    public boolean isInterrupted() {
        return interrupted;
    }



    public void setCurrentPlayerID(int currentPlayerID) {
        this.currentPlayerID = currentPlayerID;
    }



    public VirtualView(ArrayList<PlayerHandler> playingClients,int numberOfPlayers) {
        numPlayers = numberOfPlayers;
        actionCorrect = false;
        this.playingClients = playingClients;
        this.currentPlayerID=0;
    }

    public boolean isPowerApply(){
        return powerApply;
    }

    public void setUndoDone(boolean value){
        undoDone= value;
    }

    public boolean isUndoDone(){
        return undoDone;
    }

    /**
     * This method is used to set the boolean value actionDone, which determines
     * the end of an action choosed by the current player, so that the Gamephase can move on.
     * @param value
     */
    public void setActionCorrect(boolean value){
        actionCorrect = value;
    }

    /**
     * This method is used to set the boolean value GameDone, which determines
     * the end of the game (only if a player wins).
     * @param value
     */
    public void setPowerApply(boolean value){ powerApply=value;}

    /**
     * Every choice (move or build) made by a player is saved in the Choice Class
     * @return c
     */
    public Choice getChoice(){
        return choice;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * This method helps the Controller to ask for the players data during the creation of game
     * The method uses a utility class called Userdata which contains {Nickname,Age,GodCard}
     * @param set is an Array of string containing the set of Gods picked randomly, set.length = number of Players
     * @return ArrayList<UserData>  player names given through System.in
     */
    public ArrayList<UserData> getPlayerData(String[] set){
        setActionCorrect(false);
        List<String> setOfCards = new LinkedList<String>(Arrays.asList(set));
        setOfCards.add("NOGOD");
        ArrayList<UserData> players = new ArrayList<>();
        for (int i = 0; i < playingClients.size() ; i++) {
            noWriteForNotCurrentPlayers(i);
            boolean choiceDone=false;
            String selectedCard = null;
            while(!choiceDone && !interrupted) {
                sendToPlayer(i,ViewMessage.selectCard + (i + 1)+"\n");

                for (int j = 0; j < playingClients.size() ; j++)
                    if(j!=i)
                        sendToPlayer(j,ViewMessage.waitingOpponentPick);


                for (int j = 0; j < setOfCards.size(); j++)
                    sendToPlayer(i,setOfCards.get(j));


                selectedCard = (String) receiveFromPlayer(playingClients.get(i));
                if(selectedCard == null) {
                    return null;
                }


                if (setOfCards.contains(selectedCard.toUpperCase())) {
                    if(!selectedCard.toUpperCase().equals("NOGOD"))
                        setOfCards.remove(selectedCard.toUpperCase());

                    choiceDone = true;
                }
            }
            for (int j = 0; j <playingClients.size() ; j++) {
                if(j!=i)
                    sendToPlayer(j,playingClients.get(i).getNickName()+ " picked "+selectedCard.toUpperCase()+"\n");
            }

            players.add(new UserData(playingClients.get(i).getNickName(),21,selectedCard.toUpperCase()));
            sendUserDataToClients(new UserData(playingClients.get(i).getNickName(),21,selectedCard.toUpperCase()));
            setActionCorrect(false);

        }


        return players;
    }
    /**
     * this method is used by the controller to ask for the worker to use during the turn
     * @return worker, 1 if its the worker1 , 2 if its the worker2 of the player
     */
    public Integer getWorker(){
        Integer worker=null;
        boolean correct=false;
        while(!correct && !interrupted) {
            sendToPlayer(currentPlayerID,ViewMessage.workerMessage+"\n");
            try {
                String input = (String)receiveFromPlayer(playingClients.get(currentPlayerID));
                if(input == null)
                    return null;

                worker =  Integer.parseInt(input);
                if (worker == 1 || worker == 2)
                    correct = true;
            }
            catch(InputMismatchException | NumberFormatException e){
                sendToPlayer(currentPlayerID,ErrorMessage.inputMessage+"\n");
            }
        }
        return worker;
    }

    /**
     * Add an observer to the View's observer list
     *
     * @param ob
     */
    @Override
    public void attach(ViewObserver ob) {
        obs.add(ob);
    }

    /**
     * Removes an observer to the View's observer list
     *
     * @param ob
     */
    @Override
    public void detach(ViewObserver ob) {
        obs.remove(ob);
    }

    /**
     *notifies all observers that the view is initializing the game
     * @param o
     */
    @Override
    public void notifyInit(Object o) {
        for (int i = 0; i < obs.size() ; i++)
            obs.get(i).updateInit(o);
    }

    @Override
    public Integer notifyStart(Integer i) {
        return obs.get(0).updateStart(i);
    }

    @Override
    public String[][] notifyWhatToDo() {
        return obs.get(0).updateWhatToDo();
    }

    /**
     * notifies all observers that the user selected a move coordinate
     * @param o
     */
    @Override
    public void notifyMove(Object o) {
        for (int i = 0; i <obs.size() ; i++)
            obs.get(i).updateMove(o);
    }

    /**
     * notifies all observers that the user selected a build coordinate
     * @param o
     */
    @Override
    public void notifyBuild(Object o) {
        for (int i = 0; i <obs.size() ; i++)
            obs.get(i).updateBuild(o);
    }

    @Override
    public void notifyEffect() {
        obs.get(0).updateEffect();
    }

    @Override
    public void notifyInterrupt() {
        obs.get(0).updateInterrupt();
    }

    @Override
    public void notifyEnd() {
        obs.get(0).updateEnd();
    }

    @Override
    public void updateBoard(Object o,String s) {
        for (int i = 0; i <playingClients.size(); i++) {
            if(!s.equals("INIT")) {
                if (i != currentPlayerID)
                    sendToPlayer(i," "+playingClients.get(currentPlayerID).getNickName() + " did his move");
            }
            sendToPlayer(i,o);
        }
    }

    public void handleWelcomeMessage() {
        noWriteForNotCurrentPlayers(currentPlayerID);
        for (int i = 0; i < playingClients.size(); i++)
            sendToPlayer(i, ViewMessage.welcome);


    }

    public void handleWinner(String winner){
        for (int i = 0; i <playingClients.size() ; i++) {
            if(i!=currentPlayerID)
                sendToPlayer(i,winner + " "+ViewMessage.winMessage);
            else
                sendToPlayer(currentPlayerID,winner +" "+ ViewMessage.personalWinMessage);
        }

    }

    public void handleLoser(String loser){
        for (int i = 0; i <playingClients.size() ; i++) {
            if(i!=currentPlayerID)
                sendToPlayer(i,loser + " "+ViewMessage.loseMessage);
            else
                sendToPlayer(currentPlayerID,loser +" "+ ViewMessage.personalLoseMessage);
        }

    }
    
    /**
     * This method has the task to initialize the Gameboard and set the initial players position.
     */
    public void handleInit(){
        if(!interrupted)
            notifyInit(choice = new Choice(null,null,null,null,null));
    }

    /**
     * Has the task to ask the user to insert the two coordinates for his 2 workers and notify observers
     * to set this data.
     */
    public void handleInitialPosition(){
        for (int i = 0; i < numPlayers; i++) {
            noWriteForNotCurrentPlayers(i);
            for (int j = 0; j <2; j++) {
                String[] s = null;

                for(int k = 0; k < playingClients.size() ; k++)
                    if(k!=i)
                        sendToPlayer(k,ViewMessage.waitingPositioning);

                while (!actionCorrect && !interrupted) {

                    sendToPlayer(i,"Player " + (i + 1) + ", " + ViewMessage.initialPositionMessage + (j + 1) + " (digit x,y)"+"\n");
                    try{
                        String input = (String) receiveFromPlayer(playingClients.get(i));
                        if(input == null)
                            break;

                        s = input.split(",");
                        notifyInit(choice = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), j + 1, null, i));
                    }
                    catch (NumberFormatException e) {
                        sendToPlayer(i,ErrorMessage.inputMessage + "\n");
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        sendToPlayer(i,ErrorMessage.inputMessage + "\n");
                    }

                }
                setActionCorrect(false);
            }
        }
        setActionCorrect(false);
    }

    /**
     * this method is used to ask to choose one of the 2 worker of the currentPlayer available
     * @return 1 for worker1, 2 for worker2
     */
    public Integer handleStart() {
        Integer worker=null;
        setActionCorrect(false);
        while(!actionCorrect && !interrupted) {
            worker = getWorker();
            notifyStart(worker);
        }
        return worker;
    }

    /**
     * It's a handle method that gives the view the information about all the action that a playerCard can perform
     * @return array of String arrays with {START,PREMOVE,MOVE,PREBUILD,BUILD,END}
     */
    public String[][] handleWhatToDo(String s) {
        setActionCorrect(false);

        sendToPlayer(currentPlayerID,"\n" + s+", " + ViewMessage.yourTurnMessage+"\n");
        for (int i = 0; i <playingClients.size() ; i++) {
            if(i!=currentPlayerID)
                sendToPlayer(i,ViewMessage.waitingYourTurn);
        }
        return notifyWhatToDo();
    }

    /**
     * It's a handle method to notify observer that have to handle a move action from the user.
     * @param worker is an integer that tells which of the two worker are selected from the user
     */
    public void handleMove(Integer worker){
        setActionCorrect(false);
        while(!actionCorrect && !interrupted) {
            String[] s;
            sendToPlayer(currentPlayerID,ViewMessage.moveMessage);
            try{
                String input = (String)receiveFromPlayer(playingClients.get(currentPlayerID));
                if(input == null)
                    break;

                s = input.split(",");
                notifyMove(choice = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), worker, null,null));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
                sendToPlayer(currentPlayerID,ErrorMessage.inputMessage + "\n");
            }
        }
    }

    /**
     * It's a handle method to notify observer that have to handle a build action from the user.
     * @param worker is an integer that tells which of the two worker are selected from the user
     *               its the same one called from the move action
     */
    public void handleBuild(Integer worker){
        int counter = 0;
        setActionCorrect(false);
        while(!actionCorrect && !interrupted) {
            try {
                if(counter == 0)
                    sendToPlayer(currentPlayerID,ViewMessage.buildMessage + worker + "\n");

                String[] b;
                String build = (String)receiveFromPlayer(playingClients.get(currentPlayerID));
                if(build == null)
                    break;

                b = build.split(",");
                if (!build.equals("")) {
                    counter = 0;
                    sendToPlayer(currentPlayerID,ViewMessage.levelMessage + "\n");
                    String answer = (String)receiveFromPlayer(playingClients.get(currentPlayerID));
                    if(answer == null)
                        break;
                    if (answer.toUpperCase().equals("YES")) {
                        sendToPlayer(currentPlayerID,ViewMessage.insertLevel + "\n");
                        String input =  (String)receiveFromPlayer(playingClients.get(currentPlayerID));
                        if(input == null)
                            break;
                        Integer level =  Integer.parseInt(input);

                        notifyBuild(choice = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, level, null));
                    } else
                        notifyBuild(choice = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, 0, null));
                }
                else
                    counter = 1;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e){
                sendToPlayer(currentPlayerID,ErrorMessage.inputMessage + "\n");
            }
        }
    }

    /**
     * its a handle method to notify the observers that an effect has been applied (not a move, not a build effect)
     */
    public void handleEffect(){
        notifyEffect();
    }

    /**
     * It's a method that tells a player that an effect(not a explicit power) is applied
     * @param s
     * @param effect
     */
    public void printEffect(String s, String effect) {
        if(s.equals("ON"))
            sendToPlayer(currentPlayerID,ViewMessage.godPowerStart + effect);
        if(s.equals("OFF"))
            sendToPlayer(currentPlayerID,ViewMessage.godPowerEnd + effect);
    }

    /**
     * only set the turn to done and will notify the controller to change to the next currentPlayer
     * */
    public void handleEnd(){
        setActionCorrect(false);
        powerApply=false;
        notifyEnd();
    }



    /**
     * Calls an internal timer and ask if the current player wants to apply Undo Option within 5 seconds
     * @param warning its a string that can be only {"NOWARNING","WARNING"} and tells the player that if he doesn't
     *                do the undo action he will lose the game
     * @return true if the undo is applied
     */
    public boolean undoOption(String warning) {
        final boolean[] value = {true};
        String str = "";
        final String[] finalStr = {str};
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if(finalStr[0].equals("")) {
                    sendToPlayer(currentPlayerID,"You input nothing. UNDO wasn't done...");
                    value[0] = false;
                }
            }
        };
        timer.schedule(task, 5000);
        if(warning.equals("WARNING"))
            sendToPlayer(currentPlayerID,ErrorMessage.blockingMessage);
        sendToPlayer(currentPlayerID, "Input a YES within 5 seconds for UNDO : ");
        String action = (String)receiveFromPlayer(playingClients.get(currentPlayerID));
        if(action == null)
            return false;

        timer.cancel();
        if(value[0]==false || (!action.toUpperCase().equals("YES")) || action.equals("")) {
            sendToPlayer(currentPlayerID,"UNDO is not applied");
            undoDone = false;
            return false;
        }
        if(action.toUpperCase().equals("YES")) {
            sendToPlayer(currentPlayerID,"UNDO is applied");
            undoDone = true;
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * Utility method to call the correct handle method
     * @param s says if handle function must me of type : MOVE, BUILD OR EFFECT
     * @param worker all the actions (not the effect) are referred to a worker that does it
     */
    public boolean callPowerFunction(String s, Integer worker){
        boolean askUndo=false;
        switch (s) {
            case "MOVE":
                while(!askUndo && !interrupted) {
                    sendToPlayer(currentPlayerID,s + " POWER: " + ViewMessage.applyPowerMessage);
                    String input = (String)receiveFromPlayer(playingClients.get(currentPlayerID));
                    if(input == null)
                        break;

                    if (input.toUpperCase().equals("YES")) {
                        handleMove(worker);
                        setPowerApply(true);
                        askUndo=true;
                    } else {
                        if(!undoOption("NOWARNING"))
                            askUndo=true;
                        else
                            setActionCorrect(false);
                    }

                }
                break;
            case "BUILD":
                while(!askUndo && !interrupted) {
                    sendToPlayer(currentPlayerID,s + " POWER: " + ViewMessage.applyPowerMessage);
                    String input = (String)receiveFromPlayer(playingClients.get(currentPlayerID));
                    if(input == null)
                        break;

                    if (input.toUpperCase().equals("YES")) {
                        handleBuild(worker);
                        setPowerApply(true);
                        askUndo=true;
                    } else {
                        if(!undoOption("NOWARNING"))
                            askUndo=true;
                        else
                            setActionCorrect(false);
                    }

                }
                break;
            case "EFFECT":
                handleEffect();
                break;
        }
        return actionCorrect;
    }


    public void sendToPlayer(Integer clientToSend,Object message){
        if(!interrupted)
            NetworkVirtualView.sendToClient(playingClients.get(clientToSend),message);
    }

    public Object receiveFromPlayer(PlayerHandler player){
        Object fromPlayer = NetworkVirtualView.receiveFromClient(player);
        return fromPlayer;

    }

    public void sendUserDataToClients(UserData data) {
        if(!interrupted) {
            for (int i = 0; i < playingClients.size(); i++)
                sendToPlayer(i, data);
        }
    }

    public void noWriteForNotCurrentPlayers(Integer currentPlayer){
        for (int i = 0; i <playingClients.size() ; i++) {
            if (! interrupted) {
                if (i != currentPlayer)
                    sendToPlayer(i, false);
                else
                    sendToPlayer(i, true);
            }
        }
    }

    public void handleInterrupt(){
        interrupted=true;
        notifyInterrupt();
    }

}