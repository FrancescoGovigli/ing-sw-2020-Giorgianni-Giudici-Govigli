package it.polimi.ingsw.PSP42.model;

/**
 * @author Francesco Govigli
 */
public class Player {

    private final SimpleGod card;
    private final int id;
    private final String nickname;
    private final Worker worker1;
    private final Worker worker2;
    private enum State {WIN, LOSE, INGAME}
    private State playerState = State.INGAME;
    private Undo undo;  // UNDO

    /**
     * Constructor to initialize a player object and instantiating 2 workers used by the player outside the Map cell(-1,-1)
     * @param nick nickname choosed from the player
     * @param id   id automatically given to choose the order of gameplay during constructing
     * @param cardName choosed from the player
     */
    public Player(String nick, int id, String cardName) {
        this.nickname = nick;
        this.id = id;
        this.worker1 = new Worker(- 1, - 1, this);
        this.worker2 = new Worker(- 1, - 1, this);
        this.card = DeckOfGods.setGod(cardName, worker1, worker2);
        this.undo = new Undo(); // UNDO
    }

    /**
     * In every move of a player it's important to get the GodCard assigned to the player
     * @return card
     */
    public SimpleGod getCard() {
        return card;
    }

    /**
     * Used to get the id of a player to know the order of the gameplay
     * @return id (it's an integer from 1 to 3 if the game is planned for 3 player)
     */
    public int getId() {
        return id;
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
     * It is an important getter to know the status of a player during the game
     * @return
     */
    public String getPlayerState() {
        if (playerState.equals(State.INGAME))
            return "INGAME";
        if (playerState.equals(State.WIN))
            return "WIN";
        if (playerState.equals(State.LOSE))
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

    /**
     * Method used to initialize the worker in cell (x,y)
     * @param x is the initialization x-coordinate
     * @param y is the initialization y-coordinate
     * @param w is the worker initialized in gameboard
     */
    public boolean initialPosition( int x, int y, Worker w) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
            return card.powerInitialPosition(x, y, w);
        return false;
    }

    /**
     * Method used to move the worker w in (x,y) position
     * @param x is the x-coordinate for the move
     * @param y is the y-coordinate for the move
     * @param w is the worker who moves
     */
    public boolean move(int x, int y, Worker w) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4) {
            undo.undoMoveSet(w);    // UNDO
            if (card.powerMove(x, y, w)) {   //[preV]return card.powerMove(x, y, w);
                undo.undoMovePossible();
                return true;
            }
        }
        return false;
    }

    /**
     * Method to cancel the move made by the worker
     * @param w
     */
    public void doUndoMove(Worker w) {
        undo.undoMoveApply(w);
        undo.undoMoveDone();
    }

    /**
     * Method used to build with worker w in (x,y) position
     * @param x is the x-coordinate for the construction
     * @param y is the y-coordinate for the construction
     * @param w is the worker who builds
     */
    public boolean build(int x, int y, int level, Worker w) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4 && level >= 0 && level <= 4)
            undo.undoBuildSet(x, y, w);    // UNDO
            if (card.powerBuild(x, y, level, w)) {  //[preV]return card.powerBuild(x, y, level, w)
                undo.undoBuildPossible();
                return true;
            }
        return false;
    }

    /**
     * Method to cancel the build made by the worker
     * @param w
     */
    public void doUndoBuild(Worker w) {
        undo.undoBuildApply(w);
        undo.undoBuildDone();
    }

    /**
     * Method used to obtain the effect of a god
     * @return false if the god does not have an effect, true otherwise (this value depends on how the god implements the method)
     */
    public boolean effect(){
        return card.powerEffect();
    }

    /**
     * Method used to know the phase sequence of a Simple God
     * @return an array of strings with variable size lines, initialized by the specific phases that a god has during his turn
     * Structured like:
     *  start   preMove     Move    preBuild    Build   End
     *  [   ]   [   ]       [   ]   [       ]   [   ]   [   ]
     */
    public String[][] checkWhatToDo(){
            return card.getWhatToDo();
        }
}
