package it.polimi.ingsw.PSP42.view;

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

    @FXML
    public Label label;

    public void handleMoveWorker(MouseEvent event) {
        //Window owner = worker1.getScene().getWindow();
        //AlertHelper.moveAlert(Alert.AlertType.CONFIRMATION, owner, "Move", "Where do you wanna set your worker? Digit (x,y)");
        label.setText("Move your worker. Digit (x,y) position");
        event.consume();
    }

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
        if(image == null)
            return;
        else {
            image.fitWidthProperty().bind(cell.widthProperty());
            image.fitHeightProperty().bind(cell.heightProperty());
            cell.getChildren().add(image);
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
        ImageView image = getWorkerImage(playerData.getCardChoosed());
        if(image == null)
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

}
