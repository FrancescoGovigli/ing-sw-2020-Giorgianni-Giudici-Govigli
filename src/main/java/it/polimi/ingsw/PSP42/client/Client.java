package it.polimi.ingsw.PSP42.client;

import it.polimi.ingsw.PSP42.server.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements Runnable{

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("IP address of server? or press Enter to skip this step");
        String ip = scanner.nextLine();
        Socket server;
        try {
            server = new Socket(ip, 4000);
        } catch (IOException e) {
            System.out.println("Server unreachable");
            return;
        }
        String string = (String) NetworkHandler.receiveFromServer(server);
        System.out.println("[FROM SERVER] : " + string);   //connected
        string = (String) NetworkHandler.receiveFromServer(server); //Wait to be admitted to the Game
        if (string.equals(ServerMessage.ableToPlay)) {
            System.out.println("[FROM SERVER] : " +string);
            //PER INIZIALIZZARE IL NOME
            loopReceiveAndSend(server);
        }
        else
            System.out.println(string); //extraClient || gameInProgress
        try {
            System.out.println("Connection closed");
            server.close();
        } catch (IOException e) { }
    }

    private void loopReceiveAndSend(Socket server){
        boolean done = false;
        Scanner scanner = new Scanner(System.in);
        while(!done){
            Object obj = NetworkHandler.receiveFromServer(server);
            String string= null;
            if(obj instanceof String) {
                string = (String) obj;
                if((!string.equals(ServerMessage.endGame)) && (!string.equals(ServerMessage.extraClient)) ){
                    System.out.println("[FROM SERVER] : " + string);
                    NetworkHandler.sendToServer(server, scanner.next());
                }
                else {
                    System.out.println("[FROM SERVER] : " + string);
                    done = true;
                }
            }
        }
    }
}
