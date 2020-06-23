package it.polimi.ingsw.PSP42.client.clientView;

import it.polimi.ingsw.PSP42.client.GuiObservable;
import it.polimi.ingsw.PSP42.client.GuiObserver;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.UserData;
import it.polimi.ingsw.PSP42.view.ViewMessage;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.*;

public class ControllerGameBoardScene implements GuiObservable {

    private final GuiObserver guiObserver = new ViewManager(ViewManager.getClientInstance());

    @FXML
    public Pane imagePlayer1;
    @FXML
    public Pane player1Pane;
    @FXML
    public Label player1Label;

    @FXML
    public Pane imagePlayer2;
    @FXML
    public Pane player2Pane;
    @FXML
    public Label player2Label;

    @FXML
    public Pane imagePlayer3;
    @FXML
    public Pane player3Pane;
    @FXML
    public Label player3Label;

    @FXML
    public GridPane root;

    @FXML
    public GridPane board;

    @FXML
    public Label GameLabel;

    @FXML
    public Button undo;
    @FXML
    public Button power;

    @FXML
    public Button noneDefaultLevel;
    @FXML
    public Button defaultLevel;
    @FXML
    public Button dome;

    private boolean firstLabelSetting = false;

    /**
     * Used to set label with the instruction to proceed with the game.
     * Used also to set buttons style based on game's progress.
     * @param message Object used as String containing game's instructions
     */
    public void showGameMessage(Object message) {
        GameLabel.setText((String)message);
        // undo
        if (GameLabel.getText().contains("UNDO"))
            undo.setStyle(getUndoStyle("active"));
        else
            undo.setStyle(getUndoStyle("inactive"));
        // power
        if (GameLabel.getText().contains("power"))
            power.setStyle(getPowerStyle("active"));
        else
            power.setStyle(getPowerStyle("inactive"));
        // none default level
        if (isAtlas) {
            if (GameLabel.getText().contains(ClientGUIMessage.levelGUIMessage))
                noneDefaultLevel.setStyle(getNoneDefaultStyle("active"));
            else if (GameLabel.getText().contains(ClientGUIMessage.insertLevelGUIMessage)) {
                noneDefaultLevel.setStyle(getNoneDefaultStyle("levels"));
                defaultLevel.setStyle(getBuildStyle("default", "active"));
                dome.setStyle(getBuildStyle("dome", "active"));
            }
            else {
                defaultLevel.setStyle(getBuildStyle("default", "inactive"));
                dome.setStyle(getBuildStyle("dome", "inactive"));
                noneDefaultLevel.setStyle(getNoneDefaultStyle("inactive"));
            }
        }
    }

    //Used to know if current player is Atlas
    private boolean isAtlas = false;

    /**
     * Used to set default style for the scene.
     * @param userDataArrayList arraylist of data of the playing players
     * @param currentNickname String, name of the current playing player
     */
    public void setStyleScene(ArrayList<UserData> userDataArrayList, String currentNickname) {
        undo.setStyle(getUndoStyle("inactive"));
        power.setStyle(getPowerStyle("inactive"));
        for (UserData userData : userDataArrayList)
            if (userData.getNickname().equals(currentNickname))
                if (userData.getCardChosen().equals("ATLAS")) {
                    noneDefaultLevel.setStyle(getNoneDefaultStyle("inactive"));
                    isAtlas = true;
                }
                else {
                    noneDefaultLevel.setStyle(getNoneDefaultStyle("none"));
                    isAtlas = false;
                }
        defaultLevel.setStyle(getBuildStyle("default", "inactive"));
        dome.setStyle(getBuildStyle("dome", "inactive"));
    }

    /**
     * Send to view manager "YES" when clicked on undo button to UNDO.
     */
    public void doUndo() {
        if (GameLabel.getText().contains("UNDO")) {
            informManagerInput("YES");
        }
    }

    /**
     * Used to obtain Undo Button CSS style.
     * @param style what style this method has to set
     * @return CSS style as a string
     */
    private String getUndoStyle(String style) {
        String styleToSet;
        if (style.equals("active"))
            styleToSet = "-fx-background-image: url('/images/undo_active.png');";
        else
            styleToSet = "-fx-background-image: url('/images/undo_inactive.png');";
        return styleToSet + " -fx-background-size: stretch; -fx-background-color: transparent;";
    }

