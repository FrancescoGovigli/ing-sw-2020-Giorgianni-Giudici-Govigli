package it.polimi.ingsw.PSP42.server;

import it.polimi.ingsw.PSP42.controller.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.net.*;
import java.util.*;

public class ServerGameThread implements Runnable {

    private Server server;
    private VirtualView view;
    private GameBoard model;
    private Controller controller;
    private ClientHandler managedClient;
    private enum ConnectionState {AVAILABLE, TIME_OUT, DISCONNECTED}
    private static ConnectionState connectionState = ConnectionState.AVAILABLE;
    private static ArrayList<ClientHandler> sgtClients = new ArrayList<>();
    private static Object resetLock = new Object();
    private static Object numberPlayersLock = new Object();

    public static ArrayList<ClientHandler> getSgtClients() {
        return (ArrayList<ClientHandler>) sgtClients.clone();
    }

    public ClientHandler getManagedClient() {
        return managedClient;
    }

    public String getClientNickname(){
        return managedClient.getNickName();
    }

    public int getOrderOfConnection(){
        return managedClient.getClientID();
    }

    public static boolean isConnectionAvailable() {
        return connectionState == ConnectionState.AVAILABLE;
    }

    /**
     * Constructor for the thread that will manage the clientSocket until the game starts.
     * This thread is created to manage clients initialization (insertion of nickname and number of players).
     * @param server Server
     * @param clientSocket (client' socket to manage)
     * @param orderOfConnection (order in which the ClientCLI has connected to the Server)
     */
    public ServerGameThread(Server server, Socket clientSocket, Integer orderOfConnection) {
        this.server = server;
        this.managedClient = new ClientHandler(clientSocket, orderOfConnection);
        sgtClients.add(managedClient);
    }

    /**
     * Constructor for the thread that will create the game.
     * This ServerGameThread is created when the required number of players has been reached in the waiting room and
     * it will take over the game.
     * @param server Server
     * @param waitingClients (Clients who will play the game)
     */
    public ServerGameThread(Server server, ArrayList<ClientHandler> waitingClients) {
        this.server = server;
        sgtClients = waitingClients;
    }

    /**
     * Method to create all the components necessary to play and finally launch the game.
     * At the end of the game, if everything is successful, it will reset the game, so it can start a new one.
     */
    public void startGame() {
        sgtClients.sort((ClientHandler z1, ClientHandler z2) -> {
            if (z1.getClientID() > z2.getClientID())
                return 1;
            if (z1.getClientID() < z2.getClientID())
                return -1;
            return 0;
        });
        System.out.println("The Game is started");
        NetworkVirtualView.assignSGT(this);
        view = new VirtualView(sgtClients, server.getNumberOfPlayer());
        model = GameBoard.getInstance();
        controller = new Controller(model, view);
        view.attach(controller);
        model.attach(view);
        controller.runGame();
        if (!view.isInterrupted())
            resetGame();
    }

    /**
     * Method to reset the game (model and server, to be able to accept new connections).
     * Game reset can occur if the game ends,
     * if a player takes too much time to perform an action or
     * if a disconnection occurs.
     * In these cases, a message will be sent to the players and the server reset will be called.
     */
    public void resetGame() {
        synchronized (resetLock) {
            if (model != null) {
                view.handleInterrupt();
                model.reset();
            }
            if (connectionState.equals(ConnectionState.AVAILABLE)) {
                clientCommunicationAndInactivation(sgtClients, ServerMessage.gameEnd);
                server.reset(ServerMessage.END);
            } else if (connectionState.equals(ConnectionState.TIME_OUT)) {
                clientCommunicationAndInactivation(sgtClients, ServerMessage.inactivityEnd);
                server.reset(ServerMessage.INACTIVITY);
            } else if (connectionState.equals(ConnectionState.DISCONNECTED)) {
                clientCommunicationAndInactivation(sgtClients, ServerMessage.disconnectionEnd);
                server.reset(ServerMessage.DISCONNECTION);
            }
        }
    }


    /**
     * Method to communicate to the active Clients the reason for closing the game and then inactivate them.
     * @param sgtClients (ClientCLI arrays managed by an ServerGameThread)
     * @param object (message indicating why the game was closed)
     */
    private void clientCommunicationAndInactivation(ArrayList<ClientHandler> sgtClients, Object object) {
        for (ClientHandler sgtClient : sgtClients) {
            if (sgtClient.isActive()) {
                send(sgtClient, object);
                sgtClient.clientInactivation();
            }
        }
    }

