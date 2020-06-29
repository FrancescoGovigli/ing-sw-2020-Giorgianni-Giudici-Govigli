package it.polimi.ingsw.PSP42.model;

public class Player {

    private final SimpleGod card;
    private final int id;
    private final String nickname;
    private final Worker worker1;
    private final Worker worker2;
    private enum State {WIN, LOSE, INGAME}
    private State playerState = State.INGAME;
    private Undo undo;  // UNDO

    public SimpleGod getCard() {
        return card;
    }

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

    public String getPlayerState() {
        if (playerState.equals(State.INGAME))
            return "INGAME";
        if (playerState.equals(State.WIN))
            return "WIN";
        if (playerState.equals(State.LOSE))
            return "LOSE";
        return "NO_STATE";
    }

    public void setPlayerState(String s) {
        playerState = State.valueOf(s);
    }

    /**
     * Constructor to initialize Player and generate his 2 workers setting them outside the map in cell(-1,-1).
     * @param nick nickname chosen from the player
     * @param id id automatically given to choose the order of gameplay during constructing
     * @param cardName chosen from the player
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
     * Method used to initialize the worker in cell (x,y).
     * @param x is the initialization x-coordinate
     * @param y is the initialization y-coordinate
     * @param w is the worker initialized in gameBoard
     * @return true if initial position is available, false otherwise
     */
    public boolean initialPosition(int x, int y, Worker w) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 4)
            return card.powerInitialPosition(x, y, w);
        return false;
    }

    /**
     * Method used to move the worker w in (x,y) position.
     * @param x is the x-coordinate for the move
     * @param y is the y-coordinate for the move
     * @param w is the worker who moves
     * @return true if move position is available, false otherwise
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
     * Method to cancel the move made by the worker.
     * @param w interested worker
     */
    public void doUndoMove(Worker w) {
        undo.undoMoveApply(w);
        undo.undoMoveDone();
    }

    /**
     * Method used to build with worker w in (x,y) position.
     * @param x is the x-coordinate for the construction
     * @param y is the y-coordinate for the construction
     * @param level Level
     * @param w is the worker who builds
     * @return true if build position is available, false otherwise
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
     * Method to cancel the build made by the worker.
     * @param w interested worker
     */
    public void doUndoBuild(Worker w) {
        undo.undoBuildApply(w);
        undo.undoBuildDone();
    }

    /**
     * Method used to obtain the effect of a god.
     * @return false if the god does not have an effect, true otherwise (this value depends on how the god implements the method)
     */
    public boolean effect(){
        return card.powerEffect();
    }

    /**
     * Method used to know the phase sequence of a Simple God.
     * @return an array of strings with variable size lines, initialized by the specific phases that a god has during his turn
     * Structured like:
     *  start   preMove     Move    preBuild    Build   End
     *  [   ]   [   ]       [   ]   [       ]   [   ]   [   ]
     */
    public String[][] checkWhatToDo(){
            return card.getWhatToDo();
        }
}
