package it.polimi.ingsw.PSP42.view;

import it.polimi.ingsw.PSP42.model.*;

public class Choice {

    private final Integer x;
    private final Integer y;
    private Integer worker;
    private Integer level;

    public Choice(Integer x, Integer y,Integer w,Integer level){
        this.x=x;
        this.y=y;
        this.worker=w;
        this.level=level;
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

    public Integer getLevel() {

        return level;
    }

    public boolean allFieldsNull(){
        return x==null && y==null && worker==null && level==null;



    }
}
