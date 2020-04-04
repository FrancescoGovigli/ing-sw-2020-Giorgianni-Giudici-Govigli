package it.polimi.ingsw.PSP42.model;

public class Atlas extends YourBuildGod {

    public Atlas(Worker w) {
        super(w);
    }

    public boolean buildPowerAvailable(int x, int y, Worker w) {
        return GameBoard.getInstance().buildAvailable(x, y, w);
    }

    public void buildSetPower(int x, int y, Worker w) {
        GameBoard.getInstance().getCell(x, y).setSpecificCellLevel(4);
    }
}
