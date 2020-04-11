package it.polimi.ingsw.PSP42.model;

import java.util.HashMap;

public abstract class SimpleGod {

    boolean levelUp = true;
    //hashmap da modificare
    protected HashMap<String,String> hashMap;
    protected Worker w1;
    protected Worker w2;

    public SimpleGod(Worker w1,Worker w2){
        this.w1 = w1;
        this.w2= w2;
        initHashMap();
    }

    public void initHashMap(){
      //da fare
    };

    public abstract void godHashMap();

    public abstract boolean powerMoveAvailable(int x,int y, Worker w);

    public abstract void powerMove(int x, int y, Worker w);

    public abstract boolean powerBuildAvailable(int x, int y, int level, Worker w);

    public abstract void powerBuild(int x, int y, int level, Worker w);

    public abstract boolean powerEffectAvailable();

    public abstract void powerEffect();
}
