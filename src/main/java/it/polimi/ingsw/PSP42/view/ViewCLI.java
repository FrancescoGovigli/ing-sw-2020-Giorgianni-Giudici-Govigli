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
    public void setGameDone(boolean value){
        gameDone = value;
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
    public ArrayList<UserData> getPlayerdata(String[] set){
        List<String> setOfCards = new LinkedList<String>(Arrays.asList(set));
        setOfCards.add("NOGOD");
        ArrayList<UserData> players = new ArrayList<>();
        for (int i = 0; i < numPlayers ; i++) {
            outputStream.println("Insert your name player :"+(i+1) );
            String nick = scanner.next();
            outputStream.println("Insert your age player :"+(i+1) );
            int age = scanner.nextInt();
            boolean choiceDone=false;
            String selectedCard = null;
            while(!choiceDone) {
                outputStream.println("Select one of the card in the set player :" + (i + 1));
                for (int j = 0; j < setOfCards.size(); j++) {
                    System.out.println(setOfCards.get(j));
                }
                selectedCard = scanner.next();

                if (setOfCards.contains(selectedCard.toUpperCase())) {
                    if(!selectedCard.toUpperCase().equals("NOGOD"))
                       setOfCards.remove(selectedCard.toUpperCase());
                    choiceDone = true;
                }
            }

            players.add(new UserData(nick,age,selectedCard.toUpperCase()));
            }
        return players;
        }

        public int getWorker(){
           Integer worker=null;
           boolean correct=false;
           while(!correct) {
               outputStream.println(ViewMessage.workerMessage);
               try {
                   worker = scanner.nextInt();
                   if (worker == 1 || worker == 2)
                       correct = true;
               }
               catch(InputMismatchException e){
                   System.out.println(ErrorMessage.InputMessage);
               }
               scanner.nextLine();//Clear del buffer
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
    public String[][] notifyWhatToDo() {
        return obs.get(0).updateWhatToDo();
    }

    @Override
    public int notifyStart() {
       return obs.get(0).updateStart();
    }

    @Override
    public void notifyEffect() {
        obs.get(0).updateEffect();
    }


    @Override
    public void updateBoard(Object o) {
        this.show();

    }

    /**
     * * The method used to start the game and handle a turn
     */
    public void run() {
        outputStream.println("\nWELCOME TO SANTORINI GAME by Giorgianni-Giudici_Govigli" + " \uD83D\uDE0A \n");
        while(!actionDone) {
            outputStream.println(ViewMessage.numberOfPlayersMessage);
            int numPlayer=0;
            try {
                numPlayer = scanner.nextInt();
                setActionDone(true);
            }
            catch(InputMismatchException e){
                System.out.println(ErrorMessage.InputMessage+"\n");
                scanner.nextLine();
            }
            //if(numPlayer==2 || numPlayer==3)

            numPlayers = numPlayer;
        }

        handleInit();
        handleInitialPosition();

        while (!gameDone) {
            Integer worker=null;
            String nome = handleCurrentPlayer();
            outputStream.println("\n" + nome + " it's your turn!!!");
            String[][] whatToDo = handleWhatToDo();
            while(!turnDone) {
                switch (gameState) {
                    case "START":
                        while(!actionDone) {
                            if(whatToDo[0][0].equals("NULL"))
                                actionDone=true;
                            else {
                                for (int i = 0; i < whatToDo[0].length; i++) {
                                    String move = whatToDo[0][i];
                                    callFunction(move, worker);
                                }
                            }
                        }
                        handleStateChange("PREMOVE");
                        worker = handleStart();
                        break;
                    case "PREMOVE":
                            if(whatToDo[1][0].equals("NULL"))
                                actionDone=true;
                            else {
                                for (int i = 0; i < whatToDo[1].length; i++) {
                                    String move = whatToDo[1][i];
                                    callFunction(move, worker);
                                }
                            }
                        handleStateChange("MOVE");
                        break;
                    case "MOVE":
                        handleMove(worker);
                        handleStateChange("PREBUILD");
                        break;
                    case "PREBUILD":
                        if(whatToDo[3][0].equals("NULL"))
                            actionDone=true;
                        else {
                            for (int i = 0; i < whatToDo[3].length; i++) {
                                String move = whatToDo[3][i];
                                callFunction(move, worker);
                            }
                        }
                        handleStateChange("BUILD");
                        break;
                    case "BUILD":
                        handleBuild(worker);
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
        System.out.println(winner +" "+ ViewMessage.winMessage);
    }

    /**
     * Its a handle method to notify observer that have to handle a move action from the user
     * @param x x-axis for the new map position
     * @param y y-axis for the new map position
     * @param worker is an integer that tells which of the two worker are selected from the user
     */
    public void handleMove(Integer worker){
        while(!actionDone) {
            outputStream.println(ViewMessage.moveMessage);
            String input = scanner.next();
            String[] s = input.split(",");
            notifyMove(c = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), worker, null,null));
        }
    }

    /**
     * this method has the task to initialize the Gameboard and set the initial players position
     */
    public void handleInit(){
        notifyInit(c=new Choice(null,null,null,null,null));
        setActionDone(false);
    }
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
                while (! actionDone) {
                outputStream.println("Player " + (i + 1) + ", "+ViewMessage.initialPositionMessage + (j + 1) + "(digit x,y)\"");
                String input = scanner.next();
                String[] s = input.split(",");
                notifyInit(c = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), j + 1, null,i));
                }
                setActionDone(false);
            }


        }
        setActionDone(false);

    }

    /**
     * Its a handle method to notify observer that have to handle a build action from the user
     * @param x x-axis for the new map position
     * @param y y-axis for the new map position
     * @param worker is an integer that tells which of the two worker are selected from the user
     *               its the same one called from the move action
     */
    public void handleBuild(Integer worker){
        while(!actionDone) {
            outputStream.println(ViewMessage.buildMessage + worker);
            String build = scanner.next();
            String[] b = build.split(",");
            outputStream.println(ViewMessage.LevelMessage);
            String answer = scanner.next();
            if (answer.toUpperCase().equals("YES")) {
                outputStream.println("Insert level:");
                Integer level = scanner.nextInt();
                notifyBuild(c = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, level,null));
            } else
                notifyBuild(c = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, 0,null));
        }
    }

    public String handleCurrentPlayer(){
        setTurnDone(false);
        return notifyCurrentPlayer();
    }

    public void handleStateChange(String s){
        notifyState(s);
        setActionDone(false);
    }

    public String[][] handleWhatToDo(){ return notifyWhatToDo();
    }

    public int handleStart(){
        int x=0;
        while(!actionDone)
          x = notifyStart();
        setActionDone(false);
       return x;
    }

    public void handleEffect(){
        notifyEffect();
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

    public void callFunction(String s,Integer worker){
        System.out.println(s+"POWER:"+ViewMessage.applyPowerMessage);
        String input = scanner.next();
        if(input.toUpperCase().equals("YES")) {
            switch (s) {
                case "MOVE":
                    handleMove(worker);
                case "BUILD":
                    handleBuild(worker);
                case "EFFECT":
                    handleEffect();
            }
        }
        else
            setActionDone(true);
    }



}
