package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.server.*;
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


    public Label player1Label;
    public Label player2Label;
    public Label player3Label;

    public Pane imagePlayer1;
    public Pane imagePlayer2;
    public Pane imagePlayer3;
    private GuiObserver guiObserver = new ViewManager(ViewManager.getInstance());
    @FXML
    public GridPane root;
    @FXML
    public GridPane board;
    @FXML
    public ImageView worker1;
    @FXML
    public Label GameLabel;
    @FXML
    public Label DisconnectionLabel;

    /**
     * Method to set the appropriate construction on the GUI_GameBoard,
     * considering the last built level and the one below
     * @param row
     * @param col
     * @param level
     * @param previousBuiltLevel
     */
    public void setSpecificLevel(int row, int col, int level, int previousBuiltLevel) {
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
     * Method to set the appropriate player in the indicated position on the GUI_GameBoard
     * @param row
     * @param col
     * @param playerData
     */
    public void setSpecificPlayer(int row, int col, UserData playerData) {
        Pane cell = (Pane)getPaneFromBoard(row, col);
        if (playerData != null) {
            ImageView image = getWorkerImage(playerData.getCardChoosed());
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
           Platform.runLater(()->{for (Node node : cell.getChildren()) {
               if (node instanceof ImageView && node.getId().equals("Worker"))
                   cell.getChildren().remove(node);
           }});
        }
    }

    public void showGameMessage(Object message) {
        if(message.equals(ServerMessage.inactivityEnd)){
            DisconnectionLabel.setText((String)message);
            DisconnectionLabel.setTextAlignment(TextAlignment.CENTER);
            return;
        }
        GameLabel.setText((String)message);
        GameLabel.setTextAlignment(TextAlignment.CENTER);



    }

    private Node getPaneFromBoard(int row, int col) {
        for (Node node : board.getChildren()) {
            if (board.getColumnIndex(node) == col && board.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Method to obtain the image corresponding to the specified level, also considering the level on which it rests
     * @param level
     * @param previousBuiltLevel
     * @return the corresponding image
     */
    private ImageView getLevelImage(int level, int previousBuiltLevel) {
        if(GameBoardElementsPath.getLevelImagePath(level, previousBuiltLevel) != null)
            return new ImageView(GameBoardElementsPath.getLevelImagePath(level, previousBuiltLevel));
        else
            return null;
    }

    /**
     * Method to obtain the image (as a pawn) of the God that the player has
     * @param worker
     * @return the corresponding image
     */
    private ImageView getWorkerImage(String worker) {
        if(GameBoardElementsPath.getWorkerImagePath(worker) != null)
            return new ImageView(GameBoardElementsPath.getWorkerImagePath(worker));
        else
            return null;
    }

    public void PlayerChoice(MouseEvent mouseEvent) {
        FakeCell[][] checkBoard = ViewManager.getGameBoardState();
        Node cell = (Node)mouseEvent.getSource();
        Integer row = board.getRowIndex(cell);
        Integer col = board.getColumnIndex(cell);
        Pane pane = (Pane)cell;
        if (GameLabel.getText().equals((ViewMessage.workerMessage) + "\n")){
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

    @Override
    public void informManagerInput(String input) {
      guiObserver.fromGuiInput(input);
    }

    public void exitApp(MouseEvent event){
        ViewManager.closeWindow();
    }

    public void setPlayersLabel(Object o,String currentNickname) {
        ArrayList<UserData> playerList = (ArrayList<UserData>) o;
        Label[] labels = {player1Label, player2Label, player3Label};
        Pane[] workers = {imagePlayer1,imagePlayer2,imagePlayer3};
        for (int i = 0; i < playerList.size(); i++) {
            if(playerList.get(i).getNickname().equals(currentNickname))
                labels[i].getParent().setId("currentPlayer");
            else
                labels[i].getParent().setId("player");
            ImageView image = new ImageView(GameBoardElementsPath.getWorkerImagePath(playerList.get(i).getCardChoosed().toLowerCase()));
            workers[i].getChildren().add(image);
            image.fitWidthProperty().bind(workers[i].widthProperty());
            image.fitHeightProperty().bind(workers[i].heightProperty());
            workers[i].setOpacity(1);
            labels[i].getParent().setOpacity(1);
            labels[i].setOpacity(1);
            labels[i].setText(playerList.get(i).getNickname());
            labels[i].setTextAlignment(TextAlignment.CENTER);
        }

    }


}
