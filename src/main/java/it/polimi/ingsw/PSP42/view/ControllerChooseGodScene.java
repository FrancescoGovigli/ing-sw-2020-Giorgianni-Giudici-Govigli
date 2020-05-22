package it.polimi.ingsw.PSP42.view;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import java.io.*;
import java.util.*;

public class ControllerChooseGodScene {

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

    private Button card;

    private int counter = 0;



    public void goToGameBoardScene(ActionEvent event) throws IOException {
        Parent waitingSceneParent = FXMLLoader.load(getClass().getResource("/fxml/GameBoardScene.fxml"));
        Scene waitingScene = new Scene(waitingSceneParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(waitingScene);
        window.show();
    }

    public void setGods(Object listOfGods) {
        System.out.println("qua ci entra? setGods");
        List gods = (List)listOfGods;
        for (int i = 0; i <gods.size(); i++) {
            setImageGodCard(gods.get(i).toString(), i);
        }
    }

    /*public void setGods(Object message) {
        setImageGodCard(message.toString(), counter);
        counter++;
    }*/

    public void setImageGodCard(String nameGod, int i) {
        card = chooseCard(nameGod, i);
        switch (nameGod) {
            case "APOLLO": {
                card.setStyle(GodsPath.APOLLO);
                break;
            }
            case "ARTEMIS": {
                card.setStyle(GodsPath.ARTEMIS);
                break;
            }
            case "ATHENA": {
                card.setStyle(GodsPath.ATHENA);
                break;
            }
            case "ATLAS": {
                card.setStyle(GodsPath.ATLAS);
                break;
            }
            case "DEMETER": {
                card.setStyle(GodsPath.DEMETER);
                break;
            }
            case "HEPHAESTUS": {
                card.setStyle(GodsPath.HEPHAESTUS);
                break;
            }
            case "MINOTAUR": {
                card.setStyle(GodsPath.MINOTAUR);
                break;
            }
            case "PAN": {
                card.setStyle(GodsPath.PAN);
                break;
            }
            case "PROMETHEUS": {
                card.setStyle(GodsPath.PROMETHEUS);
                break;
            }
        }
    }

    //Serve per quando passi sopra la carta
    public void description(MouseEvent event) {
        Button card = (Button) event.getSource();
        String cardToString = (String) event.getSource().toString();
        String nameGod;
        if (cardToString.contains("card1"))
            nameGod = godCard1;
        else if (cardToString.contains("card2"))
            nameGod = godCard2;
        else if (cardToString.contains("card3"))
            nameGod = godCard3;
        else
            return;
        switch (nameGod) {
            case "APOLLO": {
                card.setStyle(GodsPath.APOLLO_DESC);
                break;
            }
            case "ARTEMIS": {
                card.setStyle(GodsPath.ARTEMIS_DESC);
                break;
            }
            case "ATHENA": {
                card.setStyle(GodsPath.ATHENA_DESC);
                break;
            }
            case "ATLAS": {
                card.setStyle(GodsPath.ATLAS_DESC);
                break;
            }
            case "DEMETER": {
                card.setStyle(GodsPath.DEMETER_DESC);
                break;
            }
            case "HEPHAESTUS": {
                card.setStyle(GodsPath.HEPHAESTUS_DESC);
                break;
            }
            case "MINOTAUR": {
                card.setStyle(GodsPath.MINOTAUR_DESC);
                break;
            }
            case "PAN": {
                card.setStyle(GodsPath.PAN_DESC);
                break;
            }
            case "PROMETHEUS": {
                card.setStyle(GodsPath.PROMETHEUS_DESC);
                break;
            }
        }
    }

    public void image(MouseEvent event) {
        Button card = (Button) event.getSource();
        String cardToString = (String) event.getSource().toString();
        String nameGod;
        if (cardToString.contains("card1"))
            nameGod = godCard1;
        else if (cardToString.contains("card2"))
            nameGod = godCard2;
        else if (cardToString.contains("card3"))
            nameGod = godCard3;
        else
            return;
        switch (nameGod) {
            case "APOLLO": {
                card.setStyle(GodsPath.APOLLO);
                break;
            }
            case "ARTEMIS": {
                card.setStyle(GodsPath.ARTEMIS);
                break;
            }
            case "ATHENA": {
                card.setStyle(GodsPath.ATHENA);
                break;
            }
            case "ATLAS": {
                card.setStyle(GodsPath.ATLAS);
                break;
            }
            case "DEMETER": {
                card.setStyle(GodsPath.DEMETER);
                break;
            }
            case "HEPHAESTUS": {
                card.setStyle(GodsPath.HEPHAESTUS);
                break;
            }
            case "MINOTAUR": {
                card.setStyle(GodsPath.MINOTAUR);
                break;
            }
            case "PAN": {
                card.setStyle(GodsPath.PAN);
                break;
            }
            case "PROMETHEUS": {
                card.setStyle(GodsPath.PROMETHEUS);
                break;
            }
        }
    }

    public Button chooseCard(String nameGod, int i) {
        if (i==0) {
            setGodCard1(nameGod);
            return card1;
        }
        if (i==1) {
            setGodCard2(nameGod);
            return card2;
        }
        if (i==2) {
            setGodCard3(nameGod);
            return card3;
        }
        else
            return null;
    }
}