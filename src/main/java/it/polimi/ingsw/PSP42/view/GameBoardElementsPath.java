package it.polimi.ingsw.PSP42.view;

public class GameBoardElementsPath {

    public static String GROUD = null;
    public static String LEVEL1 = "/images/lvl1.png";
    public static String LEVEL2 = "/images/lvl2.png";
    public static String LEVEL3 = "/images/lvl3.png";
    public static String DOME = "/images/lvl4.png";


    public static String PROMETHEUS_WORKER = "/images/Prometheus/PrometheusToken.png";

    public static String getImagePath(int level) {
        if(level==0)
            return GROUD;
        return "/images/lvl" +level+".png";
    }

    public static String getWorkerImage(String worker){
        return PROMETHEUS_WORKER;
    }

}
