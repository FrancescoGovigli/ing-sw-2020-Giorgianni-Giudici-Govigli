package it.polimi.ingsw.PSP42.client;

import it.polimi.ingsw.PSP42.server.*;
import it.polimi.ingsw.PSP42.model.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements Runnable{

    private NetworkHandler net;
    private boolean active=true;
    private ObjectOutputStream output;

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
                            if(!inputObject.equals(ServerMessage.extraClient))
                                System.out.println("[FROM SERVER] :"+inputObject);
                            else {
                                System.out.println((String) inputObject);
                                socketIn.close();

                            }

                        }
                        else if (inputObject instanceof FakeCell)
                            ((FakeCell)inputObject).show(inputObject);
                        else
                            throw new IllegalArgumentException();
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
                        socketOut.println(inputLine);
                        socketOut.flush();
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
            System.out.println("Connection established");
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
}
