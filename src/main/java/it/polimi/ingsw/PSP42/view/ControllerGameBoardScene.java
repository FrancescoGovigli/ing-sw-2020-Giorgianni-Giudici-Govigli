package it.polimi.ingsw.PSP42.view;


import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class ControllerGameBoardScene {

    @FXML
    public GridPane root;

    @FXML
    public GridPane board;

    @FXML
    public ImageView worker1;

    public void setSpecificLevel(int row, int col, int level, boolean dome) {
        Pane cell = (Pane)getPaneFromBoard(row,col);
        ImageView image = getLevelImage(level);
        if(image==null)
            return;
        else {
            image.fitWidthProperty().bind(cell.widthProperty());
            image.fitHeightProperty().bind(cell.heightProperty());
            cell.getChildren().add(image);
        }
    }

    public void setSpecificPlayer(int row, int col, UserData playerData) {
        Pane cell = (Pane)getPaneFromBoard(row,col);
        ImageView image = getWorkerImage(playerData.getCardChoosed());
        if(image==null)
            return;
        else {
            image.fitWidthProperty().bind(cell.widthProperty());
            image.fitHeightProperty().bind(cell.heightProperty());
            cell.getChildren().add(image);
        }
    }

    public void showGameMessage(Object message) {
    }

    private Node getPaneFromBoard(int row, int col) {
        for (Node node : board.getChildren()) {
            if (board.getColumnIndex(node) == col && board.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private ImageView getLevelImage(int level){
        if(GameBoardElementsPath.getImagePath(level)!=null)
         return new ImageView(GameBoardElementsPath.getImagePath(level));
        else
            return null;
    }

    private ImageView getWorkerImage(String worker){
        if(GameBoardElementsPath.getWorkerImage(worker)!=null)
            return new ImageView(GameBoardElementsPath.getWorkerImage(worker));
        else
            return null;
    }
}
