package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.model.*;

import javax.swing.text.*;
import java.io.*;
import java.util.*;
import it.polimi.ingsw.PSP42.model.FakeCell;

/**
 * @author Francesco Govigli
 */
public class ViewCLI implements ViewObservable, ModelObserver {
    private Scanner scanner;
    private PrintStream outputStream;
    private boolean gameDone;
    private boolean turnDone;
    private boolean actionDone;
    private boolean undoDone;
    private boolean powerApply;
    private ArrayList<ViewObserver> obs = new ArrayList<>();
    private String gameState;
    private int numPlayers;
    private Choice c;

    /**
     * This method is used to set the boolean value turnDone, which determines
     * the end of turn for the current player.
     *
     * @param value
     */
    public void setTurnDone(boolean value){
        turnDone = value;
    }

    /**
     * This method is used to set the boolean value actionDone, which determines
     * the end of an action choosed by the current player, so that the Gamephase can move on.
     * @param value
     */
    public void setActionDone(boolean value){
        actionDone = value;
    }
    /**
     * This method is used to set the boolean value GameDone, which determines
     * the end of the game (only if a player wins).
     * @param value
     */
    public void setGameDone(boolean value){
        gameDone = value;
    }

    /**
     * Every choice (move or build) made by a player is saved in the Choice Class
     * @return c
     */
    public Choice getChoice(){
        return c;
    }

    public ViewCLI() {
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        gameDone = false;
        turnDone = false;
        actionDone = false;
        gameState = "START";
    }

