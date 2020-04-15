package it.polimi.ingsw.PSP42.model;

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
     * Constructor to initialize a player object and instantiating 2 workers used by the player outside the Map cell(-1,-1)
     * @param nick nickname choosed from the player
     * @param id   id automatically given to choose the order of gameplay during constructing
     * @param card choosed from the player
     */
    public Player(String nick, int id, int age /*, String cardName*/) {
        this.nickname = nick;
        this.age = age;
        this.id = id;
        this.worker1 = new Worker(- 1, - 1, this);
        this.worker2 = new Worker(- 1, - 1, this);
        //TODO
        //this.card = GodAvailable.setGod(cardName, worker1, worker2);

        //this.card = NoGod;
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
     * Used to get the id of a player to know the order of the gameplay
     * @return id (it's an integer from 1 to 3 if the game is planned for 3 player)
     */
    public int getId() {
        return id;
    }

    /**
     * It is an important getter to know the status of a player during the game
     * @return
     */
    public String getPlayerState() {
        if(playerState.equals(State.INGAME))
            return "INGAME";
        if(playerState.equals(State.WIN))
            return "WIN";
        if(playerState.equals(State.LOSE))
            return "LOSE";
        return "NO_STATE";
    }

    /**
     * Enum State used to know if a player is still in game, have lost or won the game.
     * The gameboard has methods to change state of player looking at the whole Game State
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
     * In every move of a player it's important to get the GodCard assigned to the player
     * @return card
     */
    public SimpleGod getCard() {
        return card;
    }

    /**
     * Method used to move the worker w in (x,y) position
     * @param x is the x-coordinate for the move
     * @param y is the y-coordinate for the move
     * @param w is the worker who moves
     */
    public boolean move(int x, int y, Worker w) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
            return card.powerMove(x, y, w);
        return false;
    }

    /**
     * Method used to build with worker w in (x,y) position
     * @param x is the x-coordinate for the construction
     * @param y is the y-coordinate for the construction
     * @param w is the worker who builds
     */
    public boolean build(int x, int y, int level, Worker w) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4 && level >= 0 && level <= 4)
            return card.powerBuild(x, y, level, w);
        return false;
    }

    /**
     * TODO
     * @return
     */
    public boolean effect(){
        return card.powerEffect();
    }

    /**
     * Method used to initialize the worker in cell (x,y)
     * @param x is the initialization x-coordinate
     * @param y is the initialization y-coordinate
     * @param w is the worker initialized in gameboard
     */
    public boolean initialPosition ( int x, int y, Worker w){
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
            return card.powerInitialPosition(x, y, w);
        return false;
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