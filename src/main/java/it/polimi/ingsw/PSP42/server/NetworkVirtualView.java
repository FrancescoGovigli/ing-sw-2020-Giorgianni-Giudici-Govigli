package it.polimi.ingsw.PSP42.server;

import java.io.*;
import java.net.*;

public class NetworkVirtualView {

    public static void sendToClient(ObjectOutputStream toClient, Object object){
        try {
            toClient.writeObject(object);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(Socket client, Object object){
        try {
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(object);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object receiveFromClient(BufferedReader input){
        Object obj=null;
        try {
            obj =  input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
