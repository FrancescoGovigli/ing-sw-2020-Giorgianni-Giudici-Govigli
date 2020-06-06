package it.polimi.ingsw.PSP42.client;

import it.polimi.ingsw.PSP42.client.clientView.ViewManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AppGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ViewManager.setStage(stage);
        ViewManager.setLayout("/fxml/WelcomeScene.fxml");
        stage.setTitle("SANTORINI by Giorgianni-Giudici-Govigli");
        stage.show();
        ClientGUI c = new ClientGUI();
        c.attach(new ViewManager(c));
        Thread t = new Thread(c);
        t.start();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
}
