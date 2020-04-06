package it.polimi.ingsw.PSP42.controller;

import it.polimi.ingsw.PSP42.*;
import it.polimi.ingsw.PSP42.model.*;
import it.polimi.ingsw.PSP42.view.ViewCLI;

public class ControllerCLI implements GameObserver {
   private final GameBoard g;
   private final ViewCLI view;
   public ControllerCLI(GameBoard model,ViewCLI v){
       g=model;
       view=v;
   }

    public String whatLevel(){
        ViewCLI view = new ViewCLI();
        return view.atlasRequest();
    }

    public String secondBuild() {
        ViewCLI view = new ViewCLI();
        return view.demeterRequest();
    }




    @Override
    public void update() {

    }
}
