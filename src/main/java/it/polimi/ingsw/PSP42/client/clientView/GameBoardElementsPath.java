package it.polimi.ingsw.PSP42.client.clientView;

public class GameBoardElementsPath {

    public static String GROUND = null;

    /**
     * Method to obtain the path related to the indicated level, considering the one on which it rests.
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
     * Method to obtain the path related to the specified worker (god).
     * @param worker String corresponding to a god
     * @return the corresponding path as a string
     */
    public static String getWorkerImagePath(String worker) {
        return "/images/" + worker.toLowerCase() + "/" + worker.toLowerCase() + "Worker.png";
    }

    /**
     * Method to obtain the path related to the specified image for player (god).
     * @param god String corresponding to a god
     * @return the corresponding path as a string
     */
    public static String getGodPlayerImagePath(String god) {
        return "/images/" + god.toLowerCase() + "/" + god.toLowerCase() + "Player.png";
    }
}
