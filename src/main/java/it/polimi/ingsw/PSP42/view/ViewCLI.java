package it.polimi.ingsw.PSP42.view;

import java.awt.*;
import java.util.Scanner;

public class ViewCLI {

    public enum Color
    {
        ANSI_RED("\u001B[31m"),
        ANSI_GREEN("\u001B[32m"),
        ANSI_YELLOW("\u001B[33m"),
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

    public ViewCLI(){
        show();
    }

    public void show(){
        System.out.print("\nMAP\n");
        for (int i = 0; i < 6 ; i++) {
            for (int j = 0; j < 5 * 10; j++) {
                    System.out.print(Color. ANSI_YELLOW + "-");
            }
            System.out.print("\n");
            for (int k = 0; k <=5 * 10 ; k++) {

                if ((k % 10 == 0 || k==50) && i<5)
                    System.out.print("|");
                else
                    System.out.print(" ");
            }
            System.out.print("\n");
            for (int l = 0; l <=5 * 10 ; l++) {

                if ((l % 10 == 0 || l==50) && i<5)
                    System.out.print("|");
                else
                    System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
    /*
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
    */
    public static void main(String[] args) {
        ViewCLI v = new ViewCLI();
    }
}
