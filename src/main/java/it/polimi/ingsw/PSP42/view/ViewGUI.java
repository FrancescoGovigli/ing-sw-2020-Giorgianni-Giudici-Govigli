package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.client.*;
import javafx.application.*;
import javafx.stage.*;

public class ViewGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
            //stage.setResizable(false);
            ViewManager.setStage(stage);
            ViewManager.setLayout("/fxml/GameBoardScene.fxml");
            stage.show();
            ClientGUI c = new ClientGUI();
            c.attach(new ViewManager(c));
            Thread t = new Thread(c);
            t.start();
    }
}