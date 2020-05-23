package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.server.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class ControllerWelcomeScene implements GuiObservable {
    @FXML
    public TextField textfieldFirstPlayer;
    @FXML
    public GridPane chooseField;
    @FXML
    public Label statusLabel;
    @FXML
    public TextField textfieldOtherPlayers;

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
    public void goToWaitingScene(ActionEvent event) {
        ViewManager.setPlayPushed(true);
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
            informManagerInput(numberOfPlayers);
       }
    }

    public void threePlayerChoose(MouseEvent mouseEvent) {
        if(ableToClickPlayers) {
            System.out.println("3");
            numberOfPlayers = "3";
            informManagerInput(numberOfPlayers);
        }
    }

    @Override
    public void informManagerInput(String input) {
        guiObserver.fromGuiInput(input);
    }


    public void submitChoice(MouseEvent mouseEvent) {
        chooseField.setOpacity(1);
        choose1.setOpacity(1);
        choose2.setOpacity(1);
        nickName = textfieldFirstPlayer.getText();
        ableToClickPlayers = true;
        informManagerInput(nickName);
    }

    public void saveNickname(ActionEvent event){
        TextField text = (TextField)event.getSource();
        String nickName = text.getText();
        System.out.println(nickName);
        informManagerInput(nickName);
    }


    public void setStatusLabel(String message){
        if(message.equals("Name already taken choose another nickname"))
            statusLabel.setText("Status: Nickname already taken");
        else if(message.equals(ServerMessage.extraClient) || message.equals(ServerMessage.gameInProgress) )
            statusLabel.setText("Status: Cannot access the Game try later...");
        statusLabel.setOpacity(1);

    }

    public void submitName(MouseEvent mouseEvent) {
        nickName = textfieldOtherPlayers.getText();
        informManagerInput(nickName);
    }
}
