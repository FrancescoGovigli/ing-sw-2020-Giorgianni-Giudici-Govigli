package it.polimi.ingsw.PSP42.model;

public class Cell {

    private boolean[] builtLevel;
    private Worker worker;

    /**
     * Constructor to create a map's cell which has level 0  (the ground) set true and no worker above it self.
     */
    public Cell() {
        this.builtLevel = new boolean[]{true, false, false, false, false};
        this.worker = null;
    }

    /**
     * Method to obtain which levels are build on the cell described by an array.
     * @return builtLevelCopy (array where the position indicates the constructed level)
     */
    public boolean[] getBuiltLevel() {
        boolean[] builtLevelCopy = new boolean[5];
        for (int i = 0; i < 5; i++)
            builtLevelCopy[i] = builtLevel[i];
        return builtLevelCopy;
    }

    /**
     * Method to obtain the current level of a cell.
     * @return level (level constructed)
     */
    public int getLevel() {
        int level = 0;
        for (int i = 0; i < 5; i++)
            if (builtLevel[i])
                level = i;
        return level;
    }

    /**
     * Method to increase the level of a cell.
     */
    public void setCellLevel() {
        for (int i = 0; i < 5; i++) {
            if (! builtLevel[i]) {
                this.builtLevel[i] = true;
                i = 5;
            }
        }
    }

    /**
     * Used to set a specific level instead of setting the next level as usual.
     * @param level the level that the worker want to build.
     */
    public void setSpecificCellLevel(int level) {
        if (level >= 0 && level <= 4) {
            this.builtLevel[level] = true;
            if (level < this.getLevel())
                for (int i = level + 1; i < builtLevel.length; i++)
                    builtLevel[i] = false;
        }
    }

    /**
     * Method to obtain which worker stays on the cell.
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
