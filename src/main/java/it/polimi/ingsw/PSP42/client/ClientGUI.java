package it.polimi.ingsw.PSP42.client;

import it.polimi.ingsw.PSP42.client.clientView.ViewManager;
import it.polimi.ingsw.PSP42.server.*;
import it.polimi.ingsw.PSP42.view.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientGUI implements Runnable, ClientObservable {

    private boolean active = true;
    private boolean writeActive = true;
    private ArrayList<UserData> playersData = new ArrayList<>();
    private ClientObserver clientObserver;
    private String input;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public ArrayList<UserData> getPlayersList() {
        return (ArrayList<UserData>) playersData.clone();
    }

    /**
     * Method to obtain a player's data.
     * @param nickname of the interested player
     * @return userData if the player is present, null otherwise
     */
    public UserData getPlayerData(String nickname){
        for (UserData userData : playersData) {
            if (userData.getNickname().equals(nickname))
                return userData;
        }
        return null;
    }

    public void run() {
        Socket server = null;
        Boolean correctHostIP = false;
        while (!correctHostIP) {
            try {
                notifyConnectionStart();
                String hostIP = input;
                server = new Socket(hostIP, 4000);
                input = null;
                System.out.println("Connection established");
                correctHostIP = true;
            } catch (IOException e) {
                String error = "Server unreachable";
                ViewManager.hostIPIncorrect(error);
                System.out.println(error);
            }
        }
        ObjectInputStream socketIn = null;
        PrintWriter socketOut = null;
        try {
            socketIn = new ObjectInputStream(server.getInputStream());
            socketOut = new PrintWriter(server.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException in ClientGUI -> run (creation of ObjectInputStream & PrintWriter)");
        }
        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(socketOut);
            t0.join();
            t1.join();
        } catch (InterruptedException | NoSuchElementException e) {
            System.out.println("Connection closed from Client side");
        } finally {
            try {
                socketIn.close();
                socketOut.close();
                server.close();
            } catch (IOException e) {
                System.out.println("IOException in ClientGUI -> run (finally branch)");
            }
        }
    }

    /**
     * Method to send an object to the Server.
     * @param socketOut output
     * @return (the main thread will wait for the operation to be completed)
     */
    public Thread asyncWriteToSocket(final PrintWriter socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        if (writeActive && input != null) {
                            socketOut.println(input);
                            socketOut.flush();
                            input = null;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("You disconnected");
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    /**
     * Method to receive an object from the Server.
     * @param socketIn input
     * @return (the main thread will wait for the operation to be completed)
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if (inputObject instanceof String) {
                            if (isCloseMessage(inputObject)) {
                                System.out.println("[FROM SERVER] : " + inputObject);
                                elaborateMessage(inputObject);
                                socketIn.close();
                                setActive(false);
                                System.out.println("[FROM SERVER] : PRESS [ENTER] TO QUIT");
                            }
                            else {
                                System.out.println("[FROM SERVER] : " + inputObject);
                                elaborateMessage(inputObject);
                            }
                        }
                        else if (inputObject instanceof Boolean)
                            writeActive = (Boolean)inputObject;
                        else if (inputObject instanceof UserData) {
                            playersData.add(((UserData) inputObject));
                        }
                        else if (inputObject instanceof List) {
                            showGods(inputObject);
                            elaborateMessage(inputObject);
                        }
                        else
                            notifyShow(inputObject);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("IOException | ClassNotFoundException in ClientGUI -> asyncReadFromSocket");
                    System.out.println("Disconnected");
                    setActive(false);
                }
            }
        });
        thread.start();
        return thread;
    }

    /**
     * Method used to call the correct methods for ClientCLI observer.
     * @param message object to understand the message from Server
     */
    public void elaborateMessage(Object message) {
        if (message instanceof String) {
            if (message.equals("Welcome player 1 insert your name: "))
                notifyWelcomeFirstPlayer();
            else if (((String) message).contains("you are waiting the FIRST PLAYER to set the number of players, insert your name: "))
                notifyWelcomeOtherPlayers();
            else if (message.equals(ServerMessage.waiting) || message.equals(ViewMessage.waitingOpponentPick))
                notifyWaiting();
            else if (message.equals(ServerMessage.extraClient) || message.equals(ServerMessage.gameInProgress) || message.equals(ServerMessage.nameNotFree))
                notifyGameStatus(message);
            else if (!(message.equals(ServerMessage.enteredGame)) && !((String) message).contains("please enter the number of players"))
                notifyGameMessage(message);
        }
        else if (message instanceof List)
            notifyGodSelection(message);
    }

    /**
     * Used to save locally input from GUI.
     * @param input string obtained from GUI's command
     */
    public void saveInput(String input){
        this.input = input;
    }

    /**
     * Used to print listOfGods.
     * @param listOfGods list of strings containing name of gods sent from server
     */
    public void showGods(Object listOfGods) {
        for (int i = 0; i < ((List<String>)listOfGods).size(); i++) {
            System.out.println(((List<String>)listOfGods).get(i));
        }
    }

    /**
     * Method to check if a message invites to close or not.
     * @param object (message to check)
     * @return true if motivation involves closure, false otherwise
     */
    private boolean isCloseMessage(Object object) {
        return object.equals(ServerMessage.gameEnd) ||
               object.equals(ServerMessage.inactivityEnd) ||
               object.equals(ServerMessage.disconnectionEnd) ||
               object.equals(ServerMessage.gameInProgress) ||
               object.equals(ServerMessage.extraClient) ||
               ((String) object).contains(ViewMessage.personalLoseMessage);
    }

    /**
     * Used to attach a Client observer (ViewManager) to ClientGUI.
     * @param ob ClientObserver
     */
    @Override
    public void attach(ClientObserver ob) {
        clientObserver = ob;
    }

    /**
     * Used to notify Client observer when first player is added.
     */
    @Override
    public void notifyWelcomeFirstPlayer() {
        clientObserver.updateWelcomeFirstPlayer();
    }

    /**
     * Used to notify Client observer when an other player is added.
     */
    @Override
    public void notifyWelcomeOtherPlayers() {
        clientObserver.updateWelcomeOtherPlayers();
    }

    /**
     * Used to notify Client that he is connected to Server until he press play.
     */
    @Override
    public void notifyConnectionStart() {
        clientObserver.updateConnectionStart();
    }

    /**
     * Used to notify a player to wait his turn.
     */
    @Override
    public void notifyWaiting() {
        clientObserver.updateWaiting();
    }

    /**
     * Used to notify a player the possibility to choose his god.
     * @param listOfGods possible god to choose from the list
     */
    @Override
    public void notifyGodSelection(Object listOfGods) {
        clientObserver.updateGodSelection(listOfGods);
    }

    /**
     * Used to notify a player the status of game.
     * @param o status message
     */
    @Override
    public void notifyGameStatus(Object o) {
        clientObserver.updateGameStatus(o);
    }

    /**
     * Used to notify a player the updating of the gameBoard.
     * @param o gameBoard updated
     */
    @Override
    public void notifyShow(Object o) {
        clientObserver.updateShow(o);
    }

    /**
     * Used to notify a player a game message.
     * @param message notified message
     */
    @Override
    public void notifyGameMessage(Object message) {
        clientObserver.updateGameMessage(message);
    }
}
