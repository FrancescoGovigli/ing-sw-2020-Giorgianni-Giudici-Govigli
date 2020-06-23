package it.polimi.ingsw.PSP42.client.clientView;

import it.polimi.ingsw.PSP42.client.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.server.*;
import it.polimi.ingsw.PSP42.view.UserData;
import it.polimi.ingsw.PSP42.view.ViewMessage;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

/**
 * Class used to update scene to ClientGUI.
 */
public class ViewManager implements ClientObserver, GuiObserver {

    private static Stage stage;
    private static ClientGUI client;
    private static ControllerWelcomeScene controllerWelcomeScene;
    private static ControllerGameBoardScene controllerGameBoardScene;
    private static ControllerChooseGodScene controllerChooseGodScene;
    private static ControllerDisconnectionScene controllerDisconnectionScene;
    private final static String WELCOME_SCENE = "/fxml/WelcomeScene.fxml";
    private final static String WELCOME_FIRST_PLAYER_SCENE_PATH = "/fxml/WelcomeFirstPlayerScene.fxml";
    private final static String WAITING_SCENE_PATH = "/fxml/WaitingScene.fxml";
    private final static String WELCOME_OTHER_PLAYERS_SCENE_PATH = "/fxml/WelcomeNotFirstPlayerScene.fxml";
    private final static String CHOOSE_GOD_SCENE_PATH = "/fxml/ChooseGodScene.fxml";
    private final static String GAME_BOARD_SCENE_PATH = "/fxml/GameBoardScene.fxml";
    private final static String DISCONNECTION_SCENE_PATH = "/fxml/DisconnectionScene.fxml";
    private final static String END_GAME_SCENE_PATH = "/fxml/EndGameScene.fxml";
    private final static String LOSER_SCENE_PATH = "/fxml/LoserScene.fxml";
    private static FakeCell[][] gameBoardState;
    private static boolean playPushed = false;
    private static String currentNickname;

    /**
     * Constructor used to link this class with Client.
     * @param client class ClientGUI
     */
    public ViewManager(ClientGUI client) {
        ViewManager.client = client;
    }

    public static void setStage(Stage stage) {
        ViewManager.stage = stage;
    }

    /**
     * Used to close stage.
     */
    public static void closeWindow() {
        stage.close();
    }

    public static ClientGUI getClientInstance() {
        return client;
    }

    public static boolean isPlayPushed() {
        return playPushed;
    }

    public static void setPlayPushed(boolean playPushed) {
        ViewManager.playPushed = playPushed;
    }

    /**
     * Used to know current player nickname.
     * @param o game's message sent from Server
     * @return nickname of current player as string
     */
    public String getCurrentNickname(Object o) {
        String turn = (String)o;
        for (UserData u : client.getPlayersList()) {
            if (turn.contains(u.getNickname()))
                return u.getNickname();
        }
        return null;
    }

    public static FakeCell[][] getGameBoardState() {
        return gameBoardState;
    }

    /** Used to save input in Client.
     * @param input translated from GUI's action in string
     */
    public void inputGui(String input) {
        client.saveInput(input);
    }

