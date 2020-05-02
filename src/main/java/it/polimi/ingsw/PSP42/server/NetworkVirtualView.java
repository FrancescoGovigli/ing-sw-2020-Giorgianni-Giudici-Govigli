package it.polimi.ingsw.PSP42.Server;

import java.io.*;
import java.net.*;

public class NetworkVirtualView {

    public static void sendToClient(Socket client, Object object){
        try {
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(object);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object receiveFromClient(Socket client){
        try {
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return ("NetworkVirtualView.receiveFromClient(): IOException | ClassNotFoundException e");
        }
    }
}
