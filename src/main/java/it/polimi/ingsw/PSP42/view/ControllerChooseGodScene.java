package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import java.io.*;
import java.util.*;

public class ControllerChooseGodScene implements GuiObservable {
    private GuiObserver guiObserver = new ViewManager(ViewManager.getInstance());
    @FXML
    Button card1;
    private String godCard1;

    @FXML
    Button card2;
    private String godCard2;

    @FXML
    Button card3;
    private String godCard3;

    public void setGodCard1(String godCard1) {
        this.godCard1 = godCard1;
    }

    public void setGodCard2(String godCard2) {
        this.godCard2 = godCard2;
    }

    public void setGodCard3(String godCard3) {
        this.godCard3 = godCard3;
    }

    public void goToGameBoardScene(ActionEvent event) throws IOException {
        Button clicked = (Button)event.getSource();
        if(clicked.equals(card1))
         informManagerInput(godCard1);
        else if(clicked.equals(card2))
            informManagerInput(godCard2);
        else if(clicked.equals(card3))
            informManagerInput(godCard3);
        ViewManager.setLayout("/fxml/GameBoardScene.fxml");
    }


    public void setGods(Object listOfGods) {
        List gods = (List)listOfGods;
        int counter = 0;
        for (int i = 0; i <gods.size(); i++) {
            counter++;
            setImageGodCard(gods.get(i).toString(), i);
        }
        if (counter==1) {
            setImageGodCard("NONE", 2);
            setImageGodCard("NONE", 3);
        }
        else if (counter==2)
            setImageGodCard("NONE", 3);
    }

    /**
     * Used to set image for god's cards
     * @param godName name for the linked image
     * @param cardNum number of the card to set
     */
    public void setImageGodCard(String godName, int cardNum) {
        if (cardNum == 0) {
            setGodCard1(godName);
            card1.setStyle(GodsPath.getGodStyle(godName));
        }
        else if (cardNum == 1) {
            setGodCard2(godName);
            card2.setStyle(GodsPath.getGodStyle(godName));
        }
        else if (cardNum == 2) {
            setGodCard3(godName);
            card3.setStyle(GodsPath.getGodStyle(godName));
        }
    }

    /**
     * Used to see card's description when event is verified
     * @param event mouse entered
     */
    public void description(MouseEvent event) {
        String cardToString = event.getSource().toString();
        if (cardToString.contains("card1"))
            setChangingGodCard(godCard1, 0, "description");
        else if (cardToString.contains("card2"))
            setChangingGodCard(godCard2, 1, "description");
        else if (cardToString.contains("card3"))
            setChangingGodCard(godCard3, 2, "description");
    }

    /**
     * Used to see card when event is verified
     * @param event mouse exited
     */
    public void image(MouseEvent event) {
        String cardToString = event.getSource().toString();
        if (cardToString.contains("card1"))
            setChangingGodCard(godCard1, 0, "image");
        else if (cardToString.contains("card2"))
            setChangingGodCard(godCard2, 1, "image");
        else if (cardToString.contains("card3"))
            setChangingGodCard(godCard3, 2, "image");
    }

    public void setChangingGodCard(String godName, int cardNum, String action) {
        if (action.equals("image")) {
            if (cardNum == 0)
                card1.setStyle(GodsPath.getGodStyle(godName));
            else if (cardNum == 1)
                card2.setStyle(GodsPath.getGodStyle(godName));
            else if (cardNum == 2)
                card3.setStyle(GodsPath.getGodStyle(godName));
        }
        else if (action.equals("description")) {
            if (cardNum == 0)
                card1.setStyle(GodsPath.getGodStyle(godName+"_DESC"));
            else if (cardNum == 1)
                card2.setStyle(GodsPath.getGodStyle(godName+"_DESC"));
            else if (cardNum == 2)
                card3.setStyle(GodsPath.getGodStyle(godName+"_DESC"));
        }
    }

    @Override
    public void informManagerInput(String input) {
        guiObserver.fromGuiInput(input);
    }
}