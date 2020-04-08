package it.polimi.ingsw.PSP42;

import it.polimi.ingsw.PSP42.controller.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.*;

public class App {
    public static void main(String[] args) {
        ViewCLI view = new ViewCLI();
        GameBoard g = GameBoard.getInstance();
        ControllerCLI control = new ControllerCLI(g,view);
        view.attach(control);
        g.attach(view);
        view.run();

    }
}
