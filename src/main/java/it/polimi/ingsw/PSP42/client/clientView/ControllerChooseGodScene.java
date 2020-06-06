package it.polimi.ingsw.PSP42.client.clientView;

import it.polimi.ingsw.PSP42.client.GuiObservable;
import it.polimi.ingsw.PSP42.client.GuiObserver;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.*;

public class ControllerChooseGodScene implements GuiObservable {

    private final GuiObserver guiObserver = new ViewManager(ViewManager.getClientInstance());

    @FXML
    Button buttonCard1;
    private String godCard1;

    @FXML
    Button buttonCard2;
    private String godCard2;

    @FXML
    Button buttonCard3;
    private String godCard3;

    private Button getCard(int num) {
        if (num == 1)
            return buttonCard1;
        else if (num == 2)
            return buttonCard2;
        else
            return buttonCard3;
    }

    private void setCard(String godName, int num) {
        if (num == 1)
            this.godCard1 = godName;
        else if (num == 2)
            this.godCard2 = godName;
        else
            this.godCard3 = godName;
    }

    /**
     * Used to set on screen corresponding God's card in listOfGods.
     * If there aren't enough gods to fill spaces, then spaces remain with their original background.
     * @param listOfGods list of strings with name of gods received from ViewManager
     */
    public void setGods(Object listOfGods) {
        List gods = (List)listOfGods;
        for (int i = 0; i < gods.size(); i++)
            setImageGodCard(gods.get(i).toString(), i+1);
        if (gods.size() == 1) {
            setImageGodCard("NONE", 2);
            setImageGodCard("NONE", 3);
        }
        if (gods.size() == 2)
            setImageGodCard("NONE", 3);
    }

    /**
     * Used to set image for god's cards.
     * @param godName name for the linked image
     * @param cardNum number of the card to set
     */
    public void setImageGodCard(String godName, int cardNum) {
        setCard(godName, cardNum);
        getCard(cardNum).setStyle(getGodImageStyle(godName));
    }

    /**
     * Used to save card selection.
     * @param event click on the corresponding card
     */
    public void sendCardChosen(ActionEvent event) {
        Button clicked = (Button)event.getSource();
        if(clicked.equals(buttonCard1))
            informManagerInput(godCard1);
        else if(clicked.equals(buttonCard2))
            informManagerInput(godCard2);
        else if(clicked.equals(buttonCard3))
            informManagerInput(godCard3);
    }

    /**
     * Used to see card's description when event is verified.
     * @param event mouse entered
     */
    public void description(MouseEvent event) {
        String eventToString = event.getSource().toString();
        setChangingGodCard(getGodCardFromMouse(eventToString), getGodCardNumFromMouse(eventToString), "description");
    }

    /**
     * Used to see card when event is verified.
     * @param event mouse exited
     */
    public void image(MouseEvent event) {
        String cardToString = event.getSource().toString();
        setChangingGodCard(getGodCardFromMouse(cardToString), getGodCardNumFromMouse(cardToString), "image");
    }

    /**
     * Method to obtain the string godCard# corresponding to the event passed as a string.
     * @param event click on a button
     * @return godCard# (where # indicates an integer from 1 to 3)
     */
    private String getGodCardFromMouse(String event) {
        if (event.contains("buttonCard1"))
            return godCard1;
        else if (event.contains("buttonCard2"))
            return godCard2;
        else
            return godCard3;
    }

    /**
     * Method to obtain the integer corresponding to the event passed as a string.
     * @param event click on a button
     * @return an integer from 1 to 3
     */
    private int getGodCardNumFromMouse(String event) {
        if (event.contains("buttonCard1"))
            return 1;
        else if (event.contains("buttonCard2"))
            return 2;
        else
            return 3;
    }

    /**
     * Method that allows, during the choice of the divinity,
     * the display of the God card if the mouse is not on the card,
     * otherwise it shows the relative description.
     * @param godName String containing God's name
     * @param cardNum int
     * @param action ("image" if the mouse is not on the card, "description" otherwise)
     */
    public void setChangingGodCard(String godName, int cardNum, String action) {
        if (godName.equals("NONE"))
            getCard(cardNum).setStyle(getOpacityStyle());
        else if (action.equals("image"))
            getCard(cardNum).setStyle(getGodImageStyle(godName));
        else
            getCard(cardNum).setStyle(getGodDescriptionStyle(godName));
    }

    private String getOpacityStyle() {
        return "-fx-opacity: 0;";
    }

    /**
     * Method that returns the CSS style of the God given its name.
     * @param godName String containing God's name
     * @return CSS style
     */
    private String getGodImageStyle(String godName) {
        return "-fx-background-image: url('/images/" + godName.toLowerCase() + "/" + godName.toLowerCase() + ".png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent;";
    }

    /**
     * Method that returns the CSS style of the God description given its name.
     * @param godName String containing God's name
     * @return CSS style
     */
    private String getGodDescriptionStyle(String godName) {
        return "-fx-background-image: url('/images/" + godName.toLowerCase() + "/" + godName.toLowerCase() + "Desc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent;";
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