package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.client.*;
import it.polimi.ingsw.PSP42.client.clientView.ViewManager;
import javafx.application.*;
import javafx.stage.*;

public class GUITest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ViewManager.setStage(stage);
        ViewManager.setLayout("/fxml/WelcomeScene.fxml");
        stage.show();
        ClientGUI c = new ClientGUI();
        c.attach(new ViewManager(c));
        Thread t = new Thread(c);
        t.start();
    }
}