package it.polimi.ingsw.PSP42.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameBoardTest {

    private GameBoard board = GameBoard.getInstance();
    private Player p = new Player("BOB", 1);
    private Worker w = new Worker(-1,-1, p);

    /*@Test
    public void reset() {
        assertEquals(null, GameBoard.getInstance());
    }*/

    @Test
    public void getInstance() {
        assertEquals(GameBoard.getInstance(), GameBoard.getInstance());
    }

    @Test
    public void getCell() {
        assertEquals(board.getCell(0, 0), GameBoard.getInstance().getCell(0, 0) );
    }

    /*@Test
    public void submatrixGenerator() {
        assertEquals(GameBoard.getInstance().getCell(0, 0), board.submatrixGenerator(1, 1));
    }*/

    @Test
    public void adjacentCellMoveAvailable() {
    }

    @Test
    public void moveAvailable() {
        w.setPosition(0, 0);
        assertEquals(true, board.moveAvailable(0, 1, w));
    }

    @Test
    public void adjacentCellBuildAvailable() {
    }

    @Test
    public void buildAvailable() {
        w.setPosition(0, 0);
        assertEquals(true, board.buildAvailable(0, 1, w));
    }

    @Test
    public void workerAvailable() {
        w.setPosition(0, 0);
        assertEquals(true, board.workerAvailable(w));
    }

    /*@Test
    public void loseCondition() throws InvalidBuildException, UnavailableWorkerException, NotYourWorkerException, InvalidMoveException {
        w.setPosition(1, 1);
        Player p2 = new Player("KEVIN", 1);
        Worker w2 = new Worker(-1,-1, p2);
        w2.setPosition(1, 0);
        Player p3 = new Player("STUART", 1);
        Worker w3 = new Worker(-1,-1, p3);
        w3.setPosition(0, 1);
        p.setPosWorker(0, 0, w);
        p.build(1, 1, w);
        assertEquals();
    }*/
}