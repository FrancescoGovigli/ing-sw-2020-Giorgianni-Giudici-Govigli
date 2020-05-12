package it.polimi.ingsw.PSP42.client;

import it.polimi.ingsw.PSP42.server.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements Runnable{

    private NetworkHandler net;
    private boolean active=true;
    private boolean writeActive=true;
    private ObjectOutputStream output;
    private ArrayList<UserData> playersData = new ArrayList<>();

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
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if(inputObject instanceof String){
                            if(((String) inputObject).contains(ViewMessage.personalLoseMessage)) {
                                System.out.println("[FROM SERVER] : " + inputObject);
                                System.out.println("Game closed, PRESS [ENTER] TO QUIT...");
                                socketIn.close();
                                setActive(false);
                            }
                            else if(!inputObject.equals(ServerMessage.extraClient) && !inputObject.equals(ServerMessage.gameInProgress) && !inputObject.equals(ServerMessage.endGame) && !inputObject.equals(ServerMessage.inactivityEnd))
                                System.out.println("[FROM SERVER] : "+inputObject);

                            else {
                                System.out.println("[FROM SERVER] : "+inputObject);
                                socketIn.close();
                                setActive(false);
                                System.out.println("[FROM SERVER] : PRESS [ENTER] TO QUIT ");

                            }
                        }
                        else if(inputObject instanceof Boolean)
                            writeActive=(Boolean)inputObject;

                        else if(inputObject instanceof UserData)
                            playersData.add(((UserData) inputObject));

                        else
                            show(inputObject);
                    }
                } catch (Exception e){
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
                        String inputLine = stdin.readLine();
                        if(writeActive) {
                            socketOut.println(inputLine);
                            socketOut.flush();
                        }
                    }
                }catch(Exception e){
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
        System.out.println("IP address of server? or press Enter to skip this step");
        String ip = null;
        try {
            ip = scanner.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket server;
        try {
            server = new Socket(ip, 4000);
            System.out.println("Connection established\n");
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


        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(scanner, socketOut);
            t0.join();
            t1.join();
        } catch(InterruptedException | NoSuchElementException e){
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
    /**
     * Method to print the current GameBoard situation on the screen
     */
    public void show(Object o){
        FakeCell[][] gCopy = (FakeCell[][]) o;
        int rowIndex = 0;
        int colIndex = 0;
        int x = 0;
        int y = 0;
        System.out.println();
        for (int i = 0; i < 16; i++) {
            boolean row1 = (i == 1 || i == 4 || i == 7 || i == 10 || i == 13);
            boolean row2 = (i == 2 || i == 5 || i == 8 || i == 11 || i == 14);
            boolean rowBoardIndex = (i == 3 || i == 6 || i == 9 || i == 12);
            for (int j = 0; j < 41; j++) {
                boolean col1 = (j == 3 || j == 11 || j == 19 || j == 27 || j == 35);
                boolean col2 = (j == 5 || j == 13 || j == 21 || j == 29 || j == 37);
                boolean colBoardIndex = (j == 9 || j == 17 || j == 25 || j == 33);
                if (i % 3 == 0)
                    if (j % 8 == 0)
                        System.out.print(Color.ANSI_REVERSE + "+" + Color.RESET);
                    else
                        System.out.print(Color.ANSI_REVERSE + "-" + Color.RESET);
                else if (j % 8 == 0)
                    System.out.print(Color.ANSI_REVERSE + "|" + Color.RESET);
                else if (col1 && row1)  // possible worker
                    if (gCopy[x][y].getPlayerName() != null)    // if a player has a worker set
                        switch (gCopy[x][y].getId()){   // color (RGB) his "W" according to his id
                            case 1:
                                System.out.print(Color.ANSI_RED + "W" + Color.RESET);
                                break;
                            case 2:
                                System.out.print(Color.ANSI_GREEN + "W" + Color.RESET);
                                break;
                            case 3:
                                System.out.print(Color.ANSI_BLUE + "W" + Color.RESET);
                                break;
                            default:
                                System.out.print("W");
                                break;
                        }
                    else
                        System.out.print(" ");
                else if (col2 && row1)  // worker's number
                    if (gCopy[x][y].getWorkerNum() == 1)
                        System.out.print("1");
                    else if (gCopy[x][y].getWorkerNum() == 2)
                        System.out.print("2");
                    else
                        System.out.print(" ");
                else if (col1 && row2)  // level
                    System.out.print("L");
                else if (col2 && row2)  // level's level
                    System.out.print(gCopy[x][y].getLevel());
                else
                    System.out.print(" ");
                if (j == 40 && row1)
                    System.out.print(" ROW");
                else if (j == 40 && row2) {     // to print the row index out of the map
                    System.out.print(" " + rowIndex);
                    rowIndex++;
                }
                if (colBoardIndex && (row1 || row2))  // increase column index for gCopy if you are in the worker or level row
                    y++;
            }
            if (row1)   y = 0;  // reset column index for gCopy if you are in the level row
            if (rowBoardIndex) {    // reset column index  and increase row index for gCopy if you are in the "―" line
                y = 0;
                x++;
            }
            System.out.println();
        }
        for (int j = 0; j < 5; j++){    // to print the column index off the map
            System.out.print("  COL " + colIndex + " ");
            colIndex++;
        }
        System.out.println();
        System.out.println("Color matching to the letter 'W':");
        if(playersData.size()==3)
            System.out.println("PLAYERS: " + Color.ANSI_RED + "Player 1: " + playersData.get(0).getNickname()+" with "+playersData.get(0).getCardChoosed().toUpperCase() +" "+ Color.ANSI_GREEN + "Player 2: " + playersData.get(1).getNickname() +" with "+playersData.get(1).getCardChoosed().toUpperCase() +" "+ Color.ANSI_BLUE + "Player 3: "+ playersData.get(2).getNickname()+" with "+playersData.get(2).getCardChoosed().toUpperCase()+ Color.RESET);
        if(playersData.size()==2)
            System.out.println("PLAYERS: " + Color.ANSI_RED + "Player 1: " + playersData.get(0).getNickname()+" with "+playersData.get(0).getCardChoosed().toUpperCase() +" "+ Color.ANSI_GREEN + "Player 2: " + playersData.get(1).getNickname() +" with "+playersData.get(1).getCardChoosed().toUpperCase() + Color.RESET);
        System.out.println("\n");
    }
}
