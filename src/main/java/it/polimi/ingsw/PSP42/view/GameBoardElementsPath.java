package it.polimi.ingsw.PSP42.view;

public class GameBoardElementsPath {

    public static String GROUND = null;
    public static String NONE = "/images/glassBackground.png";

    /**
     * Method to obtain the path relative to the indicated level, considering the one on which it rests.
     * @param level int of level to build
     * @param previousBuiltLevel int of precedent level before level
     * @return the corresponding path as a string
     */
    public static String getLevelImagePath(int level, int previousBuiltLevel) {
        if (level == 0)
            return GROUND;
        else if (level == 4)
            return "/images/lvl" + previousBuiltLevel + "dome.png";
        return "/images/lvl" + level + ".png";
    }

    /**
     * Method to obtain the path relative to the specified worker (god).
     * @param worker String corresponding to a god
     * @return the corresponding path as a string
     */
    public static String getWorkerImagePath(String worker) {
        // TODO check -> previous version : if (worker.equals(null))
        if (worker.equals("NONE"))
            return NONE;
        else
            return "/images/" + worker.toLowerCase() + "/" + worker.toLowerCase() + "Worker.png";
    }
}
