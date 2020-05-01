package it.polimi.ingsw.PSP42.client;

import it.polimi.ingsw.PSP42.server.ServerMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("IP address of server? or press Enter to skip this step");
        String ip = scanner.nextLine();
        Socket server;
        try {
            server = new Socket(ip, 4040);
        } catch (IOException e) {
            System.out.println("Server unreachable");
            return;
        }
        String string = (String) NetworkHandler.receiveFromServer(server);
        System.out.println("From Server: " + string);   //connected
        string = (String) NetworkHandler.receiveFromServer(server);
        if (string.equals(ServerMessage.ableToPlay)) {
            System.out.println(string);
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
            String string = (String) NetworkHandler.receiveFromServer(server);
            if(!string.equals(ServerMessage.endGame)){
                System.out.println("You have receive: " + string);
                NetworkHandler.sendToServer(server, scanner.next());
            }
            else
                done = true;
        }
    }
}
