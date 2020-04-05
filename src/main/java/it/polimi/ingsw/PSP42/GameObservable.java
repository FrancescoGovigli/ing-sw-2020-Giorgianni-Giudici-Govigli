package it.polimi.ingsw.PSP42;

public interface GameObservable {
    /**
     * Aggiunge un osservatore alla lista dell'osservato
     * @param ob
     */
    public void attach(GameObserver ob);

    /**
     * Rimuove un osservatore dalla lista dell'osservato
     * @param ob
     */
    public void detach(GameObserver ob);

    /**
     * notifica tutti gli osservati interessati ad un cambiamento di stato
     */
    public abstract void notifyObservers();


}
