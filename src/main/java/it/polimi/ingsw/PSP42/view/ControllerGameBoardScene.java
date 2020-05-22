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

    @FXML
    public TextField textField;

    @FXML
    public Label label;

    @FXML
    public Button button;

    public void handleMoveWorker(MouseEvent event) {
        //Window owner = worker1.getScene().getWindow();
        //AlertHelper.moveAlert(Alert.AlertType.CONFIRMATION, owner, "Move", "Where do you wanna set your worker? Digit (x,y)");
        label.setText("Move your worker. Digit (x,y) position");
        event.consume();
    }

    public void handleButtonMoveWorker(ActionEvent event) {
        String[] s = null;
        String string = null;
        string = textField.getText();
        s = string.split(",");
        try {
            board.add(worker1, Integer.parseInt(s[0]), Integer.parseInt(s[1]));
        } catch (Exception e) {
            System.out.println("You write wrong");
        }
        textField.clear();
        event.consume();

    }

    public void handleDragDetected(MouseEvent event) {
        Dragboard db = worker1.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent cb = new ClipboardContent();
        Image image = worker1.getImage();
        cb.putImage(image);
        db.setContent(cb);
        event.consume();
    }

    public void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    public void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        Node node = event.getPickResult().getIntersectedNode();
        if(/*node != target && */db.hasImage()){
            Integer cIndex = GridPane.getColumnIndex(node);
            Integer rIndex = GridPane.getRowIndex(node);
            int x = cIndex == null ? 0 : cIndex;
            int y = rIndex == null ? 0 : rIndex;
            board.add(worker1, x, y);
        }
        event.consume();
    }

    public void setSpecificLevel(int i, int j, int level) {

    }

    public void setSpecificPlayer(int i, int j, UserData playerData) {
    }
}