    /**
     * The gameState is the division in phases of the Turn {Start,Premove,Move,Prebuild,Build,End}
     * @return gameState
     */
    public String getGameState() {
        return gameState;
    }
    /**
     * The gameState is set when a phase of the Turn ends. {Start,Premove,Move,Prebuild,Build,End}
     * @return gameState
     */
    public void setGameState(String s) {
        gameState = s;
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

    /**
     * this method is used by the controller to ask for the worker to use during the turn
     * @return worker, 1 if its the worker1 , 2 if its the worker2 of the player
     */
    public int getWorker(){
           Integer worker=null;
           boolean correct=false;
           while(!correct) {
               outputStream.println(ViewMessage.workerMessage+"\n");
               try {
                   worker =scanner.nextInt();
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

    @Override
    public String notifyCurrentPlayer() {
        return obs.get(0).updateCurrentPlayer();
    }

    @Override
    public int notifyStart() {
        return obs.get(0).updateStart();
    }

    @Override
    public void notifyState(String s) {
        obs.get(0).updateState(s);
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
    public void notifyEnd() {
        obs.get(0).updateEnd();
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
        outputStream.println("\nby Giorgianni-Giudici-Govigli" + " \uD83D\uDE0A \n");
        while(!actionDone) {
            outputStream.println(ViewMessage.numberOfPlayersMessage+"\n");
            int numPlayer=0;
            try {
                numPlayer = scanner.nextInt();;

            }
            catch(InputMismatchException e){
                System.out.println(ErrorMessage.InputMessage+"\n");
                scanner.next();
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
                            if(whatToDo[0][0].equals("EMPTY"))
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
                            if(whatToDo[1][0].equals("EMPTY"))
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
                        if(whatToDo[3][0].equals("EMPTY"))
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
                        if(whatToDo[5][0].equals("EMPTY"))
                            actionDone = true;
                        else {
                            for (int i = 0; i < whatToDo[5].length; i++) {
                                String move = whatToDo[5][i];
                                callFunction(move, worker);
                            }
                        }
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
     * This method has the task to initialize the Gameboard and set the initial players position.
     */
    public void handleInit(){
        notifyInit(c=new Choice(null,null,null,null,null));
        setActionDone(false);
    }

    /**
     * Has the task to ask the user to insert the two coordinates for his 2 workers and notify observers
     * to set this data.
     */
    public void handleInitialPosition(){
        for (int i = 0; i <numPlayers; i++) {
            for (int j = 0; j <2; j++) {
                String[] s = null;
                while (! actionDone) {
                    outputStream.println("Player " + (i + 1) + ", "+ViewMessage.initialPositionMessage + (j + 1) + "(digit x,y)"+"\n");
                    try{
                        String input = scanner.next();
                        s = input.split(",");
                        notifyInit(c = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), j + 1, null,i));
                    }
                    catch (NumberFormatException e) {
                        System.out.println(ErrorMessage.InputMessage + "\n");
                        scanner.nextLine();
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(ErrorMessage.InputMessage + "\n");
                        scanner.nextLine();
                    }
                }
                setActionDone(false);
            }
        }
        setActionDone(false);
    }

    /**
     * Has the task to ask for the current player of the new turn that is starting
     * @return nickname of the player
     */
    public String handleCurrentPlayer(){
        setTurnDone(false);
        return notifyCurrentPlayer();
    }

    /**
     * Says that the current player has lost the game
     * @param s
     */
    public void loseMessage(String s){
        System.out.println(s+ " " + ViewMessage.loseMessage);
    }

    /**
     * this method is used to ask to choose one of the 2 worker of the currentPlayer available
     * @return 1 for worker1, 2 for worker2
     */
    public int handleStart(){
        int x=0;
        while(!actionDone)
            x = notifyStart();
        setActionDone(false);
        return x;
    }

    /**
     * Determines the change of the GamePhase when actions in the current Phase are performed correctly.
     * Uses undoDone, because when an Undo action is done then if the player applied the Power (in premove or prebuild),
     * the phase will return to the beginning of that phase and again ask if he wants to apply the Power.
     * If the power has been applied correctly but in the next phase the worker is blocked the Player has the chance to
     * do an Undo during Move or Build phase. If UndoDone==true then the phase will return to the beginning of the power phase,
     * else the player will certainly lose.
     * @param s if UndoDone==false s will be the next GamePhase for sure.
     */
    public void handleStateChange(String s){
        if(undoDone && (getGameState().equals("PREMOVE") || getGameState().equals("PREBUILD")))
            notifyState(getGameState());
        else if(undoDone && powerApply && (getGameState().equals("MOVE") || getGameState().equals("BUILD"))){
            if(getGameState().equals("MOVE"))
                notifyState("PREMOVE");
            if(getGameState().equals("BUILD"))
                notifyState("PREBUILD");
            powerApply=false;
        }
        else
            notifyState(s);
        setActionDone(false);
        undoDone=false;
    }

    /**
     * It's a handle method that gives the view the information about all the action that a playerCard can perform
     * @return array of String arrays with {START,PREMOVE,MOVE,PREBUILD,BUILD,END}
     */
    public String[][] handleWhatToDo() {
        return notifyWhatToDo();
    }

    /**
     * It's a handle method to notify observer that have to handle a move action from the user.
     * @param worker is an integer that tells which of the two worker are selected from the user
     */
    public void handleMove(Integer worker){
        while(!actionDone) {
            String[] s = null;
            outputStream.println(ViewMessage.moveMessage);
            try{
                String input = scanner.nextLine();
                s = input.split(",");
                notifyMove(c = new Choice(Integer.parseInt(s[0]), Integer.parseInt(s[1]), worker, null,null));
            }
            catch (NumberFormatException e){

            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println(ErrorMessage.InputMessage + "\n");
                scanner.nextLine();
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
        while(!actionDone) {
            try {
                if(counter == 0)
                    outputStream.println(ViewMessage.buildMessage + worker + "\n");
                String[] b = null;
                String build = scanner.nextLine();
                b = build.split(",");
                if (! build.equals("")) {
                    counter = 0;
                    outputStream.println(ViewMessage.LevelMessage + "\n");
                    String answer = scanner.nextLine();
                    if (answer.toUpperCase().equals("YES")) {
                        outputStream.println("Insert level:" + "\n");
                        Integer level = scanner.nextInt();
                        notifyBuild(c = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, level, null));
                    } else
                        notifyBuild(c = new Choice(Integer.parseInt(b[0]), Integer.parseInt(b[1]), worker, 0, null));
                }
                else
                    counter = 1;
            }
            catch (NumberFormatException e){
                System.out.println(ErrorMessage.InputMessage + "\n");
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println(ErrorMessage.InputMessage + "\n");
                scanner.nextLine();
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
            System.out.println("Your god's power started!\n" + effect );
        if(s.equals("OFF"))
            System.out.println("Your god's power finished!\n" + effect);
    }

    /**
     * only set the turn to done and will notify the controller to change to the next currentPlayer
     */
    public void handleEnd(){
        turnDone=true;
        powerApply=false;
        notifyEnd();
    }

    /**
     * Utility method to call the correct handle method
     * @param s says if handle function must me of type : MOVE, BUILD OR EFFECT
     * @param worker all the actions (not the effect) are referred to a worker that does it
     */
    public void callFunction(String s,Integer worker){
        switch (s) {
            case "MOVE":
                System.out.println(s + " POWER: " + ViewMessage.applyPowerMessage);
                String input = scanner.nextLine();
                if(input.toUpperCase().equals("YES")) {
                    handleMove(worker);
                    powerApply=true;
                }
                else
                    setActionDone(true);
                break;
            case "BUILD":
                System.out.println(s + " POWER: " + ViewMessage.applyPowerMessage);
                input = scanner.nextLine();
                if(input.toUpperCase().equals("YES")) {
                    handleBuild(worker);
                    powerApply = true;
                }
                else
                    setActionDone(true);
                break;
            case "EFFECT":
                handleEffect();
                break;
        }
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
                    System.out.println("You input nothing. No undo is done...");
                    value[0] = false;
                }
            }
        };
        timer.schedule(task, 5000);
        if(warning.equals("WARNING"))
            System.out.println(ErrorMessage.PowerBlockingMessage);
        System.out.println( "Input a YES within 5 seconds for UNDO : ");
        //str = scanner.nextLine();
        Scanner input = new Scanner(System.in);
        String action = input.nextLine();
        timer.cancel();
        if(value[0]==false || (!action.toUpperCase().equals("YES")) || action.equals("")) {
            System.out.println("UNDO is not applied");
            undoDone = false;
            return false;
        }
        if(action.toUpperCase().equals("YES")) {
            System.out.println("UNDO is applied");
            undoDone = true;
            return true;
        }
        else {
            return false;
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
        System.out.println(Color.ANSI_RED + "Player 1 " + Color.ANSI_GREEN + "Player 2 " + Color.ANSI_BLUE + "Player 3 " + Color.RESET);
        System.out.println("\n");
    }
}
