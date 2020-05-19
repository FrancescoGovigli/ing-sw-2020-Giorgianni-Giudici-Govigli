package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

import java.io.*;

public class ControllerWelcomeScene implements GuiObservable {
    private boolean ableToClickPlayers = false;
    private GuiObserver guiObserver = new ViewManager(ViewManager.getInstance());
    @FXML
    public Button choose1;
    @FXML
    public Button choose2;


    @FXML
    private Button playButton;

    /*TODO (ABBELLIMENTO) metterei nella welcomeScene uno Status Connected oppure status: Insert Your name to continue*/
    public void goToWaitingScene(ActionEvent event) throws IOException{
        ViewManager.setPlayPushed(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/WaitingScene.fxml"));
        Parent waitingSceneParent = loader.load();
        Scene window = ((Node)event.getSource()).getScene();
        window.setRoot(waitingSceneParent);
    }

    public void saveNickname(ActionEvent event){
      TextField text = (TextField)event.getSource();
      String nickName = text.getText();
      ableToClickPlayers = true;
      informManagerNickname(nickName);
    }


    public void deleteText(MouseEvent mouseEvent) {
        TextField text = (TextField) mouseEvent.getSource();
        text.setText("");
        text.setEditable(true);

    }

    public void twoPlayerChoose(MouseEvent mouseEvent) {
        if(ableToClickPlayers) {
            System.out.println("2");
            String number = "2";
            informManagerNumberOfPlayers(number);
        }
    }

    public void threePlayerChoose(MouseEvent mouseEvent) {
        if(ableToClickPlayers) {
            System.out.println("3");
            String number = "3";
            informManagerNumberOfPlayers(number);
        }
    }

    @Override
    public void informManagerNickname(String nick) {
        guiObserver.fromGuiNickName(nick);
    }

    @Override
    public void informManagerNumberOfPlayers(String number) {
        guiObserver.fromGuiNumberOfPlayers(number);
    }
}
