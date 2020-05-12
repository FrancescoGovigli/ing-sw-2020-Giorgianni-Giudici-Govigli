package it.polimi.ingsw.PSP42.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

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
}
