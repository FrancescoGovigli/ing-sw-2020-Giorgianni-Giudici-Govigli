package it.polimi.ingsw.PSP42.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/WelcomeScene.fxml"));
            stage.setTitle("Santorini by Giorgianni-Giudici-Govigli");
            stage.setScene(new Scene(root, 400, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
