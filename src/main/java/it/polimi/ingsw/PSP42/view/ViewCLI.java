package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.model.*;

import javax.swing.text.*;
import java.io.*;
import java.util.*;

public class ViewCLI implements ViewObservable, ModelObserver {
    private Scanner scanner;
    private PrintStream outputStream;
    private boolean gameDone;
    private boolean turnDone;
    private boolean actionDone;
    private ArrayList<ViewObserver> obs = new ArrayList<>();

    private String gameState;
    private int numPlayers;
    private Choice c;


    public void setTurnDone(boolean value){
        turnDone = value;
    }
    public void setActionDone(boolean value){
        actionDone = value;
    }

    public Choice getChoice(){
        return c;
    }
    public ViewCLI(){
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        gameDone = false;
        turnDone = false;
        actionDone = false;
        gameState = "START";

    }

    public String getGameState(){
        return gameState;
    }
    public void setGameState(String s){
        gameState = s;
    }
    public int getNumPlayers(){
        return numPlayers;
    }

    /**
     * This method helps the Controller to ask for the players names during the creation of game
     * @return arraylist player names given through System.in
     */
    public ArrayList<UserData> getPlayerdata(){
        ArrayList<UserData> players = new ArrayList<>();
        for (int i = 0; i < numPlayers ; i++) {
            outputStream.println("Inserire il nome del giocatore:"+(i+1) );
            String nick = scanner.next();
            outputStream.println("Inserire l'età del giocatore:"+(i+1) );
            int age = scanner.nextInt();
            players.add(new UserData(nick,age));
        }

        return players;
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
        for (int i = 0; i <obs.size() ; i++)
            obs.get(i).updateInit(o);

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
    public String notifyCurrentPlayer() {
           return obs.get(0).updateCurrentPlayer();
    }

    @Override
    public void notifyEnd() {
        obs.get(0).updateEnd();
    }

    @Override
    public void notifyState(String s) {
        obs.get(0).updateState(s);
    }


    @Override
    public void updateBoard(Object o) {
        this.show();

    }

    /**
     * * The method used to start the game and handle a turn
     */
    public void run() {
        outputStream.println("\nBenvenuti al gioco SANTORINI di Giorgianni-Giudici_Govigli" + " \uD83D\uDE0A");
        while(!actionDone) {
            outputStream.println("Inserire il numero di giocatori");
            int numPlayer = scanner.nextInt();
            if(numPlayer==2 || numPlayer==3)
                setActionDone(true);
            numPlayers = numPlayer;
        }

        handleInit();
        handleInitialPosition();

        while (!gameDone) {
            setActionDone(false);
            setTurnDone(false);
            String nome = handleCurrentPlayer();
            outputStream.println("\n" + nome + " it's your turn!!!");
            outputStream.println(ViewMessage.workerMessage);
            Integer worker = scanner.nextInt();
            while(!turnDone) {

                switch (gameState) {
                    case "START":
                        while(!actionDone) {
                            handleStateChange("PREMOVE");
                        }
                        break;
                    case "PREMOVE":
                        while(!actionDone) {
                            handleStateChange("MOVE");
                        }

                        break;

                    case "MOVE":
                        while (!actionDone) {
                            outputStream.println(ViewMessage.moveMessage);
                            String input = scanner.next();
                            String[] s = input.split(",");
                            handleMove(Integer.parseInt(s[0]), Integer.parseInt(s[1]), worker);
                            handleStateChange("PREBUILD");
                        }
                        break;

                    case "PREBUILD":
                        handleStateChange("BUILD");
                        break;

                    case "BUILD":
                        outputStream.println(ViewMessage.buildMessage + worker);
                        String build = scanner.next();
                        String[] b = build.split(",");
                        outputStream.println(ViewMessage.LevelMessage);
                        String answer = scanner.next();
                        if (answer.equals("YES")) {
                            outputStream.println("Insert level:");
                            Integer level = scanner.nextInt();
                            handleBuild(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, level);
                        } else
                            handleBuild(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, 0);
                        handleStateChange("END");
                        break;
                    case "END":
                        handleEnd();
                        handleStateChange("START");
                        break;

                }
            }

        }
        String winner = handleCurrentPlayer();
        System.out.println(winner + ViewMessage.winMessage);
    }

    /**
     * Its a handle method to notify observer that have to handle a move action from the user
     * @param x x-axis for the new map position
     * @param y y-axis for the new map position
     * @param worker is an integer that tells which of the two worker are selected from the user
     */
    public void handleMove(Integer x,Integer y,Integer worker){
        notifyMove(c=new Choice(x,y,worker,null));
    }

    /**
     * this method has the task to initialize the Gameboard and set the initial players position
     */
    public void handleInit(){notifyInit(c=new Choice(null,null,null,null));}
    public void handleEnd(){
        turnDone=true;
        notifyEnd();
    }
    /**
     * Has the task to ask the user to insert the two coordinates for his 2 workers and notify observers
     * to set this data
     */
    public void handleInitialPosition(){
        this.show();
        for (int i = 0; i <numPlayers; i++) {
            for (int j = 0; j <2; j++) {
                outputStream.println("Giocatore "+(i+1)+", Inserire dove posizionare il tuo worker"+ (j+1) + "(digitare x,y)\"");
                String input = scanner.next();
                String[] s = input.split(",");
                notifyInit(c=new Choice(Integer.parseInt(s[0]),Integer.parseInt(s[1]),j+1,null));
            }


        }

    }

    /**
     * Its a handle method to notify observer that have to handle a build action from the user
     * @param x x-axis for the new map position
     * @param y y-axis for the new map position
     * @param worker is an integer that tells which of the two worker are selected from the user
     *               its the same one called from the move action
     */
    public void handleBuild(Integer x,Integer y,Integer worker,Integer level){
        notifyBuild(c=new Choice(x,y,worker,level));
    }

    public String handleCurrentPlayer(){
        return notifyCurrentPlayer();
    }

    public void handleStateChange(String s){
        notifyState(s);
    }

    public void show(){
        int cont=0;
        int init=0;

        boolean spazio=true;


        System.out.print(Color.ANSI_WATER+"\n           MAP\n\n"+Color.RESET);

        for (int i = 0; i <56 ; i++) {
            if(i==0)
                System.out.print("          ");
            if(spazio && (i-init)!=6) {
                System.out.print(" ");

            }

            if((i-init)==6) {

                System.out.print(Color.ANSI_GREEN+""+ cont+Color.RESET);
                cont++;
                init=cont*12;
            }



        }
        cont=0;
        System.out.print("\n");
        for (int i = 0; i < 6 ; i++) {
            for (int j = 0; j <= 5 * 11; j++) {
                if(j==0)
                    System.out.print("          ");
                if(j%11==0)
                    System.out.print(Color.ANSI_GREEN +""+ Color.ANSI_REVERSE + "+"+Color.RESET);

                if(j<55) {
                    //System.out.print(" ");
                    System.out.print(Color.ANSI_GREEN +""+ Color.ANSI_REVERSE + "-"+Color.RESET);
                }

            }

            System.out.print("\n");
            for (int k = 0; k <=61 ; k++) {
                if(k==0)
                    System.out.print("          "+Color.ANSI_GREEN);
                if ((k % 12 == 0) && i<5) {
                    System.out.print(Color.ANSI_REVERSE+"|"+Color.RESET+Color.ANSI_GREEN);
                }
                else
                    System.out.print(" ");
            }
            System.out.print("\n");

            for (int l = 0; l <=61 ; l++) {
                if(l==0 && i<5)
                    System.out.print("       " +cont+"  ");
                if ((l% 12 == 0) && i<5) {
                    //System.out.print(" ");
                    System.out.print(Color.ANSI_REVERSE+"|"+Color.RESET+Color.ANSI_GREEN);
                }
                else
                    System.out.print(" ");
            }
            System.out.print("\n");
            for (int m = 0; m <=61 ; m++) {
                if(m==0)
                    System.out.print("          ");
                if ((m % 12 == 0) && i<5) {
                   // System.out.print(" ");
                    System.out.print(Color.ANSI_REVERSE+"|"+Color.RESET+Color.ANSI_GREEN);
                }
                else
                    System.out.print(" ");
            }
            System.out.print("\n"+Color.RESET);
            cont++;
        }
    }



}
