package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import java.io.*;

public class ControllerWelcomeScene implements GuiObservable {
    @FXML
    public TextField textfield;
    @FXML
    public GridPane chooseField;
    private boolean ableToClickPlayers = false;
    private String numberOfPlayers;
    private String nickName;
    private GuiObserver guiObserver = new ViewManager(ViewManager.getInstance());

    @FXML
    public Button choose1;

    @FXML
    public Button choose2;

    @FXML
    private Button playButton;

    /*TODO (ABBELLIMENTO) metterei nella welcomeScene uno Status Connected oppure status: Insert Your name to continue*/
    public void goToWaitingScene(ActionEvent event){
        ViewManager.setPlayPushed(true);
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/WaitingScene.fxml"));
        Parent waitingSceneParent = loader.load();
        Scene window = ((Node)event.getSource()).getScene();
        window.setRoot(waitingSceneParent);*/
    }



    public void deleteText(MouseEvent mouseEvent) {
        TextField text = (TextField) mouseEvent.getSource();
        text.setText("");
        text.setEditable(true);

    }

    public void twoPlayerChoose(MouseEvent mouseEvent) {
        if(ableToClickPlayers) {
            System.out.println("2");
            numberOfPlayers = "2";
            informManagerNumberOfPlayers(numberOfPlayers);
       }
    }

    public void threePlayerChoose(MouseEvent mouseEvent) {
        if(ableToClickPlayers) {
            System.out.println("3");
            numberOfPlayers = "3";
            informManagerNumberOfPlayers(numberOfPlayers);
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

    public void submitChoice(MouseEvent mouseEvent) {
        chooseField.setOpacity(1);
        choose1.setOpacity(1);
        choose2.setOpacity(1);
        nickName = textfield.getText();
        ableToClickPlayers = true;
        informManagerNickname(nickName);

    }

}
