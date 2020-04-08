package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.model.*;

public class Choice {

    private final Integer x;
    private final Integer y;
    private Integer worker;

    public Choice(Integer x, Integer y,Integer w){
        this.x=x;
        this.y=y;
        this.worker=w;
    }
    public Integer getW(){
        return worker;
    }
    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
    public boolean allfieldsnull(){
        return x==null && y==null && worker==null;



    }
}
