package it.polimi.ingsw.PSP42.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkVirtualView {

    public synchronized static void sendToClient(Socket client, Object object){
        try {
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(object);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Object receiveFromClient(Socket client){
        try {
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return ("NetworkVirtualView.receiveFromClient(): IOException | ClassNotFoundException e");
        }
    }
}
