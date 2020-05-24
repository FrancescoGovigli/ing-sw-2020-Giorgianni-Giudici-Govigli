package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.client.*;
import it.polimi.ingsw.PSP42.model.*;
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
    private static String WELCOME_FIRST_PLAYER_SCENE_PATH = "/fxml/WelcomeFirstPlayerScene.fxml";
    private static String WAITING_SCENE_PATH = "/fxml/WaitingScene2.fxml";
    private static String WELCOME_OTHER_PLAYERS_SCENE_PATH = "/fxml/WelcomeNotFirstPlayerScene.fxml";
    private static String CHOOSE_GOD_SCENE_PATH = "/fxml/ChooseGodScene.fxml";
    private static String GAMEBOARD_SCENE_PATH = "/fxml/GameBoardScene.fxml";

    public ViewManager(ClientGUI client){
        this.client = client;
    }

    public static boolean isPlayPushed() {
        return playPushed;
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

            controllerGameBoardScene.prova();
            if(stage.getScene()==null) {
                Scene first = new Scene(root);
                stage.setScene(first);
                return;
            }
            stage.getScene().setRoot(root);
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
        while (! isPlayPushed()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateGodSelection(Object listOfGods) {
        setLayout(CHOOSE_GOD_SCENE_PATH);
        controllerChooseGodScene.setGods(listOfGods);
    }

    @Override
    public void updateWaiting() {
        setLayout(WAITING_SCENE_PATH);
    }

    @Override
    public void updateGameStatus(Object o) {
        Platform.runLater(()-> controllerWelcomeScene.setStatusLabel((String)o));
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
        FakeCell[][] gCopy = (FakeCell[][]) o;
        int DIM = gCopy.length;
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (gCopy[i][j].level != 0)
                    insertSpecificLevel(i, j, gCopy[i][j].level);
                if (gCopy[i][j].playerName != null)
                    insertSpecificPlayer(i, j, client.getPlayerData(gCopy[i][j].playerName));
            }
        }
    }

    @Override
    public void updateGameMessage(Object message) {
        Platform.runLater(()->controllerGameBoardScene.showGameMessage(message));
    }

    private void insertSpecificPlayer(int i, int j, UserData playerData) {
        Platform.runLater(()->controllerGameBoardScene.setSpecificPlayer(i,j,playerData));
    }

    /*TODO mettere isDOME() in fakecell*/
    private void insertSpecificLevel(int i, int j, int level) {
        Platform.runLater(()->controllerGameBoardScene.setSpecificLevel(i,j,level,false));
    }
}
