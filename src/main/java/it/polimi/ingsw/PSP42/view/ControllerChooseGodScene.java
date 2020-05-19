package it.polimi.ingsw.PSP42.view;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;

public class ControllerChooseGodScene {

    private String nameGod;

    public void setNameGod(String nameGod) {
        this.nameGod = nameGod;
    }

    @FXML
    Button card2;

    private final String prometheus = "-fx-background-image: url('/images/Prometheus.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    private final String prometheusDesc = "-fx-background-image: url('/images/PrometheusDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    private final String apollo = "-fx-background-image: url('/images/Apollo.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    private final String apolloDesc = "-fx-background-image: url('/images/ApolloDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";

    public void goToGameBoardScene(ActionEvent event) throws IOException {
        Parent waitingSceneParent = FXMLLoader.load(getClass().getResource("/fxml/GameBoardScene.fxml"));
        Scene waitingScene = new Scene(waitingSceneParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(waitingScene);
        window.show();
    }

    public void setGods(Object listOfGods){
        List<String> gods = (List<String>)listOfGods;
        for (int i = 0; i <gods.size(); i++) {
            setImageGodCard(gods.get(i));
        }


    }

    public void setImageGodCard(String nameGod) {
        setNameGod(nameGod);
        if (nameGod.equals("Prometheus"))
            card2.setStyle(prometheus);
        if (nameGod.equals("Apollo"))
            card2.setStyle(apollo);
    }
    //Serve per quando passi sopra la carta
    public void description() {
        if (nameGod.equals("Prometheus"))
            card2.setStyle(prometheusDesc);
        if (nameGod.equals("Apollo"))
            card2.setStyle(apolloDesc);
    }

    public void image() {
        if (nameGod.equals("Prometheus"))
            card2.setStyle(prometheus);
        if (nameGod.equals("Apollo"))
            card2.setStyle(apollo);
    }
}