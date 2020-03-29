package it.polimi.ingsw2020.PSP042.model;

/**
 * @author Daniele Giorgianni (Bob)
 */
public class Cell {
    private boolean[] cellLevel;
    private Worker worker;

    /**
     *Constructor to create a map's cell which has set level 0 (the ground) to true and no worker above it self
     */
    public Cell(){
        this.cellLevel = new boolean[]{true, false, false, false, false};
        this.worker = null;
    }

    /**
     *Function to obtain which level are build on the cell under form of array
     * @return cellLevel (array's address)
     */
    public boolean[] getCellLevel() {
        return cellLevel;
    }

    /**
     *Function to obtain the current level of a cell, which is i if the using are correct or -1 otherwise
     * @return i or -1
     */
    public int getLevel(){
        for (int i = 0; i < 5; i++) {
            if (cellLevel[i] && (i == 4 || !cellLevel[i + 1]))
                return i;
        }
        return (-1);
    }

    /**
     *Function to increase the level of a cell
     */
    public void setCellLevel() {
        for (int i = 0; i < 5; i++) {
            if (!cellLevel[i]) {
                this.cellLevel[i] = true;
                i = 5;
            }
        }
    }

    /**
     *Function to obtain which worker stay on the cell
     * @return worker (if it is set, null otherwise)
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     *Function to set a worker on the cell
     * @param w (is the worker to set)
     */
    public void setWorker(Worker w) {
        this.worker =  w;
    }

    /**
     *Function to unset a worker on the cell
     */
    public void unSetWorker() {
        this.worker = null;
    }

}