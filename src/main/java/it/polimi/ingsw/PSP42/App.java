package it.polimi.ingsw.PSP42;

import it.polimi.ingsw.PSP42.controller.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

import java.util.*;

public class App {
    public static void main(String[] args) {
        VirtualView view = new VirtualView();
        GameBoard g = GameBoard.getInstance();
        ControllerCLI control = new ControllerCLI(g,view);
        view.attach(control);
        g.attach(view);
        control.runGame();


    }
}