    /**
     * Used to set Scene layout.
     * @param path string to set correct layout for the scene
     */
    public static void setLayout(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(path));
            Parent root = loader.load();
            switch (path) {
                case WELCOME_SCENE:
                case WELCOME_FIRST_PLAYER_SCENE_PATH:
                case WELCOME_OTHER_PLAYERS_SCENE_PATH:
                    controllerWelcomeScene = loader.getController();
                    break;
                case CHOOSE_GOD_SCENE_PATH:
                    controllerChooseGodScene = loader.getController();
                    break;
                case GAME_BOARD_SCENE_PATH:
                    controllerGameBoardScene = loader.getController();
                    break;
                case DISCONNECTION_SCENE_PATH:
                case LOSER_SCENE_PATH:
                case END_GAME_SCENE_PATH:
                    controllerDisconnectionScene = loader.getController();
                    break;
            }
            if (stage.getScene() == null) {
                Scene first = new Scene(root);
                stage.setScene(first);
                return;
            }
            Platform.runLater(() -> stage.getScene().setRoot(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used by Client to inform view that Server is unreachable due to incorrect IP.
     * @param error String
     */
    public static void hostIPIncorrect(String error) {
        Platform.runLater(() -> controllerWelcomeScene.setStatusLabel(error));
        playPushed = false;
    }

    /**
     * Method used to check if the current player has Atlas.
     * Helpful to set the correct layout for player who is using Atlas.
     * @return true if current player is using Atlas, false otherwise
     */
    private boolean isAtlas() {
        for (int i = 0; i < client.getPlayersList().size(); i++)
            if (client.getPlayersList().get(i).getNickname().equals(currentNickname))
                return client.getPlayersList().get(i).getCardChosen().equals("ATLAS");
        return false;
    }

    /**
     * Method used to set scene layout for first player connected.
     */
    @Override
    public void updateWelcomeFirstPlayer() {
        setLayout(WELCOME_FIRST_PLAYER_SCENE_PATH);
    }

    /**
     * Method used to set scene layout for players that aren't first connected.
     */
    @Override
    public void updateWelcomeOtherPlayers() {
        setLayout(WELCOME_OTHER_PLAYERS_SCENE_PATH);
    }

    /**
     * Method used to wait until Player presses Play Button in Welcome Scene.
     */
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

    /**
     * Method used to set God's chosen scene layout.
     * @param listOfGods Gods available for choice
     */
    @Override
    public void updateGodSelection(Object listOfGods) {
        setLayout(CHOOSE_GOD_SCENE_PATH);
        Platform.runLater(() -> controllerChooseGodScene.setGods(listOfGods));
    }

    /**
     * Method used to set Waiting scene layout.
     */
    @Override
    public void updateWaiting() {
        setLayout(WAITING_SCENE_PATH);
    }

    /**
     * Method used to set label to inform player about his status.
     * @param o player status
     */
    @Override
    public void updateGameStatus(Object o) {
        Platform.runLater(() -> controllerWelcomeScene.setStatusLabel((String) o));
    }

    /**
     * Method used to set scene layout based on message received.
     * @param message received from Client
     */
    @Override
    public void updateGameMessage(Object message) {
        if (controllerGameBoardScene != null) {
            if (message.equals(ServerMessage.inactivityEnd) || message.equals(ServerMessage.disconnectionEnd)) {
                setLayout(DISCONNECTION_SCENE_PATH);
                Platform.runLater(() -> controllerDisconnectionScene.showMessage(message));
            }
            else if (((String)message).contains("You lost")) {
                setLayout(LOSER_SCENE_PATH);
                Platform.runLater(() -> controllerDisconnectionScene.showMessage(message));
            }
            else if (((String)message).contains("You won") || ((String)message).contains("won the Game")) {
                setLayout(END_GAME_SCENE_PATH);
                Platform.runLater(() -> controllerDisconnectionScene.setPodium(client.getPlayersList(), currentNickname));
                Platform.runLater(() -> controllerDisconnectionScene.showMessage(message));
            }
            else if (((String)message).contains("none default") && !isAtlas()) {
                fromGuiInput("NO"); //used to avoid the request to build a none default level
            }
            else {
                if (((String)message).contains(ViewMessage.yourTurnMessage) || ((String)message).contains(ViewMessage.waitingYourTurn)) {
                    currentNickname = getCurrentNickname(message);
                    Platform.runLater(() -> controllerGameBoardScene.setPlayersLabel(client.getPlayersList(), currentNickname));
                }
                Object translatedMessage = ClientGUIMessage.translateMessage((String) message);
                Platform.runLater(() -> controllerGameBoardScene.showGameMessage(translatedMessage));
            }
        }
        else if (message.equals(ServerMessage.disconnectionEnd) || message.equals(ServerMessage.inactivityEnd)) {
            setLayout(DISCONNECTION_SCENE_PATH);
            Platform.runLater(() -> controllerDisconnectionScene.showMessage(message));
        }
    }

    /**
     * Method to print the current GameBoard situation on screen.
     * @param o gameBoard to show
     */
    @Override
    public void updateShow(Object o) {
        setLayout(GAME_BOARD_SCENE_PATH);
        Platform.runLater(() -> controllerGameBoardScene.setPlayersLabel(client.getPlayersList(), currentNickname));
        Platform.runLater(()-> controllerGameBoardScene.setStyleScene(client.getPlayersList(), currentNickname));
        FakeCell[][] gCopy = (FakeCell[][]) o;
        gameBoardState = gCopy;
        int DIM = gCopy.length;
        int level;
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
                    Platform.runLater(() -> controllerGameBoardScene.setImageSpecificLevel(finalI,finalJ, finalLevel, finalPreviousBuiltLevel));
                }
                if (gCopy[i][j].playerName != null)
                    Platform.runLater(() -> controllerGameBoardScene.setImageSpecificPlayer(finalI, finalJ, client.getPlayerData(gCopy[finalI][finalJ].playerName)));
                else
                    Platform.runLater(() -> controllerGameBoardScene.setImageSpecificPlayer(finalI, finalJ, null));
            }
        }
    }

    /**
     * Used to convert a GUI action into an input.
     * @param input as String
     */
    @Override
    public void fromGuiInput(String input) {
        inputGui(input);
    }
}
