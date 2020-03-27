package it.polimi.ingsw2020.PSP042.model;

public class Player {
    private SimpleGod card;
    int id;
    String nickname;
    Worker worker1;
    Worker worker2;

    /**
     *Constructor to initialize a player object and istantiating 2 workers used by the player outside the Map cell(-1,-1)
     * @param nick nickname choosed from the player
     * @param id id automatically given to choose the order of gameplay during constructing
     * @param card choosed from the player
     */
    public Player(String nick,int id,SimpleGod card){
        this.nickname = nick;
        this.id = id;
        this.card = card;
        worker1 = new Worker(-1,-1,this);
        worker2 = new Worker(-1,-1,this);

    }

    /**
     * used to get the id of a player to know the order of the gameplay
     * @return id (it's an integer from 1 to 3 if the game is planned for 3 player)
     */
    public int getId() {
        return id;
    }

    /**
     * used to get the unique nickname of a player
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * used to get the object Worker 1
     * @return worker1
     */
    public Worker getWorker1() {
        return worker1;
    }
    /**
     * used to get the object Worker 2
     * @return worker2
     */
    public Worker getWorker2() {
        return worker2;
    }

    /**
     * in every move of a player it's important to get the GodCard assigned to the player
     * @return card
     */
    public SimpleGod getCard() {
        return card;
    }

    /**
     * sets during a move the new position of the worker
     * @param x the position x of the cell in the matrix
     * @param y the position y of the cell in the matrix
     * @param w position is set for the worker w
     */
    public void setPosWorker(int x, int y, Worker w){
        w.setX(x);
        w.setY(y);
    }
    public void build(int x, int y, int lev, Worker w){
        w.buildBlock(int x,int y,int lev);
    }
}