    /**
     * Send to view manager "YES" when clicked on power button to activate power.
     */
    public void doPower() {
        if (GameLabel.getText().contains("power"))
            informManagerInput("YES");
    }

    /**
     * Used to obtain Power Button CSS style.
     * @param style what style this method has to set
     * @return CSS style as a string
     */
    private String getPowerStyle(String style) {
        String styleToSet;
        if (style.equals("active"))
            styleToSet = "-fx-background-image: url('/images/heropower_active.png');";
        else
            styleToSet = "-fx-background-image: url('/images/heropower_inactive.png');";
        return styleToSet + " -fx-background-size: stretch; -fx-background-color: transparent;";
    }

    /**
     * Send to view manager "YES" when clicked on none default button to activate default and dome button.
     */
    public void doNoneDefaultLevel() {
        if (GameLabel.getText().contains(ClientGUIMessage.levelGUIMessage))
            informManagerInput("YES");
    }

    /**
     * Used to obtain None Default Button CSS style.
     * @param style what style this method has to set
     * @return CSS style as a string
     */
    private String getNoneDefaultStyle(String style) {
        String styleToSet;
        if (style.equals("active"))
            styleToSet = "-fx-background-image: url('/images/build_active.png');";
        else if (style.equals("inactive"))
            styleToSet = "-fx-background-image: url('/images/build_inactive.png');";
        else
            return "-fx-opacity: 0;";
        return styleToSet + " -fx-background-size: stretch; -fx-background-color: transparent; -fx-opacity: 1;";
    }

    /**
     * Send to view manager, when clicked, to build a default level.
     */
    public void buildDefaultLevel() {
        // When it's not visible
        if (GameLabel.getText().contains(ClientGUIMessage.levelGUIMessage))
            informManagerInput("YES");
        // When it's visible
        if (GameLabel.getText().contains(ClientGUIMessage.insertLevelGUIMessage))
            informManagerInput("1");
    }

    /**
     * Send to view manager, when clicked, to build a dome.
     */
    public void buildDome() {
        // When it's not visible
        if (GameLabel.getText().contains(ClientGUIMessage.levelGUIMessage))
            informManagerInput("YES");
        // When it's visible
        if (GameLabel.getText().contains(ClientGUIMessage.insertLevelGUIMessage))
            informManagerInput("4");
    }

    /**
     * Used to obtain Dome Button and DefaultLevel Button CSS styles.
     * @param style what style this method has to set
     * @return CSS style as a string
     */
    private String getBuildStyle(String type, String style) {
        String styleToSet;
        if (!(style.equals("active")))
            return "-fx-opacity: 0;";
        else {
            if (type.equals("dome"))
                styleToSet = "-fx-background-image: url('/images/lvl1dome.png');";
            else
                styleToSet = "-fx-background-image: url('/images/lvl3.png');";
        }
        return styleToSet + " -fx-background-size: stretch; -fx-background-color: transparent; -fx-opacity: 1; ";
    }


