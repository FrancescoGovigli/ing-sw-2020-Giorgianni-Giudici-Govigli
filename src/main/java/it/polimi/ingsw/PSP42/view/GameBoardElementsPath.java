package it.polimi.ingsw.PSP42.view;

public class GameBoardElementsPath {

    public static String GROUD = null;
    public static String LEVEL1 = "/images/lvl1.png";
    public static String LEVEL2 = "/images/lvl2.png";
    public static String LEVEL3 = "/images/lvl3.png";
    public static String DOME = "/images/lvl3Dome.png";

    public static String APOLLO_WORKER = "/images/Apollo/ApolloWorker.png";
    public static String ARTEMIS_WORKER = "/images/Artemis/ArtemisWorker.png";
    public static String ATHENA_WORKER = "/images/Athena/AthenaWorker.png";
    public static String ATLAS_WORKER = "/images/Atlas/AtlasWorker.png";
    public static String DEMETER_WORKER = "/images/Demeter/DemeterWorker.png";
    public static String HEPHAESTUS_WORKER = "/images/Hephaestus/HephaestusWorker.png";
    public static String MINOTAUR_WORKER = "/images/Minotaur/MinotaurWorker.png";
    public static String PAN_WORKER = "/images/Pan/PanWorker.png";
    public static String PROMETHEUS_WORKER = "/images/Prometheus/PrometheusWorker.png";
    public static String NONE = "/images/glassBackground.png";

    public static String getImagePath(int level) {
        if(level==0)
            return GROUD;
        if (level == 4)
            return DOME;
        return "/images/lvl" +level+".png";
    }

    public static String getWorkerImage(String worker){
        if (worker.equals(null))
            return NONE;
        switch (worker) {
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
        }
    }
}
