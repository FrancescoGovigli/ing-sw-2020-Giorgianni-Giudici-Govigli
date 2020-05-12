package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;

public class PlayerHandler{

    private Socket client;
    private int clientID;
    private boolean readyToPlay;
    private ObjectOutputStream out;
    private BufferedReader input;

    public Socket getClient() {
        return client;
    }


    public BufferedReader getInput() {
        return input;
    }

    public ObjectOutputStream getOut() {
        return out;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active=true;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private String nickName;

    public String getNickName() {
        return nickName;
    }


    public void setReadyToPlay(boolean readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    PlayerHandler(Socket client, int clientID){
        this.client = client;
        this.clientID = clientID;
        this.readyToPlay = false;
        this.active = true;
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public int getClientID() {
        return clientID;
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    /*CHIAMA SETTING CLIENT() CHE RICHIEDE IL NOME E IL NUMERO DI GIOCATORI SE
      CLIENT ID = 1 (IL PRIMO CONNESSO) E IL THREAD RIMANE ATTIVO FINO A CHE IL CLIENT E' CONNESSO
     */

    //TODO SE E' IN GIOCO FINCHE NON PERDE ACTIVE = TRUE E FIN TANTO CHE NON VIENE INILIZIALIZZATA
    //TODO LA PARTITA. CREO UN THREAD DI SOLA LETTURA CHE RIMANE IN WHILE. SETTING CLIENT() VA NEL SERVER
    //TODO CHOOSENUM() NEL SERVER ANCHE CLOSE CONNECTION().




    public void closeConnection(){
        active = false;
    }

    public Thread asyncSend(Object message){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(message);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t;
    }

    public Object asyncRead(){
        final String[] str = new String[1];
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str[0] = input.readLine();
                    System.out.println("[FROM CLIENT] :" + str[0]);


                } catch (Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str[0];
    }





}