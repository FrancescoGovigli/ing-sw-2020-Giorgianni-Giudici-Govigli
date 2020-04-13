package it.polimi.ingsw.PSP42.model;

public class NoGod extends SimpleGod{

    public NoGod(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] start = {"NULL"};
        String[] preMove = {"NULL"};
        String[] move = {"MOVE"};
        String[] preBuild = {"NULL"};
        String[] build = {"BUILD"};
        String[] end = {"NULL"};
        String[][] phase = {start, preMove, move, preBuild, build, end};
        return phase;
    }
}