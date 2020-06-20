package it.polimi.ingsw.PSP42.client.clientView;

import it.polimi.ingsw.PSP42.client.GuiObservable;
import it.polimi.ingsw.PSP42.client.GuiObserver;
import it.polimi.ingsw.PSP42.server.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class ControllerWelcomeScene implements GuiObservable {

    private final GuiObserver guiObserver = new ViewManager(ViewManager.getClientInstance());

    @FXML
    public TextField textFieldFirstPlayer;
    @FXML
    public GridPane chooseField;
    @FXML
    public Label statusLabel;
    @FXML
    public Pane statusPane;
    @FXML
    public TextField textFieldOtherPlayers;
    @FXML
    public Button choose1;
    @FXML
    public Button choose2;
    @FXML
    public TextField textFieldIP;

    private boolean hostIPset = false;
    private boolean ableToClickPlayers = false;
    private String numberOfPlayers;
    private static String nickName;

    public static String getNickName() {
        return nickName;
    }

    /**
     * Used to inform ViewManager that player pushed "Play" button.
     */
    public void goToWaitingScene() {
        if (hostIPset) {
            informManagerInput(textFieldIP.getText());
            ViewManager.setPlayPushed(true);
        }
    }

    /**
     * Used to delete text in textField when clicked.
     * @param mouseEvent click of mouse on screen
     */
    public void deleteText(MouseEvent mouseEvent) {
        TextField text = (TextField) mouseEvent.getSource();
        text.setText("");
        hostIPset = true;
        text.setEditable(true);
    }

    /**
     * Used to inform view manager that first player decide for a game with two players.
     */
    public void twoPlayerChoose() {
        if (ableToClickPlayers) {
            System.out.println("2");
            numberOfPlayers = "2";
            informManagerInput(numberOfPlayers);
       }
    }

    /**
     * Used to inform view manager that first player decide for a game with three players.
     */
    public void threePlayerChoose() {
        if (ableToClickPlayers) {
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
        nickName = textFieldFirstPlayer.getText();
        if(!nickName.equals("") && !nickName.equals("Insert your nickname")) {
            chooseField.setOpacity(1);
            choose1.setOpacity(1);
            choose2.setOpacity(1);
            ableToClickPlayers = true;
            informManagerInput(nickName);
        }
    }

    /**
     * Used in "WelcomeNotFirstPlayerScene.fxml".
     * When submit button is pressed inform view manager of the name typed in text field.
     */
    public void submitName() {
        nickName = textFieldOtherPlayers.getText();
        if(!nickName.equals("") && !nickName.equals("Insert your nickname"))
         informManagerInput(nickName);
    }

    /**
     * Used to create a label to show an error message.
     * @param message
     */
    public void setStatusLabel(String message) {
        if (message.equals(ServerMessage.nameNotFree)) {
            statusPane.setStyle("-fx-background-image: url('/images/PopUp.png'); -fx-background-size: stretch; -fx-opacity: 1;");
            statusLabel.setText("Nickname already taken");
        }
        else if (message.equals(ServerMessage.extraClient) || message.equals(ServerMessage.gameInProgress)) {
            statusPane.setStyle("-fx-background-image: url('/images/PopUp.png'); -fx-background-size: stretch; -fx-opacity: 1;");
            statusLabel.setText("Cannot access the Game try later...");
        }
        else if (message.equals("Server unreachable")) {
            statusPane.setStyle("-fx-background-image: url('/images/PopUp.png'); -fx-background-size: stretch; -fx-opacity: 1;");
            statusLabel.setText("Server unreachable");
        }
    }

    /**
     * Used to inform ViewManager (observer) about a GUI choice done by Client.
     * @param input choice done
     */
    @Override
    public void informManagerInput(String input) {
        guiObserver.fromGuiInput(input);
    }
}
