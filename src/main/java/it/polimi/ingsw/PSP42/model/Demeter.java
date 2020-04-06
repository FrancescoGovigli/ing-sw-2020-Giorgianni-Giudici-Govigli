package it.polimi.ingsw.PSP42.model;

import it.polimi.ingsw.PSP42.controller.ControllerCLI;

/**
 * This simple god allow a worker to build twice.
 */
public class Demeter extends YourBuildGod {

    private int counter = 1;
    private Cell precedentCell;

    public Demeter(Worker w1, Worker w2) {
        super(w1, w2);
    }

    public int getCounter() {
        return counter;
    }

    /**
     * Used to keep count of worker's build.
     * @param counter it's 1 or 2
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Used to set and keep in memory which was the precedent cell.
     * @param precedentCell first Cell where worker have build
     */
    private void setPrecedentCell(Cell precedentCell) {
        this.precedentCell = precedentCell;
    }

    public Cell getPrecedentCell() {
        return precedentCell;
    }

    /**
     * Used to verify if it's possible to build in that position.
     * Verify with different methods if it's the first or the second call of this method.
     * @param x position on the x-axis (if method called twice this parameter changed)
     * @param y position on the y-axis (if method called twice this parameter changed)
     * @param w worker that want to build
     * @return true if worker can build, false otherwise
     */
    @Override
    public boolean powerBuildAvailable(int x, int y, Worker w) {
        if(getCounter() == 1) {
            if(GameBoard.getInstance().buildAvailable(x, y, w)) {
                this.setPrecedentCell(GameBoard.getInstance().getCell(x, y));
                return true;
            }
            else
                return false;
        }
        else
            return this.powerSecondBuildAvailable(x, y, w);
    }

    /**
     * Used to build at first time in one position and, if player wants, in a second moment, in a different position.
     * @param x position on the x-axis (if method called twice this parameter changed)
     * @param y position on the y-axis (if method called twice this parameter changed)
     * @param w worker that wants to build
     */
    @Override
    public void buildPower(int x, int y, Worker w){
        if(getCounter() == 1) {
            w.buildBlock(x,y);
            setCounter(2);
            int newX = x-2;
            int newY = y-2;
            setCounter(2);
            w.getPlayer().build(newX, newY, w);
            /*
            ControllerCLI con = new ControllerCLI();
            String s = con.secondBuild();//method in controller that ask at player if,
                            // and if he wants, where to build the second block
            if(!s.equals("No")) {
                int newX = s.charAt(0) - 48;
                int newY = s.charAt(1) - 48;
                int newX = x-2;
                int newY = y-2;
                setCounter(2);
                w.getPlayer().build(newX, newY, w);
            }
            */
        }
        else {
            w.buildBlock(x, y);
            counter = 1;
            setPrecedentCell(null);
        }
    }

    /**
     * Verify if the cell where the worker wants to build is in the list of the cell available with Demeter's power.
     * @param x position of the cell on the x-axis
     * @param y position of the cell on the y-axis
     * @param w worker that wants to build
     * @return true if worker is able to build for the second time in that position, false otherwise
     */
    public boolean powerSecondBuildAvailable(int x, int y, Worker w) {
        Cell[] adj = this.adjacentCellBuildPowerAvailable(w.getCurrentX(), w.getCurrentY());
        for (int i = 0; i < adj.length; i++) {
            if (GameBoard.getInstance().getCell(x, y).equals(adj[i]))
                return true;
        }
        return false;
    }

    /**
     * Used to know in which cells the worker is able to build.
     * @param x position of the worker on x-axis
     * @param y position of the worker on y-axis
     * @return array of all available cells where worker can build
     */
    public Cell[] adjacentCellBuildPowerAvailable(int x, int y) {
        int index = 0;
        Cell[] adjCellBuildAvailable = new Cell[8];  // 8 is the maximum number of possible adjacent cell where move
        Cell[][] c = GameBoard.getInstance().submatrixGenerator(x, y);
        for (int i = 0; i < 3; i++) {    //searching around the cell(x,y)
            for (int j = 0; j < 3; j++) {
                if (c[i][j] != null &&      // c cell isn't out of map and and
                        (c[i][j].getLevel() != 4) &&        // is not 4th level and
                            (c[i][j].getWorker() == null) &&        //there isn't a worker
                                !(c[i][j].equals(getPrecedentCell())))     //it isn't the same cell where build before
                {
                    adjCellBuildAvailable[index] = c[i][j];
                    index++;
                }
            }
        }
        return adjCellBuildAvailable;
    }
}