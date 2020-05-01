package it.polimi.ingsw.PSP42.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkHandler {
    public synchronized static void sendToServer(Socket server, Object object){
        try {
            ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
            output.writeObject(object);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Object receiveFromServer(Socket server){
        try {
            ObjectInputStream input = new ObjectInputStream(server.getInputStream());
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return ("NetworkHandler.receiveFromServer(): IOException | ClassNotFoundException e");
        }
    }
}
