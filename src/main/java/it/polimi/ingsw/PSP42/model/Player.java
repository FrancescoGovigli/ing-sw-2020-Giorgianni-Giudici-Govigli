package it.polimi.ingsw.PSP42.model;

import java.util.*;

public class Player {
    private final SimpleGod card;
    private final int id;
    private final int age;
    private final String nickname;
    private final Worker worker1;
    private final Worker worker2;
    private enum State {WIN, LOSE, INGAME}
    private State playerState = State.INGAME;

    /**
     * Constructor to initialize a player object and istantiating 2 workers used by the player outside the Map cell(-1,-1)
     *
     * @param nick nickname choosed from the player
     * @param id   id automatically given to choose the order of gameplay during constructing
     * @param card choosed from the player
     */
    public Player(String nick, int id,int age/*,Simplegod card*/) {
        this.nickname = nick;
        this.age = age;
        this.id = id;
        this.worker1 = new Worker(- 1, - 1, this);
        this.worker2 = new Worker(- 1, - 1, this);
        this.card = null;
        //this.card = new Demeter(worker1, worker2);
        //this.card = new Atlas(worker1, worker2);
        //this.card = new Apollo(worker1, worker2);
        //this.card = new Artemis(worker1, worker2);
        //this.card = new Hephaestus(worker 1, worker2);
    }

    public int getAge() {
        return age;
    }

    /**
     * used to get the id of a player to know the order of the gameplay
     *
     * @return id (it's an integer from 1 to 3 if the game is planned for 3 player)
     */
    public int getId() {
        return id;
    }

    /**
     * It is an important getter to know the status of a player during the game
     *
     * @return
     */
    public State getPlayerState() {
        return playerState;
    }

    /**
     * Enum State used to know if a player is still in game, have lost or won the game.
     * The gameboard has methods to change state of player looking at the whole Game State
     *
     * @param s
     */
    public void setPlayerState(String s) {
        playerState = State.valueOf(s);
    }

    public String getNickname() {
        return nickname;
    }

    public Worker getWorker1() {
        return worker1;
    }

    public Worker getWorker2() {
        return worker2;
    }

    /**
     * in every move of a player it's important to get the GodCard assigned to the player
     *
     * @return card
     */
    public SimpleGod getCard() {
        return card;
    }

    /**
     * Method used to move the worker w in (x,y) position
     * @param x the position x of the cell in the matrix
     * @param y the position y of the cell in the matrix
     * @param w position is set for the worker w
     */
    public boolean move(int x, int y, Worker w) {
        return card.powerMove(x, y, w);
    }

    /**
     * Method used to build with worker w in (x,y) position
     * @param x the position x of the cell in the matrix
     * @param y the position y of the cell in the matrix
     * @param w build for the worker w
     */
    public boolean build(int x, int y, int level, Worker w) {
        return card.powerBuild(x, y, level, w);
    }

    /**
     * TODO
     * @return
     */
    public boolean effect(){
        return card.powerEffect();
    }

    /**
     * Method used to initialize the worker on the gameboard in the cell(x,y)
     * @param x
     * @param y
     * @param w
     */
    public boolean InitialPosition ( int x, int y, Worker w){
            return card.initPosition(x, y, w);
        }

    /**
     * Method used to know the phase sequence of a Simple God
     * @return an array of strings with variable size lines, initialized by the specific phases that a god has during his turn
     * Structured like:
     *  start   preMove     Move    preBuild    Build   End
     *  [   ]   [   ]       [   ]   [       ]   [   ]   [   ]
     */
    public String[][] checkWhatTodo(){
            return card.getWhatToDo();
        }
}