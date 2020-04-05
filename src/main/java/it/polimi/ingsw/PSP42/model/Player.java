package it.polimi.ingsw.PSP42.model;

public class Player {
    private final SimpleGod card;
    private final int id;
    private final String nickname;
    private final Worker worker1;
    private final Worker worker2;
    private enum State {WIN,LOSE,INGAME};
    private State playerState = State.INGAME;

    /**
     *Constructor to initialize a player object and istantiating 2 workers used by the player outside the Map cell(-1,-1)
     * @param nick nickname choosed from the player
     * @param id id automatically given to choose the order of gameplay during constructing
     * @param card choosed from the player
     */
    public Player(String nick, int id)/*,Simplegod card*/{
        SimpleGod card1;
        this.nickname = nick;
        this.id = id;
        this.worker1 = new Worker(-1,-1,this);
        this.worker2 = new Worker(-1,-1,this);
        //this.card = null;
        this.card = new Artemis(worker1,worker2);
    }

    /**
     * used to get the id of a player to know the order of the gameplay
     * @return id (it's an integer from 1 to 3 if the game is planned for 3 player)
     */
    public int getId() {
        return id;
    }

    /**
     * It is an important getter to know the status of a player during the game
     * @return
     */
    public State getPlayerState() {
        return playerState;
    }

    /**
     * Enum State used to know if a player is still in game, have lost or won the game.
     * The gameboard has methods to change state of player looking at the whole Game State
     * @param s
     */
    public void setPlayerState(String s){
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
     * @throws InvalidMoveException the move choosed isn't correct
     * @throws UnavailableWorkerException the worker choosed isnt able to move
     * @throws NotYourWorkerException the worker choosed isn't assigned to the player
     */
    public void setPosWorker(int x, int y, Worker w) throws InvalidMoveException, UnavailableWorkerException, NotYourWorkerException, InvalidBuildException {
        if(w.equals(worker1) || w.equals(worker2)) {
            if(w.getAvailable()){
               if(card==null){
                if(GameBoard.getInstance().moveAvailable(x, y, w))
                    w.setPosition(x, y);
                else
                    throw new InvalidMoveException("The cell selected isn't available for moving");
            }
            else
                if(card.powerAvailable(x,y,w))
                    card.setPower(x,y,w);

            else
               throw new UnavailableWorkerException("Your worker is blocked");
        }
        else
          throw new NotYourWorkerException("This worker is not yours");
      }

    }

    /**
     * Used to select (x,y) position for the building
     * @param x the position x of the cell in the matrix
     * @param y the position y of the cell in the matrix
     * @param w position is set for the worker w
     * @throws InvalidBuildException if the build method returns false
     */
    public void build(int x, int y, Worker w) throws InvalidBuildException{
        if(GameBoard.getInstance().buildAvailable(x,y,w))
            w.buildBlock(x, y);
        else
           throw new InvalidBuildException("The cell choosen is incorrect");
    }

    /**
     * Method used to initialize the worker on the gameboard in the cell(x,y)
     * @param x
     * @param y
     * @param w
     * @throws OccupiedCellException if the cell(x,y) is already occupied
     */
    public void setInitialPosition(int x, int y, Worker w) throws OccupiedCellException{
        if (w.getCurrentX() == -1 && w.getCurrentY() == -1) {
                if (GameBoard.getInstance().getCell(x, y).getWorker() == null)
                    w.setPosition(x, y);
           else
               throw new OccupiedCellException("Cell is already occupied");
        }
    }
}