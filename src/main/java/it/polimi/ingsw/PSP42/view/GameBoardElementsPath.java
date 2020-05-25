package it.polimi.ingsw.PSP42.view;

public class GameBoardElementsPath {

    public static String GROUND = null;
    public static String LEVEL1 = "/images/lvl1.png";
    public static String LEVEL2 = "/images/lvl2.png";
    public static String LEVEL3 = "/images/lvl3.png";
    public static String LEVEL1DOME = "/images/lvl1dome.png";
    public static String LEVEL2DOME = "/images/lvl2dome.png";
    public static String LEVEL3DOME = "/images/lvl3dome.png";

    public static String NONE = "/images/glassBackground.png";
    public static String APOLLO_WORKER = "/images/apollo/apolloWorker.png";
    public static String ARTEMIS_WORKER = "/images/artemis/artemisWorker.png";
    public static String ATHENA_WORKER = "/images/athena/athenaWorker.png";
    public static String ATLAS_WORKER = "images/atlas/atlasWorker.png";
    public static String DEMETER_WORKER = "images/demeter/demeterWorker.png";
    public static String HEPHAESTUS_WORKER = "images/hephaestus/hephaestusWorker.png";
    public static String MINOTAUR_WORKER = "images/minotaur/minotaurWorker.png";
    public static String PAN_WORKER = "images/pan/panWorker.png";
    public static String PROMETHEUS_WORKER = "/images/prometheus/prometheusWorker.png";

    /**
     * Method to obtain the path relative to the indicated level, considering the one on which it rests
     * @param level
     * @param previousBuiltLevel
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
     * Method to obtain the path relative to the specified worker (god)
     * @param worker
     * @return the corresponding path as a string
     */
    public static String getWorkerImagePath(String worker) {
        if (worker.equals(null))
            return NONE;
        else
            return "/images/" + worker.toLowerCase() + "/" + worker.toLowerCase() + "Worker.png";
        /*switch (worker) {
            case "APOLLO": {
                return APOLLO_WORKER;
            }
            case "ARTEMIS": {
                return ARTEMIS_WORKER;
            }
            case "ATHENA": {
                return ATHENA_WORKER;
            }
            case "ATLAS": {
                return ATLAS_WORKER;
            }
            case "DEMETER": {
                return DEMETER_WORKER;
            }
            case "HEPHAESTUS": {
                return HEPHAESTUS_WORKER;
            }
            case "MINOTAUR": {
                return MINOTAUR_WORKER;
            }
            case "PAN": {
                return PAN_WORKER;
            }
            case "PROMETHEUS": {
                return PROMETHEUS_WORKER;
            }
            default:
                return NONE;
        }*/
    }
}
