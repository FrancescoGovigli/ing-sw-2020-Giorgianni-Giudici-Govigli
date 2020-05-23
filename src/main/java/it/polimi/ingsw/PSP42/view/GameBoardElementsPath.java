package it.polimi.ingsw.PSP42.view;

public class GameBoardElementsPath {

    public static String GROUND = null;
    public static String LEVEL1 = "/images/lvl1.png";
    public static String LEVEL2 = "/images/lvl2.png";
    public static String LEVEL3 = "/images/lvl3.png";
    public static String DOME = "/images/lvl4.png";
    //TODO add the missing paths
    public static String LEVEL1DOME = "";
    public static String LEVEL2DOME = "";
    public static String LEVEL3DOME = "";


    public static String PROMETHEUS_WORKER = "/images/Prometheus/PrometheusToken.png";

    /**
     * Method to obtain the path relative to the indicated level, considering the one on which it rests
     * @param level
     * @param previousBuiltLevel
     * @return the corresponding path as a string
     */
    public static String getImagePath(int level, int previousBuiltLevel) {
        if (level == 0)
            return GROUND;
        else if (level == 4 && previousBuiltLevel != 3)
            return "/images/lvl" + previousBuiltLevel + "dome.png";
        return "/images/lvl" + level + ".png";
    }

    public static String getWorkerImage(String worker){
        return PROMETHEUS_WORKER;
    }

}
