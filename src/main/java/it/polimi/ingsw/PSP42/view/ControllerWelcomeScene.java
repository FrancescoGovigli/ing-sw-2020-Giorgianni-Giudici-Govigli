package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.server.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class ControllerWelcomeScene implements GuiObservable {

    private final GuiObserver guiObserver = new ViewManager(ViewManager.getInstance());

    @FXML
    public TextField textfieldFirstPlayer;
    @FXML
    public GridPane chooseField;
    @FXML
    public Label statusLabel;
    @FXML
    public Pane statusPane;
    @FXML
    public TextField textfieldOtherPlayers;
    @FXML
    public Button choose1;
    @FXML
    public Button choose2;

    private boolean ableToClickPlayers = false;
    private String numberOfPlayers;
    private static String nickName;

    public static String getNickName() {
        return nickName;
    }

    /**
     * Used to inform ViewManager that player pushed "Play" button.
     */
    /*TODO (ABBELLIMENTO) metterei nella welcomeScene uno Status Connected oppure status: Insert Your name to continue*/
    public void goToWaitingScene() {
        ViewManager.setPlayPushed(true);
    }

    /**
     * Used to delete text in textfield when clicked.
     * @param mouseEvent click of mouse on screen
     */
    public void deleteText(MouseEvent mouseEvent) {
        TextField text = (TextField) mouseEvent.getSource();
        text.setText("");
        text.setEditable(true);
    }

    /**
     * Used to inform view manager that first player decide for a game with two players.
     */
    public void twoPlayerChoose() {
        if(ableToClickPlayers) {
            System.out.println("2");
            numberOfPlayers = "2";
            informManagerInput(numberOfPlayers);
       }
    }

    /**
     * Used to inform view manager that first player decide for a game with three players.
     */
    public void threePlayerChoose() {
        if(ableToClickPlayers) {
            System.out.println("3");
            numberOfPlayers = "3";
            informManagerInput(numberOfPlayers);
        }
    }

    /**
     * Used in "WelcomeFirstPlayerScene.fxml".
     * When submit button is pressed show other buttons.
     * Also, inform view manager of the name typed in text field.
     */
    public void submitChoice() {
        chooseField.setOpacity(1);
        choose1.setOpacity(1);
        choose2.setOpacity(1);
        nickName = textfieldFirstPlayer.getText();
        ableToClickPlayers = true;
        informManagerInput(nickName);
    }

    /**
     * Used in "WelcomeNotFirstPlayerScene.fxml".
     * When submit button is pressed inform view manager of the name typed in text field.
     */
    public void submitName() {
        nickName = textfieldOtherPlayers.getText();
        informManagerInput(nickName);
    }

    public void setStatusLabel(String message){
        if(message.equals("Name already taken choose another nickname")) {
            statusPane.setStyle("-fx-background-image: url('/images/PopUp.png'); -fx-background-size: stretch; -fx-opacity: 1;");
            statusLabel.setText("Status: Nickname already taken");
        }
        else if(message.equals(ServerMessage.extraClient) || message.equals(ServerMessage.gameInProgress)) {
            statusPane.setStyle("-fx-background-image: url('/images/PopUp.png'); -fx-background-size: stretch; -fx-opacity: 1;");
            statusLabel.setText("Status: Cannot access the Game try later...");
        }
    }

    @Override
    public void informManagerInput(String input) {
        guiObserver.fromGuiInput(input);
    }
}
