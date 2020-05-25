package it.polimi.ingsw.PSP42.client;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.server.*;
import it.polimi.ingsw.PSP42.view.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientGUI implements Runnable, ClientObservable {
    private boolean active=true;
    private boolean writeActive=true;
    private boolean elaborating=false;
    private ArrayList<UserData> playersData = new ArrayList<>();
    private ClientObserver clientObserver;
    String input;

    public UserData getPlayerData(String nickname){
        for (UserData u:playersData) {
            if(u.getNickname().equals(nickname))
                return u;
        }
        return null;
    }

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive() && !elaborating) {
                        Object inputObject = socketIn.readObject();
                        if(inputObject instanceof String){
                            if(((String) inputObject).contains(ViewMessage.personalLoseMessage)) {
                                System.out.println("[FROM SERVER] : " + inputObject);
                                System.out.println("Game closed, PRESS [ENTER] TO QUIT...");
                                socketIn.close();
                                setActive(false);
                            }
                            else if(!inputObject.equals(ServerMessage.extraClient) && !inputObject.equals(ServerMessage.gameInProgress) && !inputObject.equals(ServerMessage.endGame) && !inputObject.equals(ServerMessage.inactivityEnd)) {
                                System.out.println("[FROM SERVER] : " + inputObject);
                                elaborateMessage(inputObject);
                            }
                            else {
                                System.out.println("[FROM SERVER] : "+inputObject);
                                elaborateMessage(inputObject);
                                socketIn.close();
                                setActive(false);
                                System.out.println("[FROM SERVER] : PRESS [ENTER] TO QUIT ");
                            }
                        }
                        else if(inputObject instanceof Boolean)
                            writeActive=(Boolean)inputObject;
                        else if(inputObject instanceof UserData)
                            playersData.add(((UserData) inputObject));
                        else if(inputObject instanceof List) {
                            showGods(inputObject);
                            elaborateMessage(inputObject);
                        }
                        else
                            notifyShow(inputObject);
                    }
                } catch (IOException | ClassNotFoundException e){
                    e.printStackTrace();
                    System.out.println("uscito dalla connessione");
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(final BufferedReader stdin, final PrintWriter socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        if(writeActive && input!=null) {
                            socketOut.println(input);
                            socketOut.flush();
                            input=null;
                        }
                    }
                } catch(Exception e){
                    System.out.println("You disconnected");
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public void run() {
        BufferedReader scanner= new BufferedReader(new InputStreamReader(System.in));
        Socket server;
        try {
            server = new Socket("localhost",4000);
            System.out.println("Connection established\n");
            notifyConnectionStart();
        } catch (IOException e) {
            System.out.println("Server unreachable");
            return;
        }

        ObjectInputStream socketIn = null;
        PrintWriter socketOut = null;
        try {
            socketIn = new ObjectInputStream(server.getInputStream());
            socketOut = new PrintWriter(server.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(scanner, socketOut);
            t0.join();
            t1.join();
        } catch(InterruptedException | NoSuchElementException e) {
            System.out.println("Connection closed from the client side");
        } finally {
            try {
                scanner.close();
                socketIn.close();
                socketOut.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void elaborateMessage(Object message) {
        elaborating=true;
            if (message instanceof String) {
                if (message.equals("Welcome player 1 insert your name: "))
                    notifyWelcomeFirstPlayer();
                else if (((String) message).contains("you are waiting the FIRST PLAYER to set the number of players, insert your name: "))
                    notifyWelcomeOtherPlayers();
                else if (message.equals("You are waiting other Players to connect..."))
                    notifyWaiting();
                else if (message.equals(ServerMessage.extraClient) || message.equals(ServerMessage.gameInProgress) || message.equals("Name already taken choose another nickname") || message.equals(ServerMessage.inactivityEnd))
                    notifyGameStatus(message);
                else if (! message.equals("You entered the Game! 😊 \n") && ! ((String) message).contains("please enter the number of players"))
                    notifyGameMessage(message);
            } else if (message instanceof List)
                notifyGodSelection(message);

            elaborating=false;
    }

    /**
     * Add an observer to the Model's observer list
     *
     * @param ob
     */
    @Override
    public void attach(ClientObserver ob) {
        clientObserver=ob;
    }

    @Override
    public void notifyWelcomeFirstPlayer() {
        clientObserver.updateWelcomeFirstPlayer();
    }

    @Override
    public void notifyWelcomeOtherPlayers() {
        clientObserver.updateWelcomeOtherPlayers();
    }

    @Override
    public void notifyConnectionStart() {
        clientObserver.updateConnectionStart();
    }

    @Override
    public void notifyWaiting() {
        clientObserver.updateWaiting();
    }

    @Override
    public void notifyGodSelection(Object listOfGods) {
        clientObserver.updateGodSelection(listOfGods);
    }

    @Override
    public void notifyGameStatus(Object o) {
        clientObserver.updateGameStatus(o);
    }

    @Override
    public void notifyShow(Object o) {
        clientObserver.updateShow(o);
    }

    @Override
    public void notifyGameMessage(Object message) {
        clientObserver.updateGameMessage(message);
    }

    public void saveInput(String input){
        this.input=input;
    }

    public void showGods(Object listOfGods) {
        for (int i = 0; i <((List<String>)listOfGods).size() ; i++) {
            System.out.println(((List<String>)listOfGods).get(i));
        }
    }
}
