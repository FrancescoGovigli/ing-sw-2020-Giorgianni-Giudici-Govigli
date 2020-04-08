package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;

import java.io.*;
import java.util.*;

public class ViewCLI implements ViewObservable, ModelObserver {
    private Scanner scanner;
    private PrintStream outputStream;
    private boolean done;
    private ArrayList<ViewObserver> obs = new ArrayList<>();
    private enum State {INIT,MOVE,BUILD,END}
    private State gameState;
    private int numPlayers;
    private Choice c;

    public Choice getChoice(){
        return c;
    }
    public ViewCLI(){
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        done = false;
        gameState = State.INIT;

    }

    public String getGameState(){
        return gameState.toString();
    }
    public void setGameState(State s){
        gameState = s;
    }
    public int getNumPlayers(){
        return numPlayers;
    }

    /**
     * This method helps the Controller to ask for the players names during the creation of game
     * @return arraylist player names given through System.in
     */
    public ArrayList<String> getPlayernames(){
        ArrayList<String> players = new ArrayList<>();
        for (int i = 0; i < numPlayers ; i++) {
            outputStream.println("Inserire il nome del giocatore :"+(i+1) );
            players.add(scanner.next());
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
    public void update(Object o) {
        this.show();
    }

    public enum Color
    {
        ANSI_SMILE("\u1F60"),
        ANSI_RED("\u001B[31m"),
        ANSI_GREEN("\u001B[38;5;70m"),
        ANSI_WATER("\u001B[36m"),
        ANSI_YELLOW("\u001B[33m"),
        ANSI_REVERSE("\u001B[7m"),
        ANSI_BLUE("\u001B[34m"),
        ANSI_PURPLE("\u001B[35m");
        static final String RESET = "\u001B[0m"; // resetta il valore a un default
        //In questo modo il metodo values() (che Java genera in
        //automatico e restituisce un array di tutti gli elementi della
        //enum) non lo restituisce
        //In fondo,
        //“RESET” non è un colore!

        private String escape; // contiene il codice testo che modifica il colore
        Color(String escape)
        {
            this.escape = escape;
        }
        public String getEscape()
        {
            return escape;
        }

        /**
         * Il metodo toString() è speciale perché viene
         * chiamato da Java implicitamente quando serve
         * convertire un oggetto in stringa
         * @return
         */
        @Override
        public String toString()
        {
            return escape;
        }
    }


    /**
     * The method used to start the game and handle a turn
     */
    public void run(){
        outputStream.println("\nBenvenuti al gioco SANTORINI di Giorgianni-Giudici_Govigli"+" \uD83D\uDE0A");
        outputStream.println("Inserire il numero di giocatori");
        int numPlayer = scanner.nextInt();
        numPlayers = numPlayer;
        handleInit();
        handleInitialPosition();

        while(!done){
            String nome = handleCurrentPlayer();
            outputStream.println("\n"+nome+ " it's your turn!!!");
            //MOVE
            setGameState(State.MOVE);
            outputStream.println("\nInserire quale worker muovere (digitare 1 o 2)");
            Integer worker = scanner.nextInt();
            outputStream.println("\nInserire una posizione in cui muoversi (digitare x,y)");
            String input = scanner.next();
            String[] s = input.split(",");
            handleMove(Integer.parseInt(s[0]),Integer.parseInt(s[1]),worker);
            outputStream.println("\nInserire una posizione in cui costruire con il tuo worker"+worker+ "(digitare x,y)");
            String build = scanner.next();
            String[] b = input.split(",");
            handleBuild(Integer.parseInt(b[0]),Integer.parseInt(b[1]),worker);
            handleEnd();

        }
    }

    /**
     * Its a handle method to notify observer that have to handle a move action from the user
     * @param x x-axis for the new map position
     * @param y y-axis for the new map position
     * @param worker is an integer that tells which of the two worker are selected from the user
     */
    public void handleMove(Integer x,Integer y,Integer worker){
        notifyMove(c=new Choice(x,y,worker));
    }

    /**
     * this method has the task to initialize the Gameboard and set the initial players position
     */
    public void handleInit(){notifyInit(c=new Choice(null,null,null));}
    public void handleEnd(){notifyEnd();}
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
                notifyInit(c=new Choice(Integer.parseInt(s[0]),Integer.parseInt(s[1]),j+1));
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
    public void handleBuild(Integer x,Integer y,Integer worker){
        notifyBuild(c=new Choice(x,y,worker));
    }

    public String handleCurrentPlayer(){
        return notifyCurrentPlayer();
    }

    public void show(){
        int cont=0;
        int init=0;
        int succ;
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
