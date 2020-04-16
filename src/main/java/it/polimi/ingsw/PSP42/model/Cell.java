package it.polimi.ingsw.PSP42.model;

public class Cell {
    private boolean[] cellLevel;
    private Worker worker;

    /**
     * Constructor to create a map's cell which has set level 0 (the ground) to true and no worker above it self.
     */
    public Cell(){
        this.cellLevel = new boolean[]{true, false, false, false, false};
        this.worker = null;
    }

    /**
     * Method to obtain which level are build on the cell under form of array.
     * @return cellLevelCopy (array where the position indicates the constructed level)
     */
    public boolean[] getCellLevel() {
        boolean[] cellLevelCopy = new boolean[5];
        for (int i = 0; i < 5; i++)
            cellLevelCopy[i] = cellLevel[i];
        return cellLevelCopy;
    }

    /**
     * Method to obtain the current level of a cell.
     * @return level (level constructed)
     */
    public int getLevel(){
        int level = 0;
        for (int i = 0; i < 5; i++)
            if (cellLevel[i])
                level = i;
        return level;
    }

    /**
     * Method to increase the level of a cell.
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
     * Used to set a specific level instead of set the successive level.
     * @param level the level that the worker want to build.
     */
    public void setSpecificCellLevel(int level){
        if (level >= 0 && level <= 4)
            this.cellLevel[level] = true;
    }

    /**
     * Method to obtain which worker stay on the cell.
     * @return worker (if it is set, null otherwise)
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     * Method to set a worker on the cell.
     * @param w (is the worker to set)
     */
    public void setWorker(Worker w) {
        this.worker =  w;
    }

    /**
     * Method to unset a worker on the cell.
     */
    public void unSetWorker() {
        this.worker = null;
    }
}