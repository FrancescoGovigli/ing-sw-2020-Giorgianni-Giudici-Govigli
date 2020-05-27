package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.client.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.server.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

public class ViewManager implements ClientObserver,GuiObserver {

    private static Stage stage;
    private static ClientGUI client;
    private static ControllerWelcomeScene controllerWelcomeScene;
    private static ControllerGameBoardScene controllerGameBoardScene;
    private static ControllerChooseGodScene controllerChooseGodScene;
    private static boolean playPushed = false;
    private static String CURRENT_SCENE_PATH;
    private static String currentNickname;
    private static String WELCOME_FIRST_PLAYER_SCENE_PATH = "/fxml/WelcomeFirstPlayerScene.fxml";
    private static String WAITING_SCENE_PATH = "/fxml/WaitingScene2.fxml";
    private static String WELCOME_OTHER_PLAYERS_SCENE_PATH = "/fxml/WelcomeNotFirstPlayerScene.fxml";
    private static String CHOOSE_GOD_SCENE_PATH = "/fxml/ChooseGodScene.fxml";
    private static String GAMEBOARD_SCENE_PATH = "/fxml/GameBoardScene.fxml";
    private static String DISCONNECTION_SCENE_PATH = "/fxml/DisconnectionScene.fxml";
    private static FakeCell[][] gameBoardState;

    public ViewManager(ClientGUI client){
        this.client = client;
    }

    public static boolean isPlayPushed() {
        return playPushed;
    }

    public static FakeCell[][] getGameBoardState() {
        return gameBoardState;
    }

    public static void setPlayPushed(boolean playPushed) {
        ViewManager.playPushed = playPushed;
    }

    public static void setStage(Stage st) {
        stage = st;
    }

    public static Stage getStage(){
        return stage;
    }

    public static void setLayout(/*Scene scene, */String path) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(path));
            CURRENT_SCENE_PATH = path;
            Parent root = loader.load();
            if(path.equals(WELCOME_OTHER_PLAYERS_SCENE_PATH))
                controllerWelcomeScene = loader.getController();
            else if (path.equals(CHOOSE_GOD_SCENE_PATH))
                controllerChooseGodScene = loader.getController();
            else if(path.equals(GAMEBOARD_SCENE_PATH))
                controllerGameBoardScene = loader.getController();
            else if(path.equals(DISCONNECTION_SCENE_PATH))
                controllerGameBoardScene = loader.getController();

            if(stage.getScene()==null) {
                Scene first = new Scene(root);
                stage.setScene(first);
                return;
            }
            Platform.runLater(()->stage.getScene().setRoot(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public  void updateWelcomeFirstPlayer() {
        setLayout(WELCOME_FIRST_PLAYER_SCENE_PATH);
    }

    @Override
    public void updateWelcomeOtherPlayers() {
        setLayout(WELCOME_OTHER_PLAYERS_SCENE_PATH);
    }

    @Override
    public void updateConnectionStart() {
        while (!isPlayPushed()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateGodSelection(Object listOfGods) {
        setLayout(CHOOSE_GOD_SCENE_PATH);
        Platform.runLater(()->controllerChooseGodScene.setGods(listOfGods));
    }

    @Override
    public void updateWaiting() {
        setLayout(WAITING_SCENE_PATH);
    }


    @Override
    public void updateGameStatus(Object o) {
        Platform.runLater(() -> controllerWelcomeScene.setStatusLabel((String) o));

    }

    @Override
    public void updateGameMessage(Object message) {
        if(controllerGameBoardScene!=null) {
            if (! message.equals(ServerMessage.inactivityEnd)) {
                if(((String)message).contains(ViewMessage.yourTurnMessage) || ((String)message).contains(ViewMessage.waitingYourTurn)) {
                    currentNickname = getCurrentNickname(message);
                    Platform.runLater(() -> controllerGameBoardScene.setPlayersLabel(client.getPlayersList(),currentNickname));
                }

                Platform.runLater(() -> controllerGameBoardScene.showGameMessage(message));
                return;
            }
            setLayout(DISCONNECTION_SCENE_PATH);
            Platform.runLater(() -> controllerGameBoardScene.showGameMessage(message));
        }
    }


    @Override
    public void fromGuiInput(String input) {
        inputGui(input);
    }


    public void inputGui(String input){
        client.saveInput(input);
    }

    public static ClientGUI getInstance(){
        return client;
    }


    /**
     * Method to print the current GameBoard situation on the screen
     */
    @Override
    public void updateShow(Object o) {
        setLayout(GAMEBOARD_SCENE_PATH);
        Platform.runLater(() -> controllerGameBoardScene.setPlayersLabel(client.getPlayersList(),currentNickname));

        FakeCell[][] gCopy = (FakeCell[][]) o;
        gameBoardState = gCopy;
        int DIM = gCopy.length;
        int level = 0;
        int previousBuiltLevel = 0;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                int finalI = i;
                int finalJ = j;
                if (gCopy[i][j].level != 0) {
                    level = gCopy[i][j].level;
                    for (int k = level - 1; k >= 0; k--) {
                        if (gCopy[i][j].builtLevel[k]) {
                            previousBuiltLevel = k;
                            k = 0;
                        }
                    }

                    int finalPreviousBuiltLevel = previousBuiltLevel;
                    int finalLevel = level;
                    Platform.runLater(()->controllerGameBoardScene.setSpecificLevel(finalI,finalJ, finalLevel, finalPreviousBuiltLevel));

                }

                if (gCopy[i][j].playerName != null)
                    Platform.runLater(()-> controllerGameBoardScene.setSpecificPlayer(finalI, finalJ, client.getPlayerData(gCopy[finalI][finalJ].playerName)));
                else
                    Platform.runLater(()-> controllerGameBoardScene.setSpecificPlayer(finalI, finalJ, null));
            }
        }




    }

    public String getCurrentNickname(Object o){
        String turn = (String)o;
        for (UserData u: client.getPlayersList()) {
            if(turn.contains(u.getNickname()))
                return u.getNickname();
        }
       return null;
    }


    public static void closeWindow(){
        stage.close();
    }
}
