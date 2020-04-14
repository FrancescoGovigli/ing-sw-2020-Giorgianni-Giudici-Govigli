package it.polimi.ingsw.PSP42.model;

/**
 * TODO
 */
public class Minotaur extends SimpleGod{

    public Minotaur(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] START = {"NULL"};
        String[] PREMOVE = {"NULL"};
        String[] MOVE = {"move"};
        String[] PREBUILD = {"NULL"};
        String[] BUILD = {"build"};
        String[] END = {"NULL"};
        String[][] phase = {START, PREMOVE, MOVE, PREBUILD, BUILD, END};
        return phase;
    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        Cell[] adj = this.adjacentCellMovePowerAvailable(x, y, w);
        for (int i = 0; i < adj.length; i++) {
            if (GameBoard.getInstance().getCell(x, y).equals(adj[i]))
                return true;
        }
        return false;
    }

    @Override
    public boolean powerMove(int x, int y, Worker w) {
        if (effectMove && !effectPlayer.getCard().powerMoveAvailable(x, y, w))
            return false;
        if(powerMoveAvailable(x, y, w)) {
            if(canPushAway(x, y, w)) {
                w.setPosition(x, y);
                return true;
            }
        }
        return false;
    }

    public Cell[] adjacentCellMovePowerAvailable(int x, int y, Worker w) {
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(w.getCurrentX(), w.getCurrentY());
        for (int i = 0; i < 3; i++) {   //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                                      // c cell isn't out of map and and
                        (c[i][j].getWorker() == null ||                         // (there is no worker in the cell or
                                c[i][j].getWorker().getPlayer() != w.getPlayer()) &&   // worker to be push away is not of the same player) and
                        (c[i][j].getLevel() != 4) &&                            // is not 4th level and
                        ((c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() <= 1) &&    // one gap level on ascent and
                                (c[i][j].getLevel() - GameBoard.getInstance().getCell(x, y).getLevel() >= - 3)))   // limit for the descent
                {
                    adjCellMoveAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellMoveAvailable;
    }

    public boolean canPushAway(int x, int y, Worker w) {
        Worker opponentWorker = GameBoard.getInstance().getCell(x, y).getWorker();
        if(opponentWorker != null) {
            int deltaX = x - w.getCurrentX();
            int deltaY = y - w.getCurrentY();
            int newOpponentX = opponentWorker.getCurrentX() + deltaX;
            int newOpponentY = opponentWorker.getCurrentY() + deltaY;
            if (GameBoard.getInstance().getCell(newOpponentX, newOpponentY).getWorker() == null) {
                opponentWorker.setPosition(newOpponentX, newOpponentY);
                return true;
            }
            return false;
        }
        return true;
    }
}
