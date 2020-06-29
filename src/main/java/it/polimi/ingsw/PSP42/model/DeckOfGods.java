package it.polimi.ingsw.PSP42.model;

public class DeckOfGods {

    public static String[] godsAvailable = {"APOLLO", "ARTEMIS", "ATHENA", "ATLAS", "DEMETER", "HEPHAESTUS", "MINOTAUR", "PAN", "PROMETHEUS"};

    /**
     * Method used to know which Gods are available.
     * @return possible gods
     */
    public static String[] possibleGods() {
        return godsAvailable;
    }

    /**
     * Method to assign to player's workers the powers of the chosen card.
     * @param cardName (God chosen)
     * @param worker1 Worker 1
     * @param worker2 Worker 2
     * @return cardSelected
     */
    public static SimpleGod setGod(String cardName, Worker worker1, Worker worker2) {
        SimpleGod cardSelected;
        switch(cardName) {
            case "APOLLO":
                cardSelected = new Apollo(worker1, worker2);
                break;
            case "ARTEMIS":
                cardSelected = new Artemis(worker1, worker2);
                break;
            case "ATHENA":
                cardSelected = new Athena(worker1, worker2);
                break;
            case "ATLAS":
                cardSelected = new Atlas(worker1, worker2);
                break;
            case "DEMETER":
                cardSelected = new Demeter(worker1, worker2);
                break;
            case "HEPHAESTUS":
                cardSelected = new Hephaestus(worker1, worker2);
                break;
            case "MINOTAUR":
                cardSelected = new Minotaur(worker1, worker2);
                break;
            case "PAN":
                cardSelected = new Pan(worker1, worker2);
                break;
            case "PROMETHEUS":
                cardSelected = new Prometheus(worker1, worker2);
                break;
            default:
                cardSelected = new NoGod(worker1, worker2);
        }
        return cardSelected;
    }
}
