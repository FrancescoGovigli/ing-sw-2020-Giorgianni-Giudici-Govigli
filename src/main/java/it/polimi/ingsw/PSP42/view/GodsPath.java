package it.polimi.ingsw.PSP42.view;

public class GodsPath {

    public static String APOLLO = "-fx-background-image: url('/images/Apollo/Apollo.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String APOLLO_DESC = "-fx-background-image: url('/images/Apollo/ApolloDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ARTEMIS = "-fx-background-image: url('/images/Artemis/Artemis.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ARTEMIS_DESC = "-fx-background-image: url('/images/Artemis/ArtemisDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATHENA = "-fx-background-image: url('/images/Athena/Athena.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATHENA_DESC = "-fx-background-image: url('/images/Athena/AthenaDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATLAS = "-fx-background-image: url('/images/Atlas/Atlas.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String ATLAS_DESC = "-fx-background-image: url('/images/Atlas/AtlasDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String DEMETER = "-fx-background-image: url('/images/Demeter/Demeter.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String DEMETER_DESC = "-fx-background-image: url('/images/Demeter/DemeterDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String HEPHAESTUS = "-fx-background-image: url('/images/Hephaestus/Hephaestus.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String HEPHAESTUS_DESC = "-fx-background-image: url('/images/Hephaestus/HephaestusDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String MINOTAUR = "-fx-background-image: url('/images/Minotaur/Minotaur.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String MINOTAUR_DESC = "-fx-background-image: url('/images/Minotaur/MinotaurDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PAN = "-fx-background-image: url('/images/Pan/Pan.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PAN_DESC = "-fx-background-image: url('/images/Pan/PanDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PROMETHEUS = "-fx-background-image: url('/images/Prometheus/Prometheus.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String PROMETHEUS_DESC = "-fx-background-image: url('/images/Prometheus/PrometheusDesc.png'); -fx-background-position: center; -fx-background-size: stretch; -fx-background-color: transparent";
    public static String NONE = "-fx-opacity: 0;";

    public static String getGodStyle(String nameGod) {

        try {
            switch (nameGod) {
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
            }
        } catch (NullPointerException e) {
            return NONE;
        }

        /*if (nameGod.equals(null))
            return NONE;
        switch (nameGod) {
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
        }
        */
    }

    public static String getImagePath(String nameGod) {
        Character firstLetter = nameGod.charAt(0);
        return "/images/" + firstLetter + nameGod.substring(1) + "/" + firstLetter + nameGod.substring(1) + ".png";
    }
}
