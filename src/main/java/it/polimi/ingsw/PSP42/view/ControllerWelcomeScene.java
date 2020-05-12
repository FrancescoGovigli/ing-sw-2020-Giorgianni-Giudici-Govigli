package it.polimi.ingsw.PSP42.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerWelcomeScene {

    private String username;

    @FXML
    private Button submitButton;

    @FXML
    private TextField textField;

    public void goToWaitingScene(ActionEvent event) throws IOException {
        username = textField.getText();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/WaitingScene.fxml"));
        Parent waitingSceneParent = loader.load();
        Scene waitingScene = new Scene(waitingSceneParent);

        ControllerWaitingScene controller = loader.getController();
        controller.setLabelUsername(username);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(waitingScene);
        window.show();
    }
}
