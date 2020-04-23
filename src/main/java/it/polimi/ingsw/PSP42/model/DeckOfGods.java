package it.polimi.ingsw.PSP42.model;

public class DeckOfGods {

    public static String[] godAvailable = {"APOLLO", "ARTEMIS", "ATHENA", "ATLAS", "DEMETER", "HEPHAESTUS", "MINOTAUR", "PAN", "PROMETHEUS"};

    public static String[] possibleGods(){return godAvailable;}

    public static SimpleGod setGod(String cardName, Worker worker1, Worker worker2){
        SimpleGod cardSelected;
        switch(cardName){
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