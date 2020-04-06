package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ViewCLI implements GameObservable,GameObserver {
    private Scanner scanner;
    private PrintStream outputStream;
    private boolean done;
    private ArrayList<GameObserver> obs;

    public ViewCLI(){
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        done = false;

    }

    /**
     * Aggiunge un osservatore alla lista dell'osservato
     *
     * @param ob
     */
    @Override
    public void attach(GameObserver ob) {
        obs.add(ob);
    }

    /**
     * Rimuove un osservatore dalla lista dell'osservato
     *
     * @param ob
     */
    @Override
    public void detach(GameObserver ob) {
        obs.remove(ob);
    }

    /**
     * notifica tutti gli osservati interessati ad un cambiamento di stato
     */
    @Override
    public void notifyObservers() {
        for (int i = 0; i <obs.size() ; i++) {
            obs.get(i).update();
        }
    }

    @Override
    public void update() {
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







    public void run(){
        outputStream.println("\n Benvenuti al gioco SANTORINI di Giorgianni-Giudici_Govigli"+" \uD83D\uDE0A");

        this.show();
        while(done){
            String input = scanner.next();
            outputStream.println("Inserire una posizione in cui muoversi digitare x,y");
            String[] s = input.split(",");
            handleMove(Integer.parseInt(s[0]),Integer.parseInt(s[1]));

        }
    }
    public void handleMove(Integer x,Integer y){
        notifyObservers();
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
            System.out.print("\n");
            cont++;
        }
    }

    public String atlasRequest() {
        System.out.println("What level do you wanna build? 'Next level' or Dome? \n");
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    public String demeterRequest() {
        System.out.println("Do you wanna build again? If you want type the cell (es. '1 1'), otherwise type 'No' \n");
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    public static void main(String[] args) {
        ViewCLI v = new ViewCLI();
    }
    public void generateCell(int x,int y){

    }


}
