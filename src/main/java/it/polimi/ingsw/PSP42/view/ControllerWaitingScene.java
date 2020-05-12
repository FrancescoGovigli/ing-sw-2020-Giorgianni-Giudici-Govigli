package it.polimi.ingsw.PSP42.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class ControllerWaitingScene {

    @FXML
    private Label labelUsername;

    public void setLabelUsername(String username) {
        labelUsername.setText(username);
    }

    public void goToGameBoardScene(ActionEvent event) throws IOException {
        Parent waitingSceneParent = FXMLLoader.load(getClass().getResource("/fxml/GameBoardScene.fxml"));
        Scene waitingScene = new Scene(waitingSceneParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(waitingScene);
        window.show();
    }

    //useless
    public void backToWelcomeScene(ActionEvent event) throws IOException {
        Parent welcomeSceneParent = FXMLLoader.load(getClass().getResource("/fxml/WelcomeScene.fxml"));
        Scene welcomeScene = new Scene(welcomeSceneParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(welcomeScene);
        window.show();
    }
}