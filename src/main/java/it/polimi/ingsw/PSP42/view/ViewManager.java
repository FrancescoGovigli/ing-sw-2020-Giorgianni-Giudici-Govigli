package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.client.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;
import java.util.List;

public class ViewManager implements ClientObserver, GuiObserver {
    private static Stage stage;
    private static ClientGUI client;
    private static boolean playPushed = false;

    public ViewManager(ClientGUI client){
        this.client = client;
    }

    public static boolean isPlayPushed() {
        return playPushed;
    }

    public static void setPlayPushed(boolean playPushed) {
        ViewManager.playPushed = playPushed;
    }

    public static void setStage(Stage st) {
        stage = st;
    }

    public static Stage getStage(){
        return stage;
    }

    public static void setLayout(Scene scene, String path) {
        try {
            Parent root = FXMLLoader.load(ViewManager.class.getResource(path));
            if(scene==null) {
                Scene first = new Scene(root);
                stage.setScene(first);
                return;
            }
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public  void updateWelcomeFirstPlayer() {
        ViewManager.setLayout(ViewManager.getStage().getScene(),"/fxml/WelcomeFirstPlayerScene2.fxml");
    }

    @Override
    public void updateWelcomeOtherPlayers() {
        ViewManager.setLayout(ViewManager.getStage().getScene(), "/fxml/WelcomeNotFirstPlayerScene.fxml");
    }

    @Override
    public void updateConnectionStart() {
        while (! isPlayPushed()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateGodSelection(Object gods) {
        List list = null;
        if (gods instanceof List)
            list = (List) gods;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ChooseGodScene.fxml"));
        ControllerChooseGodScene controller = loader.getController();

        /*System.out.println("update god");
        if (list!=null) {
            System.out.println("God's list size is: " + list.size());
            for (int i = 0; i < list.size(); i++)
                System.out.println("God: " + list.get(i).toString());
        }*/

        controller.setGods(list);
        System.out.println("setta i god? Speriamo");
        ViewManager.setLayout(ViewManager.getStage().getScene(), "/fxml/ChooseGodScene.fxml");
    }

    @Override
    public void fromGuiNickName(String nick) {
        inputGui(nick);
    }

    @Override
    public void fromGuiNumberOfPlayers(String number) {
        inputGui(number);
    }

    public void inputGui(String input){
        client.saveInput(input);
    }

    public static ClientGUI getInstance(){
        return client;
    }
}
