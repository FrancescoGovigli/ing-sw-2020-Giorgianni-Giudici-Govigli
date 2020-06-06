package it.polimi.ingsw.PSP42.client.clientView;

import it.polimi.ingsw.PSP42.view.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class ControllerDisconnectionScene {

    @FXML
    public Label disconnectionLabel;

    @FXML
    public Label winnerPlayer;

    @FXML
    public Label loserPlayer1;
    @FXML
    public Pane loserPlayer1Pane;
    @FXML
    public Pane loserPlayer1Medal;

    @FXML
    public Label loserPlayer2;
    @FXML
    public Pane loserPlayer2Pane;
    @FXML
    public Pane loserPlayer2Medal;

    /**
     * Used to set text for the label to define why game is ended (lose, win or disconnection).
     * @param message from server containing the sentence to have to show
     */
    public void showMessage(Object message) {
        disconnectionLabel.setText((String)message);
    }

    /**
     * Used to end program.
     */
    public void exitApp(){
        ViewManager.closeWindow();
    }

    /**
     * Used to set final results of the game.
     * @param userDataArrayList arraylist with all data of the players
     * @param winnerNickname name of the winner
     */
    public void setPodium(ArrayList<UserData> userDataArrayList, String winnerNickname) {
        winnerPlayer.setText(winnerNickname);
        int numberOfLosers = 0;
        for (UserData userData : userDataArrayList) {
            String loserNickName = userData.getNickname();
            if (!loserNickName.equals(winnerNickname)) {
                numberOfLosers++;
                setLoser(loserNickName, numberOfLosers);
            }
        }
    }

    /**
     * Used to set losers' labels.
     * @param nickName name of the loser
     * @param numberOfLosers 1 or 2, it depends on game's running
     */
    private void setLoser(String nickName, int numberOfLosers) {
        if (numberOfLosers == 1) {
            loserPlayer1.setText(nickName);
            loserPlayer1Pane.setOpacity(1);
            loserPlayer1Medal.setOpacity(1);
        } else if (numberOfLosers == 2) {
            loserPlayer2.setText(nickName);
            loserPlayer2Pane.setOpacity(1);
            loserPlayer2Medal.setOpacity(1);
        }
    }
}
