package it.polimi.ingsw.PSP42.model;

/**
 * Thanks this simple god a worker is able to move in a cell occupied by an opponent worker,
 * if the opponent worker can be pushed away in a free cell in same direction of the movement.
 */
public class Minotaur extends SimpleGod{

    public Minotaur(Worker w1, Worker w2) {
        super(w1, w2);
    }

    @Override
    public String[][] setPhase() {
        String[] START = {"EMPTY"};
        String[] PREMOVE = {"EMPTY"};
        String[] MOVE = {"MOVE"};
        String[] PREBUILD = {"EMPTY"};
        String[] BUILD = {"BUILD"};
        String[] END = {"EMPTY"};
        String[][] phase = {START, PREMOVE, MOVE, PREBUILD, BUILD, END};
        return phase;
    }

    @Override
    public boolean powerMoveAvailable(int x, int y, Worker w) {
        Cell[] adj = this.adjacentCellMovePowerAvailable(w.getCurrentX(), w.getCurrentY());
        for (int i = 0; i < adj.length; i++) {
            if (GameBoard.getInstance().getCell(x, y).equals(adj[i]))
                return true;
        }
        return false;
    }

    /**
     * Used to move "minotaur" worker in a cell and to push away opponent's worker if he is along the way.
     * @param x position on x-axis
     * @param y position on y-axis
     * @param w worker
     * @return true if worker moved, false otherwise
     */
    @Override
    public boolean powerMove(int x, int y, Worker w) {
        for (Player effectPlayer : effectPlayers) {
            if (effectPlayer != null && !effectPlayer.getCard().powerMoveAvailable(x, y, w))
                return false;
        }
        if(powerMoveAvailable(x, y, w)) {
            if(pushedAway(x, y, w)) {
                w.setPosition(x, y);
                return true;
            }
        }
        return false;
    }

    /**
     * Used to know in what cells worker is able to move thanks to minotaur power.
     * @param x position on x-axis
     * @param y position on y-axis
     * @return array of cells where it's possible to move
     */
    public Cell[] adjacentCellMovePowerAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellMoveAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {   //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&                                      // c cell isn't out of map and and
                        (c[i][j].getWorker() == null ||                         // (there is no worker in the cell or
                                c[i][j].getWorker().getPlayer() != GameBoard.getInstance().getCell(x, y).getWorker().getPlayer()) &&   // worker to be push away is not of the same player) and
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

    /**
     * Used to push in same direction of worker movement an opponent's worker if it's possible.
     * @param x position on x-axis where the opponent's worker can be
     * @param y position on y-axis where the opponent's worker can be
     * @param w "minotaur" worker
     * @return true if there isn't an opponent's worker or if opponent's worker was pushed away, false otherwise
     */
    public boolean pushedAway(int x, int y, Worker w) {
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
