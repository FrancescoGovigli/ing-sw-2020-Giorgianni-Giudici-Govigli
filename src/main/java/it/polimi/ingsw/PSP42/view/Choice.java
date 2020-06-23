package it.polimi.ingsw.PSP42.view;

public class Choice {

    private Integer idPlayer;
    private final Integer x;
    private final Integer y;
    private Integer worker;
    private Integer level;

    /**
     * Constructor to set Choice.
     * @param x position on the x-axis of the choice
     * @param y position on the y-axis of the choice
     * @param w integer that identifies the chosen worker
     * @param level integer that identifies the level
     * @param id integer that identifies the id of the player
     */
    public Choice(Integer x, Integer y, Integer w, Integer level, Integer id) {
        this.x = x;
        this.y = y;
        this.worker = w;
        this.level = level;
        this.idPlayer = id;
    }

    public Integer getIdPlayer() {
        return idPlayer;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getWorker(){
        return worker;
    }

    public Integer getLevel() {
        return level;
    }

    /**
     * Method used to find out if each Choice attribute is null, that means the player
     * has not chosen anything yet.
     * @return true if each Choice attribute is null, false otherwise
     */
    public boolean allFieldsNull() {
        return x == null && y == null && worker == null && level == null && idPlayer == null;
    }
}