    /**
     * Used to set style for the list of players.
     * @param playerList list to know all players
     * @param currentNickname String to know who is the current player playing
     */
    public void setPlayersLabel(ArrayList<UserData> playerList, String currentNickname) {
        Pane[] workers = {imagePlayer1, imagePlayer2, imagePlayer3};
        Pane[] panes = {player1Pane, player2Pane, player3Pane};
        Label[] labels = {player1Label, player2Label, player3Label};
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getNickname().equals(currentNickname))
                panes[i].setId("currentPlayer");
            else
                panes[i].setId("player");
            if (!firstLabelSetting) {
                ImageView image = new ImageView(GameBoardElementsPath.getGodPlayerImagePath(playerList.get(i).getCardChosen().toLowerCase()));
                workers[i].getChildren().add(image);
                image.fitWidthProperty().bind(workers[i].widthProperty());
                image.fitHeightProperty().bind(workers[i].heightProperty());
                workers[i].setOpacity(1);
                panes[i].setOpacity(1);
                labels[i].setOpacity(1);
                labels[i].setText(playerList.get(i).getNickname());
            }
        }
        firstLabelSetting = true;
    }

    /**
     * Used to know in what cell of the game board the player click.
     * This information was sent to View Manager to process.
     * @param mouseEvent click on game board
     */
    public void playerChoice(MouseEvent mouseEvent) {
        FakeCell[][] checkBoard = ViewManager.getGameBoardState();
        Node cell = (Node)mouseEvent.getSource();
        Integer row = board.getRowIndex(cell);
        Integer col = board.getColumnIndex(cell);
        Pane pane = (Pane)cell;
        if (GameLabel.getText().equals(ClientGUIMessage.workerGUIMessage)) {
            for (Node node : pane.getChildren()) {
                if (node instanceof ImageView && node.getId().equals("Worker")) {
                    if (checkBoard[row][col].playerName.equals(ControllerWelcomeScene.getNickName())) {
                        if (checkBoard[row][col].workerNum == 1)
                            informManagerInput("1");
                        else
                            informManagerInput("2");
                        return;
                    }
                }
            }
        }
        informManagerInput(row.toString() + "," + col.toString());
    }

    /**
     * Method to set the appropriate construction on the GUI_GameBoard,
     * considering the last built level and the one below.
     * @param row int row
     * @param col int column
     * @param level int, level to set of the (row, column)'s cell
     * @param previousBuiltLevel int, previous level on (row, column)'s cell
     */
    public void setImageSpecificLevel(int row, int col, int level, int previousBuiltLevel) {
        Pane cell = (Pane)getPaneFromBoard(row, col);
        ImageView image = getLevelImage(level, previousBuiltLevel);
        image.setId("Block");
        if (image == null)
            return;
        else {
            image.fitWidthProperty().bind(cell.widthProperty());
            image.fitHeightProperty().bind(cell.heightProperty());

            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(image.getImage(), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            Background background = new Background(backgroundImage);
            cell.setBackground(background);
        }
    }

    /**
     * Method to set the appropriate player in the indicated position on the GUI_GameBoard.
     * @param row int, row
     * @param col int, column
     * @param playerData UserData, data of the player to set in (row, column)'s cell
     */
    public void setImageSpecificPlayer(int row, int col, UserData playerData) {
        Pane cell = (Pane)getPaneFromBoard(row, col);
        if (playerData != null) {
            ImageView image = getWorkerImage(playerData.getCardChosen());
            image.setId("Worker");
            if (image == null)
                return;
            else {
                image.fitWidthProperty().bind(cell.widthProperty());
                    image.fitHeightProperty().bind(cell.heightProperty());
                    cell.getChildren().add(image);
            }
        }
        else {
           Platform.runLater(() -> {for (Node node : cell.getChildren()) {
               if (node instanceof ImageView && node.getId().equals("Worker"))
                   cell.getChildren().remove(node);
           }});
        }
    }

    /**
     * Used to obtain pane from the game board.
     * @param row int, row
     * @param col int, column
     * @return Node (Pane)
     */
    private Node getPaneFromBoard(int row, int col) {
        for (Node node : board.getChildren()) {
            if (board.getColumnIndex(node) == col && board.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Method to obtain the image corresponding to the specified level, also considering the level on which it rests.
     * @param level int, representing the level to build
     * @param previousBuiltLevel int, representing the precedent level of level
     * @return the corresponding image
     */
    private ImageView getLevelImage(int level, int previousBuiltLevel) {
        if (GameBoardElementsPath.getLevelImagePath(level, previousBuiltLevel) != null)
            return new ImageView(GameBoardElementsPath.getLevelImagePath(level, previousBuiltLevel));
        else
            return null;
    }

    /**
     * Method to obtain the image (as a pawn) of the God that the player has.
     * @param worker String, name of god used to represent workers
     * @return the corresponding image
     */
    private ImageView getWorkerImage(String worker) {
        if (GameBoardElementsPath.getWorkerImagePath(worker) != null)
            return new ImageView(GameBoardElementsPath.getWorkerImagePath(worker));
        else
            return null;
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
