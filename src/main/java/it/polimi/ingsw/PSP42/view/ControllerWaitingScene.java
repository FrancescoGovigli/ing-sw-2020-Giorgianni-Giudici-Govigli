package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;

import java.io.*;

public class ControllerWaitingScene  implements GuiObservable  {
    @FXML
    public Label statusLabel;
    private GuiObserver guiObserver = new ViewManager(ViewManager.getInstance());
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

    public void saveNickname(ActionEvent event){
        TextField text = (TextField)event.getSource();
        String nickName = text.getText();
        System.out.println(nickName);
        informManagerNickname(nickName);
    }

    public void deleteText(MouseEvent mouseEvent) {
        TextField text = (TextField) mouseEvent.getSource();
        text.setText("");
        text.setEditable(true);

    }

    @Override
    public void informManagerNickname(String nick) {
        guiObserver.fromGuiNickName(nick);
    }

    @Override
    public void informManagerNumberOfPlayers(String number) {
    }

    public void setExistingLabel(){
        statusLabel.setOpacity(1);

    }
}