    /**
     * Method to make the connection to the game available.
     * It is called by the Server after a reset.
     */
    public static void reactivateConnectionState() {
        connectionState = ConnectionState.AVAILABLE;
    }

    /**
     * Method executed by threads that manage clients.
     */
    @Override
    public void run() {
        settingClient();
    }

    /**
     * Method to welcome the client, ask for his name and the number of players if he is the first to connect,
     * make him ready to play and put him on hold until the Server has reached the number of players needed to play.
     */
    public void settingClient() {
        Object object;
        String nickName = null;
        send(managedClient, ServerMessage.enteredGame);
        if (managedClient.getClientID() == 1) {
            send(managedClient, "Welcome player " + managedClient.getClientID() + " insert your name: ");
            object = read(managedClient);
            if (!isReadOK(object)) {
                return;
            }
            nickName = object.toString();
            Integer i = chooseNumberOfPlayer(nickName);
            if (!isReadOK(i)) {
                return;
            }
            System.out.println("Number of players received is: " + i);
            server.setNumberOfPlayer(i);
            synchronized (numberPlayersLock) {
                if (managedClient.isActive()) {
                    managedClient.setNickName(nickName);
                    server.waitingRoom(this);
                }
                numberPlayersLock.notifyAll();
            }
        }
        else {
            send(managedClient, "Welcome player " + managedClient.getClientID() + " you are waiting the FIRST PLAYER to set the number of players, insert your name: ");
            do {
                if (nickName != null)
                    send(managedClient, ServerMessage.nameNotFree);
                object = read(managedClient);
                if (!isReadOK(object)) {
                    return;
                }
                nickName = object.toString();
                while (!server.isNumberOfPlayerSet() && isConnectionAvailable()) {
                    synchronized (numberPlayersLock) {
                        try {
                            numberPlayersLock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("InterruptedException in ServerGameThread -> settingClient");
                        }
                    }
                }
            } while (!server.isNickNameUnique(nickName));
            if (managedClient.isActive()) {
                managedClient.setNickName(nickName);
                server.waitingRoom(this);
            }
        }
    }

    /**
     * Method for setting the number of players (executed by the first connected client).
     * @param name of who will perform the setting
     * @return choice (the set number)
     */
    private Integer chooseNumberOfPlayer(String name) {
        Object object = null;
        boolean correctChoice = false;
        Integer choice = null;
        String string = (name + ", please enter the number of players: ");
        send(managedClient, string);
        while (!correctChoice) {
            if (choice != null) {
                string = (name + ", please enter a correct value of players (2 or 3): ");
                send(managedClient, string);
            }
            object = read(managedClient);
            if (!isReadOK(object))
                return null;
            else {
                choice = Integer.parseInt(object.toString());
                if (choice == 2 || choice == 3)
                    correctChoice = true;
            }
        }
        return choice;
    }

    /**
     * Method to send an object to a Client.
     * @param clientHandler (reference to the client concerned)
     * @param object to send
     */
    public void send(ClientHandler clientHandler, Object object) {
        clientHandler.sendToClient(object);
    }

    /**
     * Method to receive an object from a Client.
     * The method contains a timer to avoid waiting too long and to understand if a disconnection has occurred.
     * @param clientHandler (reference to the client concerned)
     * @return receivedObject
     */
    public Object read(ClientHandler clientHandler) {
        final Object[] receivedObject = {null};
        Timer timer = new Timer();
        final boolean[] timeOut = {false};
        TimerTask task = new TimerTask() {
            public void run() {
                if (receivedObject[0] == null) {
                    timeOut[0] = true;
                    connectionState = ConnectionState.TIME_OUT;
                    if (view != null)
                        view.handleInterrupt();
                    resetGame();
                }
            }
        };
        timer.schedule(task, 40000);
        receivedObject[0] = clientHandler.readFromClient();
        timer.cancel();
        if (!timeOut[0] && receivedObject[0] == null)
            connectionState = ConnectionState.DISCONNECTED;
        return receivedObject[0];
    }

    /**
     * Method to verify that what has been received by the Client is correct.
     * @param object to be verified
     * @return true if the object exists (it isn't null), false otherwise
     */
    private boolean isReadOK(Object object) {
        synchronized (resetLock) {
            if (object == null && connectionState == ConnectionState.DISCONNECTED)
                resetGame();
            return object != null;
        }
    }
}
