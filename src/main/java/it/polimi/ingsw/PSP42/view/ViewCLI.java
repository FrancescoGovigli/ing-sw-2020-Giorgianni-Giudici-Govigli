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
        setActionDone(false);
        List<String> setOfCards = new LinkedList<String>(Arrays.asList(set));
        setOfCards.add("NOGOD");
        ArrayList<UserData> players = new ArrayList<>();
        int age = 0;
        for (int i = 0; i < numPlayers ; i++) {
            outputStream.println("Insert your name player : "+(i+1) +"\n" );
            String nick = scanner.next();
            while(!actionDone) {
                outputStream.println("Insert your age player : " + (i + 1) + "\n");

                try {
                    age = scanner.nextInt();
                    setActionDone(true);
                } catch (InputMismatchException e) {
                    System.out.println(ErrorMessage.InputMessage + "\n");
                    scanner.nextLine();
                }
            }

            boolean choiceDone=false;
            String selectedCard = null;
            while(!choiceDone) {
                outputStream.println("Select one of the card in the set player : " + (i + 1)+"\n");
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
            setActionDone(false);
            }

        return players;
        }

        public int getWorker(){
           Integer worker=null;
           boolean correct=false;
           while(!correct) {
               outputStream.println(ViewMessage.workerMessage+"\n");
               try {
                   worker = scanner.nextInt();
                   if (worker == 1 || worker == 2)
                       correct = true;
               }
               catch(InputMismatchException e){
                   System.out.println(ErrorMessage.InputMessage+"\n");
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
        this.show(o);

    }

    /**
     * * The method used to start the game and handle a turn
     */
    public void run() {
        outputStream.println(".-. . .-..----..-.    .---.  .----. .-.   .-..----.    .---.  .----.     .----.  .--.  .-. .-. .---.  .----. .----. .-..-. .-..-.\n" +
                "| |/ \\| || {_  | |   /  ___}/  {}  \\|  `.'  || {_     {_   _}/  {}  \\   { {__   / {} \\ |  `| |{_   _}/  {}  \\| {}  }| ||  `| || |\n" +
                "|  .'.  || {__ | `--.\\     }\\      /| |\\ /| || {__      | |  \\      /   .-._} }/  /\\  \\| |\\  |  | |  \\      /| .-. \\| || |\\  || |\n" +
                "`-'   `-'`----'`----' `---'  `----' `-' ` `-'`----'     `-'   `----'    `----' `-'  `-'`-' `-'  `-'   `----' `-' `-'`-'`-' `-'`-'");
        outputStream.println("\nby Giorgianni-Giudici_Govigli" + " \uD83D\uDE0A \n");
        while(!actionDone) {
            outputStream.println(ViewMessage.numberOfPlayersMessage+"\n");
            int numPlayer=0;
            try {
                numPlayer = scanner.nextInt();
                setActionDone(true);
            }
            catch(InputMismatchException e){
                System.out.println(ErrorMessage.InputMessage+"\n");
                scanner.nextLine();
            }
            if(numPlayer==2 || numPlayer==3) {
                numPlayers = numPlayer;
                setActionDone(true);
            }
        }

        handleInit();
        handleInitialPosition();

        while (!gameDone) {
            Integer worker=null;
            String[][] whatToDo=null;
            String nome = handleCurrentPlayer();
            while(!turnDone) {

                if(gameState.equals("START")) {
                    outputStream.println("\n" + nome + " it's your turn!!!"+"\n");
                    whatToDo = handleWhatToDo();
                }

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
        for (int i = 0; i <numPlayers; i++) {
            for (int j = 0; j <2; j++) {
                while (! actionDone) {
                outputStream.println("Player " + (i + 1) + ", "+ViewMessage.initialPositionMessage + (j + 1) + "(digit x,y)"+"\n");
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
            outputStream.println(ViewMessage.buildMessage + worker+"\n");
            String build = scanner.next();
            String[] b = build.split(",");
            outputStream.println(ViewMessage.LevelMessage+"\n");
            String answer = scanner.next();
            if (answer.toUpperCase().equals("YES")) {
                outputStream.println("Insert level:"+"\n");
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

    /**
     * Method to print the current GameBoard situation on the screen
     */
    public void show(Object o){
        //TODO make the appropriate setting of the individual cells based on the copy of the GameBoard received
        FakeCell[][] copy = (FakeCell[][]) o;
        int rowIndex = 0;
        int colIndex = 0;
        System.out.println();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 41; j++) {
                boolean row1 = (i == 1 || i == 4 || i == 7 || i == 10 || i == 13);
                boolean row2 = (i == 2 || i == 5 || i == 8 || i == 11 || i == 14);
                boolean col1 = (j == 3 || j == 11 || j == 19 || j == 27 || j == 35);
                boolean col2 = (j == 5 || j == 13 || j == 21 || j == 29 || j == 37);
                if (i % 3 == 0)
                    if (j % 8 == 0)
                        System.out.print(Color.ANSI_REVERSE + "+" + Color.RESET);
                    else
                        System.out.print(Color.ANSI_REVERSE + "―" + Color.RESET);
                else if (j % 8 == 0)
                    System.out.print(Color.ANSI_REVERSE + "|" + Color.RESET);
                else if (col1 && row1)  // possible worker
                    System.out.print("W");
                else if (col2 && row1)  // worker's number
                    System.out.print("1");
                else if (col1 && row2)  // level
                    System.out.print("L");
                else if (col2 && row2)  // level's level
                    System.out.print("0");
                else
                    System.out.print(" ");
                if (j == 40 && row1)
                    System.out.print(" ROW");
                else if (j == 40 && row2) {
                    System.out.print(" " + rowIndex);
                    rowIndex++;
                }
            }
            System.out.println();
        }
        for (int j = 0; j < 5; j++){
            System.out.print("  COL " + colIndex + " ");
            colIndex++;
        }
        System.out.println();
    }

    public void callFunction(String s,Integer worker){

        switch (s) {
            case "MOVE":
                System.out.println(s+"POWER:"+ViewMessage.applyPowerMessage);
                String input = scanner.next();
                if(input.toUpperCase().equals("YES"))
                    handleMove(worker);
                else
                    setActionDone(true);
            case "BUILD":
                System.out.println(s+"POWER:"+ViewMessage.applyPowerMessage);
                input = scanner.next();
                if(input.toUpperCase().equals("YES"))
                    handleBuild(worker);
                else
                    setActionDone(true);
            case "EFFECT":
                handleEffect();
        }
    }

    public void loseMessage(String s){
        System.out.println(s+ " " + ViewMessage.loseMessage);
    }



}
