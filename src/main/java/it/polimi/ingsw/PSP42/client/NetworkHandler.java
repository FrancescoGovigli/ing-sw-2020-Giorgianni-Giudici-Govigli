package it.polimi.ingsw.PSP42.client;

import java.io.*;
import java.net.*;

public class NetworkHandler {

    public  static void sendToServer(Socket server, Object object){
        try {
            ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
            output.writeObject(object);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static Object receiveFromServer(Socket server){
        try {
            ObjectInputStream input = new ObjectInputStream(server.getInputStream());
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return ("NetworkHandler.receiveFromServer(): IOException | ClassNotFoundException e");
        }
    }
}
