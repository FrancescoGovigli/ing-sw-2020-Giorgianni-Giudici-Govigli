package it.polimi.ingsw.PSP42.view;

public class GodsPath {

    public static String APOLLO = "-fx-background-image: url('/images/apollo/apollo.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String APOLLO_DESC = "-fx-background-image: url('/images/apollo/apolloDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ARTEMIS = "-fx-background-image: url('/images/artemis/artemis.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ARTEMIS_DESC = "-fx-background-image: url('/images/artemis/artemisDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATHENA = "-fx-background-image: url('/images/athena/athena.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATHENA_DESC = "-fx-background-image: url('/images/athena/athenaDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATLAS = "-fx-background-image: url('/images/atlas/atlas.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATLAS_DESC = "-fx-background-image: url('/images/atlas/atlasDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String DEMETER = "-fx-background-image: url('/images/demeter/demeter.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String DEMETER_DESC = "-fx-background-image: url('/images/demeter/demeterDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String HEPHAESTUS = "-fx-background-image: url('/images/hephaestus/hephaestus.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String HEPHAESTUS_DESC = "-fx-background-image: url('/images/hephaestus/hephaestusDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String MINOTAUR = "-fx-background-image: url('/images/minotaur/minotaur.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String MINOTAUR_DESC = "-fx-background-image: url('/images/minotaur/minotaurDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PAN = "-fx-background-image: url('/images/pan/pan.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PAN_DESC = "-fx-background-image: url('/images/pan/panDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PROMETHEUS = "-fx-background-image: url('/images/prometheus/prometheus.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PROMETHEUS_DESC = "-fx-background-image: url('/images/prometheus/prometheusDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String NONE = "-fx-opacity: 0;";

    public static String getGodStyle(String nameGod) {
        if (nameGod.equals(null))
            return NONE;
        else if (nameGod.contains("_"))
            return "-fx-background-image: url('/images/" + nameGod.toLowerCase() + "/" + nameGod.toLowerCase() + "Desc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
        else
            return "-fx-background-image: url('/images/" + nameGod.toLowerCase() + "/" + nameGod.toLowerCase() + ".png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
        /*switch (nameGod) {
            case "APOLLO": {
                return APOLLO;
            }
            case "APOLLO_DESC": {
                return APOLLO_DESC;
            }
            case "ARTEMIS": {
                return ARTEMIS;
            }
            case "ARTEMIS_DESC": {
                return ARTEMIS_DESC;
            }
            case "ATHENA": {
                return ATHENA;
            }
            case "ATHENA_DESC": {
                return ATHENA_DESC;
            }
            case "ATLAS": {
                return ATLAS;
            }
            case "ATLAS_DESC": {
                return ATLAS_DESC;
            }
            case "DEMETER": {
                return DEMETER;
            }
            case "DEMETER_DESC": {
                return DEMETER_DESC;
            }
            case "HEPHAESTUS": {
                return HEPHAESTUS;
            }
            case "HEPHAESTUS_DESC": {
                return HEPHAESTUS_DESC;
            }
            case "MINOTAUR": {
                return MINOTAUR;
            }
            case "MINOTAUR_DESC": {
                return MINOTAUR_DESC;
            }
            case "PAN": {
                return PAN;
            }
            case "PAN_DESC": {
                return PAN_DESC;
            }
            case "PROMETHEUS": {
                return PROMETHEUS;
            }
            case "PROMETHEUS_DESC": {
                return PROMETHEUS_DESC;
            }
            default:
                return NONE;
        }*/
    }
}
